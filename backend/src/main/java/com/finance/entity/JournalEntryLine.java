package com.finance.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 会计分录明细实体
 */
@Data
@Entity
@Table(name = "journal_entry_line")
public class JournalEntryLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_entry_id", nullable = false)
    @JsonBackReference
    private JournalEntry journalEntry; // 所属分录

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_subject_id", nullable = false)
    private AccountSubject accountSubject; // 会计科目

    @Column(nullable = false, length = 20)
    private String direction; // 借贷方向：借、贷

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount; // 金额

    @Column(length = 500)
    private String remark; // 备注

    @Column(length = 10)
    private String currency; // 币种（默认CNY）

    @Column(precision = 10, scale = 4)
    private BigDecimal exchangeRate; // 汇率

    @Column(precision = 15, scale = 2)
    private BigDecimal foreignAmount; // 外币金额
    
    @Column
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

