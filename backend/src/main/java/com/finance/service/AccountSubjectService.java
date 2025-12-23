package com.finance.service;

import com.finance.entity.AccountSubject;
import com.finance.repository.AccountSubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * 会计科目业务逻辑服务类
 * 
 * <p>提供会计科目的业务处理方法，包括科目的查询、维护、分类等操作。
 * 支持科目树形结构的构建和管理。</p>
 * 
 * @author 财务管理系统
 * @version 1.0
 * @since 2025-01-01
 * @see AccountSubject
 * @see AccountSubjectRepository
 */
@Service
public class AccountSubjectService {
    
    /** 会计科目数据访问对象 */
    @Autowired
    private AccountSubjectRepository accountSubjectRepository;

    /**
     * 查询所有会计科目
     * 
     * @return 会计科目列表
     */
    public List<AccountSubject> findAll() {
        return accountSubjectRepository.findAll();
    }

    /**
     * 根据ID查询会计科目
     * 
     * @param id 科目ID
     * @return 会计科目对象，不存在时返回null
     */
    public AccountSubject findById(Long id) {
        return accountSubjectRepository.findById(id).orElse(null);
    }

    /**
     * 根据科目编码查询会计科目
     * 
     * @param code 科目编码
     * @return 会计科目对象，不存在时返回null
     */
    public AccountSubject findByCode(String code) {
        return accountSubjectRepository.findByCode(code).orElse(null);
    }

    /**
     * 根据父级科目ID查询子科目列表
     * 用于构建科目树形结构
     * 
     * @param parentId 父级科目ID
     * @return 子科目列表
     */
    public List<AccountSubject> findByParentId(Long parentId) {
        return accountSubjectRepository.findByParentId(parentId);
    }

    /**
     * 根据科目类型查询科目列表
     * 
     * @param type 科目类型（资产、负债、所有者权益、成本、损益）
     * @return 该类型的科目列表
     */
    public List<AccountSubject> findByType(String type) {
        return accountSubjectRepository.findByType(type);
    }

    /**
     * 查询所有启用的会计科目
     * 
     * @return 启用状态的科目列表
     */
    public List<AccountSubject> findEnabled() {
        return accountSubjectRepository.findByEnabled(true);
    }

    /**
     * 保存会计科目
     * 支持新增和更新操作
     * 
     * @param accountSubject 会计科目对象
     * @return 保存后的科目对象
     */
    @Transactional
    public AccountSubject save(AccountSubject accountSubject) {
        return accountSubjectRepository.save(accountSubject);
    }

    /**
     * 删除会计科目
     * 注意：删除科目前需确保该科目未被使用
     * 
     * @param id 科目ID
     */
    @Transactional
    public void delete(Long id) {
        accountSubjectRepository.deleteById(id);
    }
}










