package com.finance.repository;

import com.finance.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    Optional<PurchaseOrder> findByOrderNo(String orderNo);
    List<PurchaseOrder> findByStatus(String status);
    List<PurchaseOrder> findByOrderDateBetween(LocalDate startDate, LocalDate endDate);
    List<PurchaseOrder> findBySupplierId(Long supplierId);
}







