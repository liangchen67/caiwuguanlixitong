package com.finance.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 销售单明细实体
 */
@Data
@Entity
@Table(name = "sales_invoice_line")
public class SalesInvoiceLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_invoice_id", nullable = false)
    @JsonBackReference
    private SalesInvoice salesInvoice; // 所属销售单

    @Column(nullable = false, length = 200)
    private String itemName; // 商品名称

    @Column(length = 50)
    private String itemCode; // 商品编码

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

