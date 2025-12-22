package com.finance.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 企业信息实体
 */
@Data
@Entity
@Table(name = "company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name; // 企业名称

    @Column(length = 50)
    private String scale; // 企业规模（小型、中型、大型）

    @Column(precision = 15, scale = 2)
    private BigDecimal registeredCapital; // 注册资金

    @Column(length = 50)
    private String industry; // 所属行业

    @Column(length = 200)
    private String address; // 公司地址

    @Column(length = 50)
    private String legalPerson; // 法人代表

    @Column(length = 50)
    private String contactPhone; // 联系电话

    @Column(length = 100)
    private String email; // 邮箱

    @Column(length = 50)
    private String taxNumber; // 税号

    @Column(nullable = false)
    private LocalDateTime createdAt; // 创建时间

    @Column
    private LocalDateTime updatedAt; // 更新时间

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







