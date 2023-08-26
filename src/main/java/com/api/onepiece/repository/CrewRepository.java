package com.api.onepiece.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.onepiece.entity.Crew;

public interface CrewRepository extends JpaRepository<Crew, Long>{
    
    public boolean existsByName(String name);

}
