package com.finance.repository;

import com.finance.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 企业信息数据访问接口
 * 
 * <p>继承自 JpaRepository，提供企业信息的 CRUD 操作。
 * 包含基本的增删改查功能，如 save、findById、findAll、delete 等。</p>
 * 
 * @author 财务管理系统
 * @version 1.0
 * @since 2025-01-01
 * @see Company
 * @see JpaRepository
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
}










