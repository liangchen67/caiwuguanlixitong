package com.finance.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 会计分录（记账凭证）实体类
 * 
 * <p>会计分录是财务核算的核心，记录每一笔经济业务的借贷双方科目和金额。
 * 每个分录包含凭证号、记账日期、摘要、借贷明细等信息。</p>
 * 
 * <p>遵循借贷记账法原则：有借必有贷，借贷必相等。
 * 每个分录可以关联多条明细行（JournalEntryLine）。</p>
 * 
 * @author 财务管理系统
 * @version 1.0
 * @since 2025-01-01
 */
@Data
@Entity
@Table(name = "journal_entry")
public class JournalEntry {
    
    /** 分录唯一标识ID，数据库自增主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 凭证号，必填项，唯一索引，如：JE20250101001，最大长度50字符 */
    @Column(nullable = false, length = 50, unique = true)
    private String voucherNo;

    /** 记账日期，必填项，用于确定会计期间 */
    @Column(nullable = false)
    private LocalDate entryDate;

    /** 摘要说明，必填项，描述业务内容，最大长度500字符 */
    @Column(nullable = false, length = 500)
    private String description;

    /** 总金额，必填项，借方或贷方的合计金额，精度15位，小数点后2位 */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;

    /** 业务类型，如：采购、销售、员工费用、银行存款等，最大长度50字符 */
    @Column(length = 50)
    private String businessType;

    /** 关联业务ID，用于追溯原始业务单据 */
    @Column
    private Long businessId;

    /** 状态，必填项，可选值：草稿、已过账、已审核，最大长度20字符 */
    @Column(nullable = false, length = 20)
    private String status;

    /** 制单人，记录创建凭证的用户，最大长度50字符 */
    @Column(length = 50)
    private String createdBy;

    /** 审核人，记录审核凭证的用户，最大长度50字符 */
    @Column(length = 50)
    private String approvedBy;

    /** 审核时间，记录凭证通过审核的时间 */
    @Column
    private LocalDateTime approvedAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    /**
     * 分录明细列表
     * 一对多关系，一个分录包含多条明细行
     * 使用级联操作，删除分录时自动删除所有明细
     */
    @OneToMany(mappedBy = "journalEntry", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<JournalEntryLine> entryLines;

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

