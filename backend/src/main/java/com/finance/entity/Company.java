package com.finance.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 企业信息实体类
 * 
 * <p>用于存储和管理企业的基本信息，包括企业名称、规模、注册资本、
 * 所属行业、法人代表、联系方式、税号等核心信息。</p>
 * 
 * <p>该实体类作为财务管理系统的基础数据，关联到整个系统的业务流程。</p>
 * 
 * @author 财务管理系统
 * @version 1.0
 * @since 2025-01-01
 */
@Data
@Entity
@Table(name = "company")
public class Company {
    
    /**
     * 企业唯一标识ID
     * 数据库自增主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 企业名称
     * 必填项，最大长度200字符
     */
    @Column(nullable = false, length = 200)
    private String name;

    /**
     * 企业规模
     * 可选值：小型、中型、大型
     * 最大长度50字符
     */
    @Column(length = 50)
    private String scale;

    /**
     * 注册资金
     * 精度为15位，小数点后2位
     * 单位：元（人民币）
     */
    @Column(precision = 15, scale = 2)
    private BigDecimal registeredCapital;

    /**
     * 所属行业
     * 如：软件和信息技术服务业、制造业等
     * 最大长度50字符
     */
    @Column(length = 50)
    private String industry;

    /**
     * 公司地址
     * 企业注册地址或办公地址
     * 最大长度200字符
     */
    @Column(length = 200)
    private String address;

    /**
     * 法人代表
     * 企业法定代表人姓名
     * 最大长度50字符
     */
    @Column(length = 50)
    private String legalPerson;

    /**
     * 联系电话
     * 企业主要联系电话
     * 最大长度50字符
     */
    @Column(length = 50)
    private String contactPhone;

    /**
     * 电子邮箱
     * 企业联系邮箱
     * 最大长度100字符
     */
    @Column(length = 100)
    private String email;

    /**
     * 纳税人识别号（税号）
     * 用于税务登记和发票开具
     * 最大长度50字符
     */
    @Column(length = 50)
    private String taxNumber;

    /**
     * 创建时间
     * 记录数据首次创建的时间戳
     * 必填项
     */
    @Column(nullable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     * 记录数据最后一次更新的时间戳
     */
    @Column
    private LocalDateTime updatedAt;

    /**
     * JPA生命周期回调：在持久化之前执行
     * 自动设置创建时间和更新时间为当前时间
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * JPA生命周期回调：在更新之前执行
     * 自动更新更新时间为当前时间
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}










