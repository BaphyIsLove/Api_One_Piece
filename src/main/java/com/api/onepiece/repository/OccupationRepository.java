package com.api.onepiece.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.onepiece.entity.Occupation;

public interface OccupationRepository extends JpaRepository<Occupation, Long>{
    
    public boolean existsByName(String name);

}
