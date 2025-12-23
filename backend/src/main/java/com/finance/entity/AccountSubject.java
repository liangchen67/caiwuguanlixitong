package com.finance.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 会计科目实体类
 * 
 * <p>会计科目是会计核算的基础，用于对会计要素进行分类核算。
 * 本系统支持五种科目类型：资产、负债、所有者权益、成本、损益。</p>
 * 
 * <p>科目采用多级结构（最多5级），支持父子级关联，每个科目都有
 * 唯一的科目编码和明确的借贷方向。</p>
 * 
 * <p>示例科目编码：
 * <ul>
 *   <li>1001 - 库存现金（资产类）</li>
 *   <li>1002 - 银行存款（资产类）</li>
 *   <li>2201 - 应付账款（负债类）</li>
 *   <li>6001 - 主营业务收入（损益类）</li>
 * </ul>
 * </p>
 * 
 * @author 财务管理系统
 * @version 1.0
 * @since 2025-01-01
 */
@Data
@Entity
@Table(name = "account_subject")
public class AccountSubject {
    
    /**
     * 科目唯一标识ID
     * 数据库自增主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 科目编码
     * 必填项，唯一索引，最大长度50字符
     * 编码规则通常按照会计准则制定，如：1001、1002等
     */
    @Column(nullable = false, length = 50, unique = true)
    private String code;

    /**
     * 科目名称
     * 必填项，最大长度100字符
     * 如：库存现金、银行存款、应收账款等
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * 科目类型
     * 必填项，最大长度50字符
     * 可选值：资产、负债、所有者权益、成本、损益
     */
    @Column(nullable = false, length = 50)
    private String type;

    /**
     * 科目类别
     * 必填项，最大长度50字符
     * 用于更细致的分类，如：流动资产、固定资产、流动负债、非流动负债等
     */
    @Column(nullable = false, length = 50)
    private String category;

    /**
     * 父级科目ID
     * 用于构建科目树形结构
     * 0或null表示一级科目（顶级科目）
     */
    @Column
    private Long parentId;

    /**
     * 科目级别
     * 必填项，取值范围：1-5
     * 1表示一级科目，2表示二级科目，以此类推
     */
    @Column(nullable = false)
    private Integer level;

    /**
     * 借贷方向
     * 必填项
     * 可选值：借方、贷方
     * 表示该科目余额的正常方向
     */
    @Column(nullable = false)
    private String direction;

    /**
     * 是否启用
     * 必填项，默认值为true
     * true表示启用，false表示停用
     * 停用的科目不能用于新的业务单据
     */
    @Column(nullable = false)
    private Boolean enabled = true;

    /**
     * 科目说明
     * 对科目用途、核算范围等进行详细说明
     * 最大长度500字符
     */
    @Column(length = 500)
    private String description;

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










