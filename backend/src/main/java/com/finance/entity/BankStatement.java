package com.finance.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 银行流水（对账单）实体类
 * 
 * <p>用于记录企业银行账户的每一笔交易流水，通常从银行下载对账单后导入。
 * 银行流水用于与企业账面记录进行核对，确保账实相符。</p>
 * 
 * <p>支持银行对账功能，可以标识未达账项（企业已记账但银行未入账，
 * 或银行已入账但企业未记账）。</p>
 * 
 * @author 财务管理系统
 * @version 1.0
 * @since 2025-01-01
 */
@Data
@Entity
@Table(name = "bank_statement")
public class BankStatement {
    
    /** 流水唯一标识ID，数据库自增主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 银行账号，必填项，企业在该银行的账户号码，最大长度50字符 */
    @Column(nullable = false, length = 50)
    private String bankAccount;

    /** 银行名称，必填项，如：中国工商银行北京分行，最大长度100字符 */
    @Column(nullable = false, length = 100)
    private String bankName;

    /** 交易日期，必填项，银行记账日期 */
    @Column(nullable = false)
    private LocalDate transactionDate;

    /** 交易流水号，必填项，银行系统生成的唯一流水号，最大长度50字符 */
    @Column(nullable = false, length = 50)
    private String transactionNo;

    /** 交易类型，必填项，可选值：收入、支出，最大长度20字符 */
    @Column(nullable = false, length = 20)
    private String transactionType;

    /** 交易金额，必填项，精度15位，小数点后2位 */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    /** 余额，交易后的账户余额，精度15位，小数点后2位 */
    @Column(precision = 15, scale = 2)
    private BigDecimal balance;

    /** 对方户名，交易对方的账户名称，最大长度200字符 */
    @Column(length = 200)
    private String counterparty;

    /** 对方账号，交易对方的银行账号，最大长度50字符 */
    @Column(length = 50)
    private String counterpartyAccount;

    /** 用途/摘要，交易说明或备注，最大长度500字符 */
    @Column(length = 500)
    private String purpose;

    /**
     * 对账状态
     * 可选值：未对账、已对账、未达账项
     * 最大长度20字符
     */
    @Column(length = 20)
    private String reconciliationStatus;

    /** 匹配的会计分录ID，对账后关联到对应的会计分录 */
    @Column
    private Long matchedJournalEntryId;

    /** 对账日期，完成对账的日期 */
    @Column
    private LocalDate reconciliationDate;

    /** 备注信息，最大长度500字符 */
    @Column(length = 500)
    private String remark;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (reconciliationStatus == null) {
            reconciliationStatus = "未对账";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}










