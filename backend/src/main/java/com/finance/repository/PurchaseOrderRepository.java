package com.finance.repository;

import com.finance.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 采购订单数据访问接口
 * 
 * <p>提供采购订单的数据库操作方法，支持按订单号、状态、日期范围、供应商等条件查询。</p>
 * 
 * @author 财务管理系统
 * @version 1.0
 * @since 2025-01-01
 * @see PurchaseOrder
 */
@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    
    /**
     * 根据采购订单号查询订单
     * 
     * @param orderNo 订单号（唯一）
     * @return 采购订单对象的 Optional 包装
     */
    Optional<PurchaseOrder> findByOrderNo(String orderNo);
    
    /**
     * 根据状态查询采购订单列表
     * 
     * @param status 状态（草稿、已提交、已审核、已完成、已取消）
     * @return 该状态的订单列表
     */
    List<PurchaseOrder> findByStatus(String status);
    
    /**
     * 查询指定日期范围内的采购订单
     * 用于采购统计和报表
     * 
     * @param startDate 开始日期（包含）
     * @param endDate 结束日期（包含）
     * @return 日期范围内的订单列表
     */
    List<PurchaseOrder> findByOrderDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * 根据供应商ID查询该供应商的所有采购订单
     * 用于查看供应商的采购历史
     * 
     * @param supplierId 供应商ID
     * @return 该供应商的订单列表
     */
    List<PurchaseOrder> findBySupplierId(Long supplierId);
}
