package com.finance.repository;

import com.finance.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * 员工信息数据访问接口
 * 
 * <p>提供员工信息的数据库操作方法，支持按员工号、状态、部门、姓名等条件查询。</p>
 * 
 * @author 财务管理系统
 * @version 1.0
 * @since 2025-01-01
 * @see Employee
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    /**
     * 根据员工编号查询员工
     * 
     * @param employeeNo 员工编号（唯一）
     * @return 员工对象的 Optional 包装
     */
    Optional<Employee> findByEmployeeNo(String employeeNo);
    
    /**
     * 根据在职状态查询员工列表
     * 
     * @param status 状态（在职、离职）
     * @return 该状态的员工列表
     */
    List<Employee> findByStatus(String status);
    
    /**
     * 根据部门查询员工列表
     * 
     * @param department 部门名称
     * @return 该部门的员工列表
     */
    List<Employee> findByDepartment(String department);
    
    /**
     * 根据员工姓名模糊查询
     * 用于员工搜索功能
     * 
     * @param name 员工姓名关键词
     * @return 姓名包含关键词的员工列表
     */
    List<Employee> findByNameContaining(String name);
}
