package com.finance.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 客户信息实体类
 * 
 * <p>用于管理企业的客户（销售对象）基本信息，包括客户编码、名称、
 * 联系方式、银行账户、税务信息等。</p>
 * 
 * <p>客户信息用于销售订单、应收账款管理等业务模块，
 * 是财务管理系统中的重要基础数据。</p>
 * 
 * @author 财务管理系统
 * @version 1.0
 * @since 2025-01-01
 */
@Data
@Entity
@Table(name = "customer")
public class Customer {
    
    /**
     * 客户唯一标识ID
     * 数据库自增主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 客户编码
     * 必填项，唯一索引，最大长度50字符
     * 用于快速识别和检索客户，如：CUS001、CUS002等
     */
    @Column(nullable = false, length = 50, unique = true)
    private String code;

    /**
     * 客户名称
     * 必填项，最大长度200字符
     * 客户的完整企业名称或个人姓名
     */
    @Column(nullable = false, length = 200)
    private String name;

    /**
     * 联系人
     * 客户方的主要联系人姓名
     * 最大长度200字符
     */
    @Column(length = 200)
    private String contactPerson;

    /**
     * 联系电话
     * 客户联系人的电话号码
     * 最大长度50字符
     */
    @Column(length = 50)
    private String contactPhone;

    /**
     * 电子邮箱
     * 客户的联系邮箱地址
     * 最大长度100字符
     */
    @Column(length = 100)
    private String email;

    /**
     * 地址
     * 客户的详细地址
     * 最大长度500字符
     */
    @Column(length = 500)
    private String address;

    /**
     * 纳税人识别号（税号）
     * 用于开具发票时填写
     * 最大长度50字符
     */
    @Column(length = 50)
    private String taxNumber;

    /**
     * 开户银行
     * 客户的银行开户行名称
     * 最大长度100字符
     */
    @Column(length = 100)
    private String bankName;

    /**
     * 银行账号
     * 客户的银行账户号码
     * 用于收付款业务
     * 最大长度50字符
     */
    @Column(length = 50)
    private String bankAccount;

    /**
     * 客户类别
     * 用于客户分类管理
     * 如：大客户、普通客户、VIP客户等
     * 最大长度50字符
     */
    @Column(length = 50)
    private String category;

    /**
     * 状态
     * 最大长度20字符
     * 可选值：启用、禁用
     * 禁用的客户不能创建新的业务单据
     */
    @Column(length = 20)
    private String status;

    /**
     * 备注
     * 记录客户的其他补充信息
     * 最大长度500字符
     */
    @Column(length = 500)
    private String remark;

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










