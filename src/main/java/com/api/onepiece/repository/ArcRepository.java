package com.api.onepiece.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.api.onepiece.entity.Arc;

public interface ArcRepository extends JpaRepository<Arc, Long>{

    @Query("SELECT MAX(SUBSTRING(a.uniqueKey, 4)) FROM Arc a WHERE SUBSTRING(a.uniqueKey, 1, 1) = :prefix")
    Long findMaxNumberByPrefix(String prefix);
    
    public boolean existsByName(String name);

    public boolean existsByUniqueKey(String uniqueKey);

    public Optional<Arc> findByUniqueKey(String uniqueKey);

    public Optional<Arc> findByName(String name);
}
