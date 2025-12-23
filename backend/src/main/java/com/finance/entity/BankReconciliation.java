package com.finance.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 银行对账记录实体类
 * 
 * <p>用于记录银行账户的对账结果，对比企业账面记录与银行对账单，
 * 找出未达账项，编制银行存款余额调节表。</p>
 * 
 * <p>对账原理：
 * <ul>
 *   <li>调整后账面余额 = 账面余额 + 银行已收企业未收 - 银行已付企业未付</li>
 *   <li>调整后银行余额 = 银行余额 + 企业已收银行未收 - 企业已付银行未付</li>
 *   <li>如果两者相等，说明账实相符</li>
 * </ul>
 * </p>
 * 
 * @author 财务管理系统
 * @version 1.0
 * @since 2025-01-01
 */
@Data
@Entity
@Table(name = "bank_reconciliation")
public class BankReconciliation {
    
    /** 对账记录唯一标识ID，数据库自增主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 银行账号，必填项，被对账的银行账户，最大长度50字符 */
    @Column(nullable = false, length = 50)
    private String bankAccount;

    /** 对账日期，必填项，本次对账的截止日期 */
    @Column(nullable = false)
    private LocalDate reconciliationDate;

    /** 账面余额，必填项，企业账上记录的银行存款余额，精度15位，小数点后2位 */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal bookBalance;

    /** 银行余额，必填项，银行对账单显示的余额，精度15位，小数点后2位 */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal bankBalance;

    /** 调整后账面余额，加上未达账项调整后的账面余额，精度15位，小数点后2位 */
    @Column(precision = 15, scale = 2)
    private BigDecimal adjustedBookBalance;

    /** 调整后银行余额，加上未达账项调整后的银行余额，精度15位，小数点后2位 */
    @Column(precision = 15, scale = 2)
    private BigDecimal adjustedBankBalance;

    /** 已匹配笔数，必填项，企业账与银行账成功匹配的交易笔数 */
    @Column(nullable = false)
    private Integer matchedCount;

    /** 企业账未达账项笔数，必填项，企业已记账但银行未记账的笔数 */
    @Column(nullable = false)
    private Integer unmatchedBookCount;

    /** 银行账未达账项笔数，必填项，银行已记账但企业未记账的笔数 */
    @Column(nullable = false)
    private Integer unmatchedBankCount;

    /**
     * 对账状态
     * 可选值：平衡、不平衡
     * 平衡表示调整后两边余额相等，不平衡表示存在差异
     * 最大长度20字符
     */
    @Column(length = 20)
    private String status;

    /** 备注信息，记录对账过程中的特殊情况，最大长度500字符 */
    @Column(length = 500)
    private String remark;

    /** 对账人，执行本次对账的用户，最大长度50字符 */
    @Column(length = 50)
    private String createdBy;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

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










