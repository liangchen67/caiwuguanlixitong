package com.finance.repository;

import com.finance.entity.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
    Optional<JournalEntry> findByVoucherNo(String voucherNo);
    List<JournalEntry> findByStatus(String status);
    List<JournalEntry> findByEntryDateBetween(LocalDate startDate, LocalDate endDate);
    List<JournalEntry> findByEntryDateBetweenAndStatus(LocalDate startDate, LocalDate endDate, String status);
    List<JournalEntry> findByEntryDateBeforeAndStatus(LocalDate endDate, String status);
    
    @Query("SELECT e FROM JournalEntry e WHERE e.businessType = ?1 AND e.businessId = ?2")
    List<JournalEntry> findByBusinessTypeAndBusinessId(String businessType, Long businessId);
}

