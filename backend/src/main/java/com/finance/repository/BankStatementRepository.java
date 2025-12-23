package com.finance.repository;

import com.finance.entity.BankStatement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

/**
 * 银行流水数据访问接口
 * 
 * <p>提供银行流水的数据库操作方法，支持按银行账号、交易日期、对账状态等条件查询。
 * 用于银行对账和现金流量管理。</p>
 * 
 * @author 财务管理系统
 * @version 1.0
 * @since 2025-01-01
 * @see BankStatement
 */
@Repository
public interface BankStatementRepository extends JpaRepository<BankStatement, Long> {
    
    /**
     * 根据银行账号查询所有流水记录
     * 
     * @param bankAccount 银行账号
     * @return 该账号的所有流水记录
     */
    List<BankStatement> findByBankAccount(String bankAccount);
    
    /**
     * 查询指定银行账号在指定日期范围内的流水记录
     * 用于银行对账
     * 
     * @param bankAccount 银行账号
     * @param startDate 开始日期（包含）
     * @param endDate 结束日期（包含）
     * @return 符合条件的流水记录列表
     */
    List<BankStatement> findByBankAccountAndTransactionDateBetween(
        String bankAccount, LocalDate startDate, LocalDate endDate);
    
    /**
     * 根据对账状态查询流水记录
     * 
     * @param status 对账状态（未对账、已对账、未达账项）
     * @return 该状态的流水记录列表
     */
    List<BankStatement> findByReconciliationStatus(String status);
    
    /**
     * 查询指定银行账号下指定对账状态的流水记录
     * 用于筛选未对账或未达账项
     * 
     * @param bankAccount 银行账号
     * @param status 对账状态
     * @return 符合条件的流水记录列表
     */
    List<BankStatement> findByBankAccountAndReconciliationStatus(
        String bankAccount, String status);
    
    /**
     * 查询指定日期范围内的所有流水记录
     * 
     * @param startDate 开始日期（包含）
     * @param endDate 结束日期（包含）
     * @return 日期范围内的流水记录列表
     */
    List<BankStatement> findByTransactionDateBetween(
        LocalDate startDate, LocalDate endDate);
}
