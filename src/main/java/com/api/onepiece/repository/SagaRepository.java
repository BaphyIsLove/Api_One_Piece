package com.api.onepiece.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import com.api.onepiece.entity.Saga;

public interface SagaRepository extends JpaRepository<Saga, Long>{
    
    public boolean existsByName(String name);
    
}
