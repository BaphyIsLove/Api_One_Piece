package com.api.onepiece.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.onepiece.entity.CharacterEntity;

public interface CharacterEntityRepository extends JpaRepository<CharacterEntity, Long>{
    
    public boolean existsByName(String name);    
    
    public Optional<CharacterEntity> findByName(String name);

}
