package com.finance.service;

import com.finance.entity.PurchaseOrder;
import com.finance.entity.PurchaseOrderLine;
import com.finance.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PurchaseOrderService {
    
    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    public List<PurchaseOrder> findAll() {
        return purchaseOrderRepository.findAll();
    }

    public PurchaseOrder findById(Long id) {
        return purchaseOrderRepository.findById(id).orElse(null);
    }

    public List<PurchaseOrder> findByStatus(String status) {
        return purchaseOrderRepository.findByStatus(status);
    }

    public long count() {
        return purchaseOrderRepository.count();
    }

    public List<PurchaseOrder> getLatest(int limit) {
        return purchaseOrderRepository.findAll(
            org.springframework.data.domain.PageRequest.of(0, limit, 
            org.springframework.data.domain.Sort.by(
                org.springframework.data.domain.Sort.Direction.DESC, "createdAt"
            ))
        ).getContent();
    }

    @Transactional
    public PurchaseOrder save(PurchaseOrder order) {
        // 如果是新增，生成订单号
        if (order.getId() == null) {
            order.setOrderNo(generateOrderNo());
        }
        
        // 计算总金额
        if (order.getOrderLines() != null && !order.getOrderLines().isEmpty()) {
            BigDecimal total = BigDecimal.ZERO;
            BigDecimal taxTotal = BigDecimal.ZERO;
            
            for (PurchaseOrderLine line : order.getOrderLines()) {
                line.setPurchaseOrder(order);
                total = total.add(line.getAmount());
                if (line.getTaxRate() != null) {
                    taxTotal = taxTotal.add(line.getAmount().multiply(line.getTaxRate()).divide(new BigDecimal(100)));
                }
            }
            
            order.setTotalAmount(total);
            order.setTaxAmount(taxTotal);
        }
        
        return purchaseOrderRepository.save(order);
    }

    @Transactional
    public void delete(Long id) {
        purchaseOrderRepository.deleteById(id);
    }

    private String generateOrderNo() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = purchaseOrderRepository.count() + 1;
        return String.format("PO-%s-%04d", date, count);
    }
}

