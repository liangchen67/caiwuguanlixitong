package com.finance.repository;

import com.finance.entity.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 会计分录数据访问接口
 * 
 * <p>提供会计分录的数据库操作方法，支持按凭证号、状态、日期范围等条件查询。</p>
 * 
 * @author 财务管理系统
 * @version 1.0
 * @since 2025-01-01
 * @see JournalEntry
 */
@Repository
public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
    
    /**
     * 根据凭证号查询会计分录
     * 
     * @param voucherNo 凭证号（唯一）
     * @return 分录对象的 Optional 包装
     */
    Optional<JournalEntry> findByVoucherNo(String voucherNo);
    
    /**
     * 根据状态查询分录列表
     * 
     * @param status 状态（草稿、已过账、已审核）
     * @return 该状态的分录列表
     */
    List<JournalEntry> findByStatus(String status);
    
    /**
     * 查询指定日期范围内的所有分录
     * 
     * @param startDate 开始日期（包含）
     * @param endDate 结束日期（包含）
     * @return 日期范围内的分录列表
     */
    List<JournalEntry> findByEntryDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * 查询指定日期范围和状态的分录
     * 用于统计某个会计期间的已过账分录
     * 
     * @param startDate 开始日期（包含）
     * @param endDate 结束日期（包含）
     * @param status 状态
     * @return 符合条件的分录列表
     */
    List<JournalEntry> findByEntryDateBetweenAndStatus(LocalDate startDate, LocalDate endDate, String status);
    
    /**
     * 查询指定日期之前且符合状态的分录
     * 
     * @param endDate 截止日期（不包含）
     * @param status 状态
     * @return 符合条件的分录列表
     */
    List<JournalEntry> findByEntryDateBeforeAndStatus(LocalDate endDate, String status);
    
    /**
     * 根据业务类型和业务ID查询相关联的分录
     * 用于追溯某个业务单据生成的会计分录
     * 
     * @param businessType 业务类型（如：采购、销售、员工费用）
     * @param businessId 业务单据ID
     * @return 关联的分录列表
     */
    @Query("SELECT e FROM JournalEntry e WHERE e.businessType = ?1 AND e.businessId = ?2")
    List<JournalEntry> findByBusinessTypeAndBusinessId(String businessType, Long businessId);
}

