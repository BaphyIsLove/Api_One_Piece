package com.api.onepiece.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.onepiece.entity.Ubication;

public interface UbicationRepository extends JpaRepository<Ubication, Long>{
    
    public boolean existsByName(String name);

}
