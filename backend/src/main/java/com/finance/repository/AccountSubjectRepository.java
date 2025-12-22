package com.finance.repository;

import com.finance.entity.AccountSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountSubjectRepository extends JpaRepository<AccountSubject, Long> {
    Optional<AccountSubject> findByCode(String code);
    List<AccountSubject> findByParentId(Long parentId);
    List<AccountSubject> findByType(String type);
    List<AccountSubject> findByEnabled(Boolean enabled);
}







