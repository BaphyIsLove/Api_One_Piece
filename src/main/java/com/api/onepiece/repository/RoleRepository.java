package com.api.onepiece.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.onepiece.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{
    
    public boolean existsByName(String name);

}
