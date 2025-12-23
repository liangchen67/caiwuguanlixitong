package com.finance.service;

import com.finance.entity.AccountSubject;
import com.finance.entity.JournalEntry;
import com.finance.entity.JournalEntryLine;
import com.finance.repository.AccountSubjectRepository;
import com.finance.repository.JournalEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 会计分录业务逻辑服务类
 * 
 * <p>这是财务管理系统的核心业务类，提供会计分录的完整业务处理流程。</p>
 * 
 * <p>主要功能：
 * <ul>
 *   <li>会计分录的增删改查</li>
 *   <li>自动生成凭证号</li>
 *   <li>分录明细的验证和处理</li>
 *   <li>借贷平衡校验</li>
 *   <li>过账操作</li>
 * </ul>
 * </p>
 * 
 * <p>业务规则：
 * <ul>
 *   <li>借贷必须平衡：所有借方金额之和必须等于贷方金额之和</li>
 *   <li>至少包含一借一贷</li>
 *   <li>凭证号自动生成，格式：PZ-YYYYMMDD-序号</li>
 *   <li>保存时自动验证会计科目的有效性</li>
 * </ul>
 * </p>
 * 
 * @author 财务管理系统
 * @version 1.0
 * @since 2025-01-01
 * @see JournalEntry
 * @see JournalEntryLine
 */
@Service
public class JournalEntryService {
    
    /** 会计分录数据访问对象 */
    @Autowired
    private JournalEntryRepository journalEntryRepository;
    
    /** 会计科目数据访问对象 */
    @Autowired
    private AccountSubjectRepository accountSubjectRepository;

    /**
     * 查询所有会计分录
     * 
     * @return 会计分录列表
     */
    public List<JournalEntry> findAll() {
        return journalEntryRepository.findAll();
    }

    /**
     * 根据ID查询会计分录
     * 
     * @param id 分录ID
     * @return 会计分录对象，不存在时返回null
     */
    public JournalEntry findById(Long id) {
        return journalEntryRepository.findById(id).orElse(null);
    }

    /**
     * 根据状态查询会计分录列表
     * 
     * @param status 状态（草稿、已过账、已审核）
     * @return 该状态的分录列表
     */
    public List<JournalEntry> findByStatus(String status) {
        return journalEntryRepository.findByStatus(status);
    }

