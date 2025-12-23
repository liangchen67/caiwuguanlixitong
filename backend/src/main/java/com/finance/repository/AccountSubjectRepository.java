package com.finance.repository;

import com.finance.entity.AccountSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * 会计科目数据访问接口
 * 
 * <p>提供会计科目的数据库操作方法，包括基本的 CRUD 以及按条件查询。</p>
 * 
 * @author 财务管理系统
 * @version 1.0
 * @since 2025-01-01
 * @see AccountSubject
 */
@Repository
public interface AccountSubjectRepository extends JpaRepository<AccountSubject, Long> {
    
    /**
     * 根据科目编码查询会计科目
     * 
     * @param code 科目编码（唯一）
     * @return 科目对象的 Optional 包装
     */
    Optional<AccountSubject> findByCode(String code);
    
    /**
     * 根据父级科目ID查询所有子科目
     * 用于构建科目树形结构
     * 
     * @param parentId 父级科目ID（0表示查询一级科目）
     * @return 子科目列表
     */
    List<AccountSubject> findByParentId(Long parentId);
    
    /**
     * 根据科目类型查询科目列表
     * 
     * @param type 科目类型（资产、负债、所有者权益、成本、损益）
     * @return 该类型的科目列表
     */
    List<AccountSubject> findByType(String type);
    
    /**
     * 根据启用状态查询科目列表
     * 
     * @param enabled 是否启用（true-启用，false-停用）
     * @return 符合条件的科目列表
     */
    List<AccountSubject> findByEnabled(Boolean enabled);
}










