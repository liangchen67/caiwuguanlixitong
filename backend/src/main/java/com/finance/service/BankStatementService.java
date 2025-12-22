package com.finance.service;

import com.finance.entity.BankStatement;
import com.finance.entity.JournalEntry;
import com.finance.repository.BankStatementRepository;
import com.finance.repository.JournalEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BankStatementService {

    @Autowired
    private BankStatementRepository bankStatementRepository;

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    public List<BankStatement> findAll() {
        return bankStatementRepository.findAll();
    }

    public BankStatement findById(Long id) {
        return bankStatementRepository.findById(id).orElse(null);
    }

    public List<BankStatement> findByBankAccount(String bankAccount) {
        return bankStatementRepository.findByBankAccount(bankAccount);
    }

    public List<BankStatement> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return bankStatementRepository.findByTransactionDateBetween(startDate, endDate);
    }

    public List<BankStatement> findUnreconciled(String bankAccount) {
        if (bankAccount != null && !bankAccount.isEmpty()) {
            return bankStatementRepository.findByBankAccountAndReconciliationStatus(
                bankAccount, "未对账");
        }
        return bankStatementRepository.findByReconciliationStatus("未对账");
    }

    @Transactional
    public BankStatement save(BankStatement statement) {
        return bankStatementRepository.save(statement);
    }

    @Transactional
    public List<BankStatement> batchSave(List<BankStatement> statements) {
        return bankStatementRepository.saveAll(statements);
    }

    @Transactional
    public void delete(Long id) {
        bankStatementRepository.deleteById(id);
    }

    /**
     * 自动匹配银行流水和会计分录
     */
    @Transactional
    public Map<String, Object> autoMatch(String bankAccount, LocalDate startDate, LocalDate endDate) {
        // 获取未对账的银行流水
        List<BankStatement> statements = bankStatementRepository
            .findByBankAccountAndTransactionDateBetween(bankAccount, startDate, endDate)
            .stream()
            .filter(s -> "未对账".equals(s.getReconciliationStatus()))
            .collect(Collectors.toList());

        // 获取同期间已过账的会计分录（涉及银行存款科目）
        List<JournalEntry> entries = journalEntryRepository
            .findByEntryDateBetweenAndStatus(startDate, endDate, "已过账");

        int matchedCount = 0;
        List<Map<String, Object>> matchedPairs = new ArrayList<>();

        // 简单匹配逻辑：金额和日期匹配
        for (BankStatement statement : statements) {
            for (JournalEntry entry : entries) {
                if (entry.getEntryDate().equals(statement.getTransactionDate()) &&
                    entry.getTotalAmount().compareTo(statement.getAmount()) == 0) {
                    
                    // 匹配成功
                    statement.setReconciliationStatus("已对账");
                    statement.setMatchedJournalEntryId(entry.getId());
                    statement.setReconciliationDate(LocalDate.now());
                    bankStatementRepository.save(statement);
                    
                    matchedCount++;
                    
                    Map<String, Object> pair = new HashMap<>();
                    pair.put("statement", statement);
                    pair.put("entry", entry);
                    matchedPairs.add(pair);
                    
                    break; // 找到匹配后跳出
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("matchedCount", matchedCount);
        result.put("totalStatements", statements.size());
        result.put("matchedPairs", matchedPairs);
        
        return result;
    }

    /**
     * 手动匹配
     */
    @Transactional
    public void manualMatch(Long statementId, Long journalEntryId) {
        BankStatement statement = bankStatementRepository.findById(statementId)
            .orElseThrow(() -> new RuntimeException("银行流水不存在"));
        
        statement.setReconciliationStatus("已对账");
        statement.setMatchedJournalEntryId(journalEntryId);
        statement.setReconciliationDate(LocalDate.now());
        
        bankStatementRepository.save(statement);
    }

    /**
     * 取消匹配
     */
    @Transactional
    public void unmatch(Long statementId) {
        BankStatement statement = bankStatementRepository.findById(statementId)
            .orElseThrow(() -> new RuntimeException("银行流水不存在"));
        
        statement.setReconciliationStatus("未对账");
        statement.setMatchedJournalEntryId(null);
        statement.setReconciliationDate(null);
        
        bankStatementRepository.save(statement);
    }

    /**
     * 获取统计信息
     */
    public Map<String, Object> getStatistics(String bankAccount) {
        List<BankStatement> all = bankAccount != null && !bankAccount.isEmpty() 
            ? bankStatementRepository.findByBankAccount(bankAccount)
            : bankStatementRepository.findAll();

        long total = all.size();
        long reconciled = all.stream()
            .filter(s -> "已对账".equals(s.getReconciliationStatus()))
            .count();
        long unreconciled = all.stream()
            .filter(s -> "未对账".equals(s.getReconciliationStatus()))
            .count();

        Map<String, Object> stats = new HashMap<>();
        stats.put("total", total);
        stats.put("reconciled", reconciled);
        stats.put("unreconciled", unreconciled);
        
        return stats;
    }
}

