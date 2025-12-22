package com.finance.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 采购订单实体
 */
@Data
@Entity
@Table(name = "purchase_order")
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String orderNo; // 订单号

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier; // 供应商

    @Column(nullable = false)
    private LocalDate orderDate; // 订单日期

    @Column
    private LocalDate expectedDate; // 预计到货日期

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount; // 总金额

    @Column(precision = 15, scale = 2)
    private BigDecimal taxAmount; // 税额

    @Column(precision = 15, scale = 2)
    private BigDecimal paidAmount; // 已付款金额

    @Column(nullable = false, length = 20)
    private String status; // 状态：草稿、已提交、已审核、已完成、已取消

    @Column(length = 50)
    private String paymentMethod; // 付款方式

    @Column(length = 500)
    private String remark; // 备注

    @Column(length = 50)
    private String createdBy; // 创建人

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PurchaseOrderLine> orderLines; // 订单明细

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

