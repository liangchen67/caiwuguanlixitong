package com.finance.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 供应商实体
 */
@Data
@Entity
@Table(name = "supplier")
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String code; // 供应商编码

    @Column(nullable = false, length = 200)
    private String name; // 供应商名称

    @Column(length = 200)
    private String contactPerson; // 联系人

    @Column(length = 50)
    private String contactPhone; // 联系电话

    @Column(length = 100)
    private String email; // 邮箱

    @Column(length = 500)
    private String address; // 地址

    @Column(length = 50)
    private String taxNumber; // 税号

    @Column(length = 100)
    private String bankName; // 开户行

    @Column(length = 50)
    private String bankAccount; // 银行账号

    @Column(length = 50)
    private String category; // 供应商类别

    @Column(length = 20)
    private String status; // 状态：启用、禁用

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







