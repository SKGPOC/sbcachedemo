package com.oup.cachedemo.sbcachedemo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.oup.cachedemo.sbcachedemo.bo.EmployeeBO;
import com.oup.cachedemo.sbcachedemo.dao.RocksDBRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service(value = "samplesrv")
@Slf4j
public class SampleService {

    @Autowired
    @Qualifier(value = "rocksdbrepo")
    RocksDBRepository repo;

    @CacheEvict(cacheNames = { "cache_EMPLOYEES" }, allEntries = true)
    public void add(EmployeeBO employee) {
        log.info("Adding employee");
        repo.save(employee.getUserId(), employee);

    }
    @Caching(evict = {
        @CacheEvict("cache_EMPLOYEES"),
        @CacheEvict(value = "cache_EMPLOYEE_BY_ID", key = "#employee.userId")
    })
    public void update(EmployeeBO employee) {
        log.info("Adding employee");
        repo.save(employee.getUserId(), employee);
    }

    @Cacheable(value = "cache_EMPLOYEE_BY_ID", key = "#id")
    public EmployeeBO getEmployee(Integer id) {
        
        return !repo.find(id).isPresent() ? null : (EmployeeBO) repo.find(id).get();

    }

    @Cacheable(value = "cache_EMPLOYEES")
    public List<EmployeeBO> getAll() {
        log.info("Getting employee");       
        return repo.findAll();

    }

}
