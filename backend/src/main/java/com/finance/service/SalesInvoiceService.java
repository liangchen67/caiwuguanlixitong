package com.finance.service;

import com.finance.entity.SalesInvoice;
import com.finance.entity.SalesInvoiceLine;
import com.finance.repository.SalesInvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class SalesInvoiceService {
    
    @Autowired
    private SalesInvoiceRepository salesInvoiceRepository;

    public List<SalesInvoice> findAll() {
        return salesInvoiceRepository.findAll();
    }

    public SalesInvoice findById(Long id) {
        return salesInvoiceRepository.findById(id).orElse(null);
    }

    public List<SalesInvoice> findByStatus(String status) {
        return salesInvoiceRepository.findByStatus(status);
    }

    public long count() {
        return salesInvoiceRepository.count();
    }

    public List<SalesInvoice> getLatest(int limit) {
        return salesInvoiceRepository.findAll(
            org.springframework.data.domain.PageRequest.of(0, limit, 
            org.springframework.data.domain.Sort.by(
                org.springframework.data.domain.Sort.Direction.DESC, "createdAt"
            ))
        ).getContent();
    }

    @Transactional
    public SalesInvoice save(SalesInvoice invoice) {
        // 如果是新增，生成销售单号
        if (invoice.getId() == null) {
            invoice.setInvoiceNo(generateInvoiceNo());
        }
        
        // 计算总金额
        if (invoice.getInvoiceLines() != null && !invoice.getInvoiceLines().isEmpty()) {
            BigDecimal total = BigDecimal.ZERO;
            BigDecimal taxTotal = BigDecimal.ZERO;
            
            for (SalesInvoiceLine line : invoice.getInvoiceLines()) {
                line.setSalesInvoice(invoice);
                total = total.add(line.getAmount());
                if (line.getTaxRate() != null) {
                    taxTotal = taxTotal.add(line.getAmount().multiply(line.getTaxRate()).divide(new BigDecimal(100)));
                }
            }
            
            invoice.setTotalAmount(total);
            invoice.setTaxAmount(taxTotal);
        }
        
        return salesInvoiceRepository.save(invoice);
    }

    @Transactional
    public void delete(Long id) {
        salesInvoiceRepository.deleteById(id);
    }

    private String generateInvoiceNo() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = salesInvoiceRepository.count() + 1;
        return String.format("SI-%s-%04d", date, count);
    }
}

