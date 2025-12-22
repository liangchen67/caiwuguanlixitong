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

@Service
public class JournalEntryService {
    
    @Autowired
    private JournalEntryRepository journalEntryRepository;
    
    @Autowired
    private AccountSubjectRepository accountSubjectRepository;

    public List<JournalEntry> findAll() {
        return journalEntryRepository.findAll();
    }

    public JournalEntry findById(Long id) {
        return journalEntryRepository.findById(id).orElse(null);
    }

    public List<JournalEntry> findByStatus(String status) {
        return journalEntryRepository.findByStatus(status);
    }

    public List<JournalEntry> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return journalEntryRepository.findByEntryDateBetween(startDate, endDate);
    }

    public long count() {
        return journalEntryRepository.count();
    }

    public List<JournalEntry> getLatest(int limit) {
        return journalEntryRepository.findAll(
            org.springframework.data.domain.PageRequest.of(0, limit, 
            org.springframework.data.domain.Sort.by(
                org.springframework.data.domain.Sort.Direction.DESC, "createdAt"
            ))
        ).getContent();
    }

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

    @Transactional
    public void delete(Long id) {
        journalEntryRepository.deleteById(id);
    }

    @Transactional
    public JournalEntry post(Long id) {
        JournalEntry entry = findById(id);
        if (entry != null) {
            entry.setStatus("已过账");
            return journalEntryRepository.save(entry);
        }
        return null;
    }

    // 生成凭证号：格式 PZ-YYYYMMDD-序号
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

