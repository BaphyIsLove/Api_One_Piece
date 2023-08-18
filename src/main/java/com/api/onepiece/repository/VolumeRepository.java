package com.api.onepiece.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.api.onepiece.entity.Volume;

public interface VolumeRepository extends JpaRepository<Volume, Long>{
    
    @Query("SELECT MAX(SUBSTRING(v.uniqueKey, 4)) FROM Volume v WHERE SUBSTRING(v.uniqueKey, 1, 1) = :prefix")
    Long findMaxNumberByPrefix(String prefix);

    public boolean existsByUniqueKey(String uniqueKey);

    public boolean existsByName(String name);

    public Optional<Volume> findByName(String name);

    public Optional<Volume> findByUniqueKey(String uniqueKey);

}
