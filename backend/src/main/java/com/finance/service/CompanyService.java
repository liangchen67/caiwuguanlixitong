package com.finance.service;

import com.finance.entity.Company;
import com.finance.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CompanyService {
    
    @Autowired
    private CompanyRepository companyRepository;

    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    public Company findById(Long id) {
        return companyRepository.findById(id).orElse(null);
    }

    @Transactional
    public Company save(Company company) {
        return companyRepository.save(company);
    }

    @Transactional
    public void delete(Long id) {
        companyRepository.deleteById(id);
    }
}







