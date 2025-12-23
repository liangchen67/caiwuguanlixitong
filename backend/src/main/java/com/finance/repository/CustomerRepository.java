package com.finance.repository;

import com.finance.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * 客户信息数据访问接口
 * 
 * <p>提供客户信息的数据库操作方法，支持按编码、状态、名称等条件查询。</p>
 * 
 * @author 财务管理系统
 * @version 1.0
 * @since 2025-01-01
 * @see Customer
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    /**
     * 根据客户编码查询客户
     * 
     * @param code 客户编码（唯一）
     * @return 客户对象的 Optional 包装
     */
    Optional<Customer> findByCode(String code);
    
    /**
     * 根据状态查询客户列表
     * 
     * @param status 状态（启用、禁用）
     * @return 该状态的客户列表
     */
    List<Customer> findByStatus(String status);
    
    /**
     * 根据客户名称模糊查询
     * 用于客户搜索功能
     * 
     * @param name 客户名称关键词
     * @return 名称包含关键词的客户列表
     */
    List<Customer> findByNameContaining(String name);
}
