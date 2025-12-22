package com.finance.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 银行对账记录实体
 */
@Data
@Entity
@Table(name = "bank_reconciliation")
public class BankReconciliation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String bankAccount; // 银行账号

    @Column(nullable = false)
    private LocalDate reconciliationDate; // 对账日期

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal bookBalance; // 账面余额（企业账上）

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal bankBalance; // 银行余额（银行对账单）

    @Column(precision = 15, scale = 2)
    private BigDecimal adjustedBookBalance; // 调整后账面余额

    @Column(precision = 15, scale = 2)
    private BigDecimal adjustedBankBalance; // 调整后银行余额

    @Column(nullable = false)
    private Integer matchedCount; // 已匹配笔数

    @Column(nullable = false)
    private Integer unmatchedBookCount; // 企业账未达账项笔数

    @Column(nullable = false)
    private Integer unmatchedBankCount; // 银行账未达账项笔数

    @Column(length = 20)
    private String status; // 状态：平衡、不平衡

    @Column(length = 500)
    private String remark; // 备注

    @Column(length = 50)
    private String createdBy; // 对账人

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







