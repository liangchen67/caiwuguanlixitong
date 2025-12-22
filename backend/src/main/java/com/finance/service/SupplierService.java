package com.finance.service;

import com.finance.entity.Supplier;
import com.finance.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class SupplierService {
    
    @Autowired
    private SupplierRepository supplierRepository;

    public List<Supplier> findAll() {
        return supplierRepository.findAll();
    }

    public Supplier findById(Long id) {
        return supplierRepository.findById(id).orElse(null);
    }

    public List<Supplier> findByStatus(String status) {
        return supplierRepository.findByStatus(status);
    }

    public List<Supplier> searchByName(String name) {
        return supplierRepository.findByNameContaining(name);
    }

    @Transactional
    public Supplier save(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    @Transactional
    public void delete(Long id) {
        supplierRepository.deleteById(id);
    }
}







