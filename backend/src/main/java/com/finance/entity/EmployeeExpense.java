package com.finance.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 员工费用报销实体类
 * 
 * <p>用于管理员工的费用报销业务，记录员工发生的各类费用，
 * 包括差旅费、办公费、通讯费等，并进行审批和支付管理。</p>
 * 
 * <p>费用报销单审核通过后可以生成会计分录，记入相应的费用科目。</p>
 * 
 * @author 财务管理系统
 * @version 1.0
 * @since 2025-01-01
 */
@Data
@Entity
@Table(name = "employee_expense")
public class EmployeeExpense {
    
    /** 费用单唯一标识ID，数据库自增主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 费用单号，必填项，唯一索引，如：EXP20250101001，最大长度50字符 */
    @Column(nullable = false, length = 50, unique = true)
    private String expenseNo;

    /** 报销员工，多对一关系，立即加载 */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    /** 费用发生日期，必填项 */
    @Column(nullable = false)
    private LocalDate expenseDate;

    /**
     * 费用类型
     * 必填项，如：差旅费、办公费、通讯费、交通费等
     * 最大长度50字符
     */
    @Column(nullable = false, length = 50)
    private String expenseType;

    /** 报销金额，必填项，精度15位，小数点后2位 */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    /** 费用说明，描述费用的详细情况，最大长度500字符 */
    @Column(length = 500)
    private String description;

    /**
     * 审批状态
     * 必填项，可选值：草稿、已提交、已审核、已支付、已拒绝
     * 最大长度20字符
     */
    @Column(nullable = false, length = 20)
    private String status;

    /** 审批人，记录审批该费用单的用户，最大长度50字符 */
    @Column(length = 50)
    private String approvedBy;

    /** 审批时间，记录费用单通过审批的时间 */
    @Column
    private LocalDateTime approvedAt;

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
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}










