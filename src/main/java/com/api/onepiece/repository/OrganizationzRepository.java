package com.api.onepiece.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.onepiece.entity.Organizationz;

public interface OrganizationzRepository extends JpaRepository<Organizationz, Long>{
    
    public boolean existsByName(String name);

}
