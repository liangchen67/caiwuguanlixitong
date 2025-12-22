package com.finance.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 会计分录实体
 */
@Data
@Entity
@Table(name = "journal_entry")
public class JournalEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String voucherNo; // 凭证号

    @Column(nullable = false)
    private LocalDate entryDate; // 记账日期

    @Column(nullable = false, length = 500)
    private String description; // 摘要说明

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount; // 总金额

    @Column(length = 50)
    private String businessType; // 业务类型：采购、销售、员工费用等

    @Column
    private Long businessId; // 关联业务ID

    @Column(nullable = false, length = 20)
    private String status; // 状态：草稿、已过账、已审核

    @Column(length = 50)
    private String createdBy; // 制单人

    @Column(length = 50)
    private String approvedBy; // 审核人

    @Column
    private LocalDateTime approvedAt; // 审核时间

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "journalEntry", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<JournalEntryLine> entryLines; // 分录明细

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

