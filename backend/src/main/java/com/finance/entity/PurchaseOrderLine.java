package com.finance.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 采购订单明细实体类
 * 
 * <p>采购订单的明细行，记录每个采购物料的详细信息，
 * 包括物料名称、编码、规格、数量、单价、金额等。</p>
 * 
 * @author 财务管理系统
 * @version 1.0
 * @since 2025-01-01
 */
@Data
@Entity
@Table(name = "purchase_order_line")
public class PurchaseOrderLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id", nullable = false)
    @JsonBackReference
    private PurchaseOrder purchaseOrder; // 所属订单

    @Column(nullable = false, length = 200)
    private String itemName; // 物料名称

    @Column(length = 50)
    private String itemCode; // 物料编码

    @Column(length = 100)
    private String specification; // 规格型号

    @Column(length = 20)
    private String unit; // 单位

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity; // 数量

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal unitPrice; // 单价

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount; // 金额

    @Column(precision = 5, scale = 2)
    private BigDecimal taxRate; // 税率

    @Column(length = 500)
    private String remark; // 备注
}

