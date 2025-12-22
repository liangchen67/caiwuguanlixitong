package com.finance.service;

import com.finance.entity.BankReconciliation;
import com.finance.entity.BankStatement;
import com.finance.entity.JournalEntry;
import com.finance.repository.BankReconciliationRepository;
import com.finance.repository.BankStatementRepository;
import com.finance.repository.JournalEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BankReconciliationService {

    @Autowired
    private BankReconciliationRepository reconciliationRepository;

    @Autowired
    private BankStatementRepository bankStatementRepository;

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private BankStatementService bankStatementService;

    public List<BankReconciliation> findAll() {
        return reconciliationRepository.findAll();
    }

    public BankReconciliation findById(Long id) {
        return reconciliationRepository.findById(id).orElse(null);
    }

    public List<BankReconciliation> findByBankAccount(String bankAccount) {
        return reconciliationRepository.findByBankAccount(bankAccount);
    }

    @Transactional
    public BankReconciliation save(BankReconciliation reconciliation) {
        return reconciliationRepository.save(reconciliation);
    }

    @Transactional
    public void delete(Long id) {
        reconciliationRepository.deleteById(id);
    }

    /**
     * 自动对账（委托给BankStatementService）
     */
    @Transactional
    public Map<String, Object> autoMatch(String bankAccount, LocalDate startDate, LocalDate endDate) {
        return bankStatementService.autoMatch(bankAccount, startDate, endDate);
    }

    /**
     * 生成银行余额调节表
     */
    @Transactional
    public Map<String, Object> generateReconciliationReport(
            String bankAccount, 
            LocalDate reconciliationDate, 
            BigDecimal bankBalance,
            String createdBy) {
        
        // 1. 计算账面余额（银行存款科目余额）
        BigDecimal bookBalance = calculateBookBalance(bankAccount, reconciliationDate);

        // 2. 获取企业已记银行未记（未达账项）
        List<JournalEntry> bookOnly = getBookOnlyEntries(bankAccount, reconciliationDate);
        
        // 3. 获取银行已记企业未记（未达账项）
        List<BankStatement> bankOnly = getBankOnlyStatements(bankAccount, reconciliationDate);

        // 4. 计算调整金额
        BigDecimal bookOnlyIncome = BigDecimal.ZERO;
        BigDecimal bookOnlyExpense = BigDecimal.ZERO;
        
        for (JournalEntry entry : bookOnly) {
            // 简化处理：根据金额正负判断收支
            if (entry.getTotalAmount().compareTo(BigDecimal.ZERO) > 0) {
                bookOnlyIncome = bookOnlyIncome.add(entry.getTotalAmount());
            } else {
                bookOnlyExpense = bookOnlyExpense.add(entry.getTotalAmount().abs());
            }
        }

        BigDecimal bankOnlyIncome = BigDecimal.ZERO;
        BigDecimal bankOnlyExpense = BigDecimal.ZERO;
        
        for (BankStatement statement : bankOnly) {
            if ("收入".equals(statement.getTransactionType())) {
                bankOnlyIncome = bankOnlyIncome.add(statement.getAmount());
            } else {
                bankOnlyExpense = bankOnlyExpense.add(statement.getAmount());
            }
        }

        // 5. 计算调整后余额
        // 调整后账面余额 = 账面余额 + 银行已收企业未收 - 银行已付企业未付
        BigDecimal adjustedBookBalance = bookBalance
            .add(bankOnlyIncome)
            .subtract(bankOnlyExpense);

        // 调整后银行余额 = 银行余额 + 企业已收银行未收 - 企业已付银行未付
        BigDecimal adjustedBankBalance = bankBalance
            .add(bookOnlyIncome)
            .subtract(bookOnlyExpense);

        // 6. 判断是否平衡
        String status = adjustedBookBalance.compareTo(adjustedBankBalance) == 0 
            ? "平衡" : "不平衡";

        // 7. 保存对账记录
        BankReconciliation reconciliation = new BankReconciliation();
        reconciliation.setBankAccount(bankAccount);
        reconciliation.setReconciliationDate(reconciliationDate);
        reconciliation.setBookBalance(bookBalance);
        reconciliation.setBankBalance(bankBalance);
        reconciliation.setAdjustedBookBalance(adjustedBookBalance);
        reconciliation.setAdjustedBankBalance(adjustedBankBalance);
        reconciliation.setMatchedCount(getMatchedCount(bankAccount, reconciliationDate));
        reconciliation.setUnmatchedBookCount(bookOnly.size());
        reconciliation.setUnmatchedBankCount(bankOnly.size());
        reconciliation.setStatus(status);
        reconciliation.setCreatedBy(createdBy);

        reconciliation = reconciliationRepository.save(reconciliation);

        // 8. 组装返回数据
        Map<String, Object> result = new HashMap<>();
        result.put("reconciliation", reconciliation);
        result.put("bookBalance", bookBalance);
        result.put("bankBalance", bankBalance);
        result.put("adjustedBookBalance", adjustedBookBalance);
        result.put("adjustedBankBalance", adjustedBankBalance);
        result.put("bookOnlyEntries", bookOnly);
        result.put("bankOnlyStatements", bankOnly);
        result.put("bookOnlyIncome", bookOnlyIncome);
        result.put("bookOnlyExpense", bookOnlyExpense);
        result.put("bankOnlyIncome", bankOnlyIncome);
        result.put("bankOnlyExpense", bankOnlyExpense);
        result.put("status", status);

        return result;
    }

    /**
     * 计算账面余额
     */
    private BigDecimal calculateBookBalance(String bankAccount, LocalDate endDate) {
        // 简化处理：查询截止日期前所有银行存款相关凭证
        List<JournalEntry> entries = journalEntryRepository
            .findByEntryDateBeforeAndStatus(endDate.plusDays(1), "已过账");

        BigDecimal balance = BigDecimal.ZERO;
        // 这里需要根据实际的银行存款科目计算
        // 简化处理，后续可以优化
        return balance;
    }

    /**
     * 获取企业已记银行未记的凭证
     */
    private List<JournalEntry> getBookOnlyEntries(String bankAccount, LocalDate endDate) {
        // 获取已过账但未匹配的凭证
        List<JournalEntry> entries = journalEntryRepository
            .findByEntryDateBeforeAndStatus(endDate.plusDays(1), "已过账");

        // 过滤出未被银行流水匹配的凭证
        List<Long> matchedEntryIds = bankStatementRepository
            .findByBankAccount(bankAccount).stream()
            .filter(s -> s.getMatchedJournalEntryId() != null)
            .map(BankStatement::getMatchedJournalEntryId)
            .collect(Collectors.toList());

        return entries.stream()
            .filter(e -> !matchedEntryIds.contains(e.getId()))
            .filter(e -> e.getBusinessType() != null && e.getBusinessType().contains("银行"))
            .collect(Collectors.toList());
    }

    /**
     * 获取银行已记企业未记的流水
     */
    private List<BankStatement> getBankOnlyStatements(String bankAccount, LocalDate endDate) {
        return bankStatementRepository.findByBankAccount(bankAccount).stream()
            .filter(s -> s.getTransactionDate().isBefore(endDate.plusDays(1)))
            .filter(s -> "未对账".equals(s.getReconciliationStatus()))
            .collect(Collectors.toList());
    }

    /**
     * 获取已匹配数量
     */
    private int getMatchedCount(String bankAccount, LocalDate endDate) {
        return (int) bankStatementRepository.findByBankAccount(bankAccount).stream()
            .filter(s -> s.getTransactionDate().isBefore(endDate.plusDays(1)))
            .filter(s -> "已对账".equals(s.getReconciliationStatus()))
            .count();
    }

    /**
     * 获取对账详情
     */
    public Map<String, Object> getReconciliationDetail(Long id) {
        BankReconciliation reconciliation = reconciliationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("对账记录不存在"));

        Map<String, Object> detail = new HashMap<>();
        detail.put("reconciliation", reconciliation);
        
        // 获取未达账项
        List<BankStatement> bankOnly = getBankOnlyStatements(
            reconciliation.getBankAccount(), 
            reconciliation.getReconciliationDate());
        
        List<JournalEntry> bookOnly = getBookOnlyEntries(
            reconciliation.getBankAccount(), 
            reconciliation.getReconciliationDate());

        detail.put("bankOnlyStatements", bankOnly);
        detail.put("bookOnlyEntries", bookOnly);

        return detail;
    }
}

