package com.finance.repository;

import com.finance.entity.BankStatement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BankStatementRepository extends JpaRepository<BankStatement, Long> {
    
    List<BankStatement> findByBankAccount(String bankAccount);
    
    List<BankStatement> findByBankAccountAndTransactionDateBetween(
        String bankAccount, LocalDate startDate, LocalDate endDate);
    
    List<BankStatement> findByReconciliationStatus(String status);
    
    List<BankStatement> findByBankAccountAndReconciliationStatus(
        String bankAccount, String status);
    
    List<BankStatement> findByTransactionDateBetween(
        LocalDate startDate, LocalDate endDate);
}