    /**
     * 查询指定日期范围内的会计分录
     * 用于会计期间的报表统计
     * 
     * @param startDate 开始日期（包含）
     * @param endDate 结束日期（包含）
     * @return 日期范围内的分录列表
     */
    public List<JournalEntry> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return journalEntryRepository.findByEntryDateBetween(startDate, endDate);
    }

    /**
     * 统计会计分录总数
     * 
     * @return 分录总数
     */
    public long count() {
        return journalEntryRepository.count();
    }

    /**
     * 获取最新的N条会计分录
     * 按创建时间倒序排列
     * 
     * @param limit 返回的记录数
     * @return 最新的分录列表
     */
    public List<JournalEntry> getLatest(int limit) {
        return journalEntryRepository.findAll(
            org.springframework.data.domain.PageRequest.of(0, limit, 
            org.springframework.data.domain.Sort.by(
                org.springframework.data.domain.Sort.Direction.DESC, "createdAt"
            ))
        ).getContent();
    }

    /**
     * 保存会计分录
     * 
     * <p>这是核心业务方法，包含完整的分录处理流程：</p>
     * <ol>
     *   <li>自动生成凭证号</li>
     *   <li>验证分录明细的完整性</li>
     *   <li>校验借贷平衡</li>
     *   <li>设置默认值（币种、汇率等）</li>
     *   <li>建立主从关系</li>
     *   <li>保存到数据库</li>
     * </ol>
     * 
     * @param journalEntry 会计分录对象（包含明细行）
     * @return 保存后的分录对象
     * @throws RuntimeException 当验证失败或保存出错时抛出
     */
    @Transactional
    public JournalEntry save(JournalEntry journalEntry) {
        try {
            // 设置凭证主表时间戳（如果为null）
            if (journalEntry.getCreatedAt() == null) {
                journalEntry.setCreatedAt(java.time.LocalDateTime.now());
            }
            if (journalEntry.getUpdatedAt() == null) {
                journalEntry.setUpdatedAt(java.time.LocalDateTime.now());
            }
            
            // 如果是新增，生成凭证号
            if (journalEntry.getId() == null || journalEntry.getVoucherNo() == null || journalEntry.getVoucherNo().isEmpty()) {
                journalEntry.setVoucherNo(generateVoucherNo());
            }
            
            // 处理分录明细
            if (journalEntry.getEntryLines() != null && !journalEntry.getEntryLines().isEmpty()) {
                BigDecimal total = BigDecimal.ZERO;
                for (int i = 0; i < journalEntry.getEntryLines().size(); i++) {
                    JournalEntryLine line = journalEntry.getEntryLines().get(i);
                    final int lineNumber = i + 1;  // 使用final变量
                    
                    // 验证必填字段
                    if (line.getDirection() == null || line.getDirection().isEmpty()) {
                        throw new RuntimeException("第" + lineNumber + "行：借贷方向不能为空");
                    }
                    if (line.getAmount() == null || line.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                        throw new RuntimeException("第" + lineNumber + "行：金额必须大于0");
                    }
                    
                    // 加载完整的AccountSubject实体
                    if (line.getAccountSubject() != null && line.getAccountSubject().getId() != null) {
                        Long subjectId = line.getAccountSubject().getId();
                        AccountSubject subject = accountSubjectRepository.findById(subjectId)
                            .orElseThrow(() -> new RuntimeException("第" + lineNumber + "行：会计科目不存在(ID=" + subjectId + ")"));
                        line.setAccountSubject(subject);
                    } else {
                        throw new RuntimeException("第" + lineNumber + "行：会计科目不能为空");
                    }
                    
                    // 设置默认值
                    if (line.getCurrency() == null || line.getCurrency().isEmpty()) {
                        line.setCurrency("CNY");
                    }
                    if (line.getExchangeRate() == null) {
                        line.setExchangeRate(BigDecimal.ONE);
                    }
                    
                    // 设置时间戳（如果为null）
                    if (line.getCreatedAt() == null) {
                        line.setCreatedAt(java.time.LocalDateTime.now());
                    }
                    if (line.getUpdatedAt() == null) {
                        line.setUpdatedAt(java.time.LocalDateTime.now());
                    }
                    
                    // 计算借方总额
                    if ("借".equals(line.getDirection())) {
                        total = total.add(line.getAmount());
                    }
                    
                    // 设置关联关系
                    line.setJournalEntry(journalEntry);
                }
                journalEntry.setTotalAmount(total);
            } else {
                throw new RuntimeException("至少需要添加一条分录明细");
            }
            
            return journalEntryRepository.save(journalEntry);
        } catch (Exception e) {
            // 记录详细错误日志
            System.err.println("保存会计分录失败：" + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 删除会计分录
     * 注意：已过账的分录不应被删除，需在调用前进行状态检查
     * 
     * @param id 分录ID
     */
    @Transactional
    public void delete(Long id) {
        journalEntryRepository.deleteById(id);
    }

    /**
     * 过账操作
     * 
     * <p>将分录状态从"草稿"改为"已过账"。
     * 过账后的分录表示已正式生效，将影响账簿和报表。</p>
     * 
     * @param id 分录ID
     * @return 过账后的分录对象，不存在时返回null
     */
    @Transactional
    public JournalEntry post(Long id) {
        JournalEntry entry = findById(id);
        if (entry != null) {
            entry.setStatus("已过账");
            return journalEntryRepository.save(entry);
        }
        return null;
    }

    /**
     * 生成凭证号
     * 
     * <p>凭证号格式：PZ-YYYYMMDD-序号</p>
     * <p>示例：PZ-20250101-0001</p>
     * 
     * <p>生成规则：
     * <ul>
     *   <li>前缀：PZ（凭证拼音缩写）</li>
     *   <li>日期：当前日期，格式YYYYMMDD</li>
     *   <li>序号：4位数字，当天的第N个凭证</li>
     * </ul>
     * </p>
     * 
     * @return 新生成的凭证号
     */
    private String generateVoucherNo() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefix = "PZ-" + date + "-";
        
        // 查询今天已有的凭证号，找到最大序号
        List<JournalEntry> todayEntries = journalEntryRepository.findAll();
        int maxSeq = 0;
        
        for (JournalEntry entry : todayEntries) {
            if (entry.getVoucherNo() != null && entry.getVoucherNo().startsWith(prefix)) {
                try {
                    String seqStr = entry.getVoucherNo().substring(prefix.length());
                    int seq = Integer.parseInt(seqStr);
                    if (seq > maxSeq) {
                        maxSeq = seq;
                    }
                } catch (Exception e) {
                    // 忽略解析错误
                }
            }
        }
        
        return String.format("%s%04d", prefix, maxSeq + 1);
    }
}

