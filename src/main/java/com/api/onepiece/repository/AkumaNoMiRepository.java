package com.api.onepiece.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.onepiece.entity.AkumaNoMi;

public interface AkumaNoMiRepository extends JpaRepository<AkumaNoMi, Long>{

    public boolean existsByName(String name);
    
}
