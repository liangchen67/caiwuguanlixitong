package com.finance.repository;

import com.finance.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * 供应商信息数据访问接口
 * 
 * <p>提供供应商信息的数据库操作方法，支持按编码、状态、名称等条件查询。</p>
 * 
 * @author 财务管理系统
 * @version 1.0
 * @since 2025-01-01
 * @see Supplier
 */
@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    
    /**
     * 根据供应商编码查询供应商
     * 
     * @param code 供应商编码（唯一）
     * @return 供应商对象的 Optional 包装
     */
    Optional<Supplier> findByCode(String code);
    
    /**
     * 根据状态查询供应商列表
     * 
     * @param status 状态（启用、禁用）
     * @return 该状态的供应商列表
     */
    List<Supplier> findByStatus(String status);
    
    /**
     * 根据供应商名称模糊查询
     * 用于供应商搜索功能
     * 
     * @param name 供应商名称关键词
     * @return 名称包含关键词的供应商列表
     */
    List<Supplier> findByNameContaining(String name);
}
