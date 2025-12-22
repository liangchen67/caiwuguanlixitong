package com.finance.repository;

import com.finance.entity.BankReconciliation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BankReconciliationRepository extends JpaRepository<BankReconciliation, Long> {
    
    List<BankReconciliation> findByBankAccount(String bankAccount);
    
    Optional<BankReconciliation> findByBankAccountAndReconciliationDate(
        String bankAccount, LocalDate reconciliationDate);
    
    List<BankReconciliation> findByReconciliationDateBetween(
        LocalDate startDate, LocalDate endDate);
}







