package com.finance.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 银行流水实体
 */
@Data
@Entity
@Table(name = "bank_statement")
public class BankStatement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String bankAccount; // 银行账号

    @Column(nullable = false, length = 100)
    private String bankName; // 银行名称

    @Column(nullable = false)
    private LocalDate transactionDate; // 交易日期

    @Column(nullable = false, length = 50)
    private String transactionNo; // 交易流水号

    @Column(nullable = false, length = 20)
    private String transactionType; // 交易类型：收入、支出

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount; // 交易金额

    @Column(precision = 15, scale = 2)
    private BigDecimal balance; // 余额

    @Column(length = 200)
    private String counterparty; // 对方户名

    @Column(length = 50)
    private String counterpartyAccount; // 对方账号

    @Column(length = 500)
    private String purpose; // 用途/摘要

    @Column(length = 20)
    private String reconciliationStatus; // 对账状态：未对账、已对账、未达账项

    @Column
    private Long matchedJournalEntryId; // 匹配的会计分录ID

    @Column
    private LocalDate reconciliationDate; // 对账日期

    @Column(length = 500)
    private String remark; // 备注

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







