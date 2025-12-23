package com.finance.repository;

import com.finance.entity.SalesInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 销售单数据访问接口
 * 
 * <p>提供销售单的数据库操作方法，支持按单号、状态、日期范围、客户等条件查询。</p>
 * 
 * @author 财务管理系统
 * @version 1.0
 * @since 2025-01-01
 * @see SalesInvoice
 */
@Repository
public interface SalesInvoiceRepository extends JpaRepository<SalesInvoice, Long> {
    
    /**
     * 根据销售单号查询销售单
     * 
     * @param invoiceNo 销售单号（唯一）
     * @return 销售单对象的 Optional 包装
     */
    Optional<SalesInvoice> findByInvoiceNo(String invoiceNo);
    
    /**
     * 根据状态查询销售单列表
     * 
     * @param status 状态（草稿、已提交、已审核、已完成、已取消）
     * @return 该状态的销售单列表
     */
    List<SalesInvoice> findByStatus(String status);
    
    /**
     * 查询指定日期范围内的销售单
     * 用于销售统计和报表
     * 
     * @param startDate 开始日期（包含）
     * @param endDate 结束日期（包含）
     * @return 日期范围内的销售单列表
     */
    List<SalesInvoice> findByInvoiceDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * 根据客户ID查询该客户的所有销售单
     * 用于查看客户的销售历史
     * 
     * @param customerId 客户ID
     * @return 该客户的销售单列表
     */
    List<SalesInvoice> findByCustomerId(Long customerId);
}
