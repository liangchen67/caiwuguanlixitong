package com.finance.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 员工实体
 */
@Data
@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String employeeNo; // 员工编号

    @Column(nullable = false, length = 100)
    private String name; // 员工姓名

    @Column(length = 50)
    private String department; // 部门

    @Column(length = 50)
    private String position; // 职位

    @Column(length = 50)
    private String phone; // 联系电话

    @Column(length = 100)
    private String email; // 邮箱

    @Column
    private LocalDate hireDate; // 入职日期

    @Column(length = 20)
    private String status; // 状态：在职、离职

    @Column(length = 200)
    private String bankAccount; // 银行账号

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







