package com.finance.repository;

import com.finance.entity.EmployeeExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 员工费用报销数据访问接口
 * 
 * <p>提供员工费用报销单的数据库操作方法，支持按单号、状态、员工、日期等条件查询。</p>
 * 
 * @author 财务管理系统
 * @version 1.0
 * @since 2025-01-01
 * @see EmployeeExpense
 */
@Repository
public interface EmployeeExpenseRepository extends JpaRepository<EmployeeExpense, Long> {
    
    /**
     * 根据费用单号查询费用报销单
     * 
     * @param expenseNo 费用单号（唯一）
     * @return 费用报销单对象的 Optional 包装
     */
    Optional<EmployeeExpense> findByExpenseNo(String expenseNo);
    
    /**
     * 根据审批状态查询费用报销单列表
     * 
     * @param status 状态（草稿、已提交、已审核、已支付、已拒绝）
     * @return 该状态的费用报销单列表
     */
    List<EmployeeExpense> findByStatus(String status);
    
    /**
     * 根据员工ID查询该员工的所有费用报销单
     * 用于查看员工的报销历史
     * 
     * @param employeeId 员工ID
     * @return 该员工的费用报销单列表
     */
    List<EmployeeExpense> findByEmployeeId(Long employeeId);
    
    /**
     * 查询指定日期范围内的费用报销单
     * 用于费用统计和报表
     * 
     * @param startDate 开始日期（包含）
     * @param endDate 结束日期（包含）
     * @return 日期范围内的费用报销单列表
     */
    List<EmployeeExpense> findByExpenseDateBetween(LocalDate startDate, LocalDate endDate);
}
