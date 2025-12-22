package com.finance.repository;

import com.finance.entity.SalesInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalesInvoiceRepository extends JpaRepository<SalesInvoice, Long> {
    Optional<SalesInvoice> findByInvoiceNo(String invoiceNo);
    List<SalesInvoice> findByStatus(String status);
    List<SalesInvoice> findByInvoiceDateBetween(LocalDate startDate, LocalDate endDate);
    List<SalesInvoice> findByCustomerId(Long customerId);
}







