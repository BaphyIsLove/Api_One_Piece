package com.api.onepiece.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.onepiece.entity.Saga;

public interface SagaRepository extends JpaRepository<Saga, Long>{
    
    @Query("SELECT MAX(SUBSTRING(s.uniqueKey, 4)) FROM Saga s WHERE SUBSTRING(s.uniqueKey, 1, 1) = :prefix")
    Long findMaxNumberByPrefix(@Param("prefix") String prefix);

    public boolean existsByName(String name);

    public Optional<Saga> findByUniqueKey(String uniqueKey);

    public Optional<Saga> findByName(String name);
    
}
