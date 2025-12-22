package com.finance.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 员工费用实体
 */
@Data
@Entity
@Table(name = "employee_expense")
public class EmployeeExpense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String expenseNo; // 费用单号

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee; // 员工

    @Column(nullable = false)
    private LocalDate expenseDate; // 费用日期

    @Column(nullable = false, length = 50)
    private String expenseType; // 费用类型：差旅费、办公费、通讯费等

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount; // 金额

    @Column(length = 500)
    private String description; // 费用说明

    @Column(nullable = false, length = 20)
    private String status; // 状态：草稿、已提交、已审核、已支付、已拒绝

    @Column(length = 50)
    private String approvedBy; // 审批人

    @Column
    private LocalDateTime approvedAt; // 审批时间

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
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}







