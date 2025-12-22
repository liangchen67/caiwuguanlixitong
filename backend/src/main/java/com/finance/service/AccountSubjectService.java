package com.finance.service;

import com.finance.entity.AccountSubject;
import com.finance.repository.AccountSubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class AccountSubjectService {
    
    @Autowired
    private AccountSubjectRepository accountSubjectRepository;

    public List<AccountSubject> findAll() {
        return accountSubjectRepository.findAll();
    }

    public AccountSubject findById(Long id) {
        return accountSubjectRepository.findById(id).orElse(null);
    }

    public AccountSubject findByCode(String code) {
        return accountSubjectRepository.findByCode(code).orElse(null);
    }

    public List<AccountSubject> findByParentId(Long parentId) {
        return accountSubjectRepository.findByParentId(parentId);
    }

    public List<AccountSubject> findByType(String type) {
        return accountSubjectRepository.findByType(type);
    }

    public List<AccountSubject> findEnabled() {
        return accountSubjectRepository.findByEnabled(true);
    }

    @Transactional
    public AccountSubject save(AccountSubject accountSubject) {
        return accountSubjectRepository.save(accountSubject);
    }

    @Transactional
    public void delete(Long id) {
        accountSubjectRepository.deleteById(id);
    }
}







