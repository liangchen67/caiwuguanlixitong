package com.finance.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 员工信息实体类
 * 
 * <p>用于管理企业员工的基本信息，包括员工编号、姓名、部门、职位、
 * 联系方式、入职日期、在职状态等。</p>
 * 
 * <p>员工信息用于员工报销、工资发放等业务场景。</p>
 * 
 * @author 财务管理系统
 * @version 1.0
 * @since 2025-01-01
 */
@Data
@Entity
@Table(name = "employee")
public class Employee {
    
    /** 员工唯一标识ID，数据库自增主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 员工编号，必填项，唯一索引，最大长度50字符 */
    @Column(nullable = false, length = 50, unique = true)
    private String employeeNo;

    /** 员工姓名，必填项，最大长度100字符 */
    @Column(nullable = false, length = 100)
    private String name;

    /** 所属部门，如：财务部、销售部、技术部等，最大长度50字符 */
    @Column(length = 50)
    private String department;

    /** 职位名称，如：会计、销售经理、软件工程师等，最大长度50字符 */
    @Column(length = 50)
    private String position;

    /** 联系电话，最大长度50字符 */
    @Column(length = 50)
    private String phone;

    /** 电子邮箱，最大长度100字符 */
    @Column(length = 100)
    private String email;

    /** 入职日期 */
    @Column
    private LocalDate hireDate;

    /** 在职状态，可选值：在职、离职，最大长度20字符 */
    @Column(length = 20)
    private String status;

    /** 银行账号，用于工资发放，最大长度200字符 */
    @Column(length = 200)
    private String bankAccount;

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










