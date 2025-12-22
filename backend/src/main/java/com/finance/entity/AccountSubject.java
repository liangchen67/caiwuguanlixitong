package com.finance.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 会计科目实体
 */
@Data
@Entity
@Table(name = "account_subject")
public class AccountSubject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String code; // 科目编码

    @Column(nullable = false, length = 100)
    private String name; // 科目名称

    @Column(nullable = false, length = 50)
    private String type; // 科目类型：资产、负债、所有者权益、成本、损益

    @Column(nullable = false, length = 50)
    private String category; // 科目类别：流动资产、固定资产、流动负债等

    @Column
    private Long parentId; // 父级科目ID（0表示一级科目）

    @Column(nullable = false)
    private Integer level; // 科目级别（1-5级）

    @Column(nullable = false)
    private String direction; // 借贷方向：借方、贷方

    @Column(nullable = false)
    private Boolean enabled = true; // 是否启用

    @Column(length = 500)
    private String description; // 科目说明

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







