package com.finance.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 会计分录明细实体类
 * 
 * <p>会计分录的明细行，记录每一笔经济业务的借方或贷方科目和金额。
 * 一个分录包含多条明细行，所有明细行的借方合计必须等于贷方合计。</p>
 * 
 * <p>支持外币业务，可记录外币金额和汇率。</p>
 * 
 * @author 财务管理系统
 * @version 1.0
 * @since 2025-01-01
 */
@Data
@Entity
@Table(name = "journal_entry_line")
public class JournalEntryLine {
    
    /** 明细行唯一标识ID，数据库自增主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 所属分录
     * 多对一关系，多条明细行属于一个分录
     * 延迟加载以提高性能
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_entry_id", nullable = false)
    @JsonBackReference
    private JournalEntry journalEntry;

    /**
     * 会计科目
     * 多对一关系，指定该明细行所使用的会计科目
     * 立即加载以便显示科目信息
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_subject_id", nullable = false)
    private AccountSubject accountSubject;

    /**
     * 借贷方向
     * 必填项，可选值：借、贷
     * 最大长度20字符
     */
    @Column(nullable = false, length = 20)
    private String direction;

    /**
     * 金额
     * 必填项，该科目的本次发生额
     * 精度15位，小数点后2位
     */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    /** 备注信息，对该明细行的补充说明，最大长度500字符 */
    @Column(length = 500)
    private String remark;

    /** 币种，默认CNY（人民币），用于外币业务，最大长度10字符 */
    @Column(length = 10)
    private String currency;

    /** 汇率，外币兑换人民币的汇率，精度10位，小数点后4位 */
    @Column(precision = 10, scale = 4)
    private BigDecimal exchangeRate;

    /** 外币金额，原始外币金额，精度15位，小数点后2位 */
    @Column(precision = 15, scale = 2)
    private BigDecimal foreignAmount;
    
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

