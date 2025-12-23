package com.finance.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 销售单（发票）实体类
 * 
 * <p>用于管理企业的销售业务，记录向客户销售商品或提供服务的详细信息，
 * 包括客户、销售日期、商品明细、金额、税额、收款情况等。</p>
 * 
 * <p>销售单可以生成会计分录，自动记入应收账款和主营业务收入科目。</p>
 * 
 * @author 财务管理系统
 * @version 1.0
 * @since 2025-01-01
 */
@Data
@Entity
@Table(name = "sales_invoice")
public class SalesInvoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String invoiceNo; // 销售单号

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer; // 客户

    @Column(nullable = false)
    private LocalDate invoiceDate; // 销售日期

    @Column
    private LocalDate dueDate; // 应收款到期日

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount; // 总金额

    @Column(precision = 15, scale = 2)
    private BigDecimal taxAmount; // 税额

    @Column(precision = 15, scale = 2)
    private BigDecimal receivedAmount; // 已收款金额

    @Column(nullable = false, length = 20)
    private String status; // 状态：草稿、已提交、已审核、已完成、已取消

    @Column(length = 50)
    private String paymentMethod; // 收款方式

    @Column(length = 500)
    private String remark; // 备注

    @Column(length = 50)
    private String createdBy; // 创建人

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "salesInvoice", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<SalesInvoiceLine> invoiceLines; // 销售单明细

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

