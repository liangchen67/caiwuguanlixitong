package com.finance.repository;

import com.finance.entity.BankReconciliation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 银行对账记录数据访问接口
 * 
 * <p>提供银行对账记录的数据库操作方法，用于记录和查询银行存款余额调节表。</p>
 * 
 * @author 财务管理系统
 * @version 1.0
 * @since 2025-01-01
 * @see BankReconciliation
 */
@Repository
public interface BankReconciliationRepository extends JpaRepository<BankReconciliation, Long> {
    
    /**
     * 根据银行账号查询所有对账记录
     * 用于查看某个银行账户的对账历史
     * 
     * @param bankAccount 银行账号
     * @return 该账号的所有对账记录
     */
    List<BankReconciliation> findByBankAccount(String bankAccount);
    
    /**
     * 查询指定银行账号在指定日期的对账记录
     * 用于检查是否已经对账
     * 
     * @param bankAccount 银行账号
     * @param reconciliationDate 对账日期
     * @return 对账记录的 Optional 包装
     */
    Optional<BankReconciliation> findByBankAccountAndReconciliationDate(
        String bankAccount, LocalDate reconciliationDate);
    
    /**
     * 查询指定日期范围内的所有对账记录
     * 用于统计分析
     * 
     * @param startDate 开始日期（包含）
     * @param endDate 结束日期（包含）
     * @return 日期范围内的对账记录列表
     */
    List<BankReconciliation> findByReconciliationDateBetween(
        LocalDate startDate, LocalDate endDate);
}
