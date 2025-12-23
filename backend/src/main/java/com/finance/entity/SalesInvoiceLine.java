package com.finance.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 销售单明细实体类
 * 
 * <p>销售单的明细行，记录每个销售商品的详细信息，
 * 包括商品名称、编码、规格、数量、单价、金额等。</p>
 * 
 * @author 财务管理系统
 * @version 1.0
 * @since 2025-01-01
 */
@Data
@Entity
@Table(name = "sales_invoice_line")
public class SalesInvoiceLine {
    
    /** 明细行唯一标识ID，数据库自增主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 所属销售单，多对一关系，延迟加载 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_invoice_id", nullable = false)
    @JsonBackReference
    private SalesInvoice salesInvoice;

    /** 商品名称，必填项，最大长度200字符 */
    @Column(nullable = false, length = 200)
    private String itemName;

    /** 商品编码，用于商品识别，最大长度50字符 */
    @Column(length = 50)
    private String itemCode;

    /** 规格型号，商品的详细规格，最大长度100字符 */
    @Column(length = 100)
    private String specification;

    /** 计量单位，如：件、个、台、公斤等，最大长度20字符 */
    @Column(length = 20)
    private String unit;

    /** 数量，必填项，精度10位，小数点后2位 */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;

    /** 单价，必填项，精度15位，小数点后2位 */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal unitPrice;

    /** 金额，必填项，由数量×单价计算得出，精度15位，小数点后2位 */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    /** 税率，如：0.13表示13%的增值税，精度5位，小数点后2位 */
    @Column(precision = 5, scale = 2)
    private BigDecimal taxRate;

    /** 备注信息，最大长度500字符 */
    @Column(length = 500)
    private String remark;
}

