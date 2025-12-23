package com.finance.service;

import com.finance.entity.Company;
import com.finance.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * 企业信息业务逻辑服务类
 * 
 * <p>提供企业信息的业务处理方法，包括查询、新增、更新、删除等操作。
 * 该服务作为控制器层和数据访问层之间的中间层，处理业务逻辑。</p>
 * 
 * @author 财务管理系统
 * @version 1.0
 * @since 2025-01-01
 * @see Company
 * @see CompanyRepository
 */
@Service
public class CompanyService {
    
    /** 企业信息数据访问对象 */
    @Autowired
    private CompanyRepository companyRepository;

    /**
     * 查询所有企业信息
     * 
     * @return 企业信息列表
     */
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    /**
     * 根据ID查询企业信息
     * 
     * @param id 企业ID
     * @return 企业信息对象，不存在时返回null
     */
    public Company findById(Long id) {
        return companyRepository.findById(id).orElse(null);
    }

    /**
     * 保存企业信息
     * 支持新增和更新操作
     * 
     * @param company 企业信息对象
     * @return 保存后的企业信息对象
     */
    @Transactional
    public Company save(Company company) {
        return companyRepository.save(company);
    }

    /**
     * 删除企业信息
     * 
     * @param id 企业ID
     */
    @Transactional
    public void delete(Long id) {
        companyRepository.deleteById(id);
    }
}










