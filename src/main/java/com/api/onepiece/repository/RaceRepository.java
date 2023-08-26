package com.api.onepiece.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.onepiece.entity.Race;

public interface RaceRepository extends JpaRepository<Race, Long>{

    public boolean existsByName(String name);

}
