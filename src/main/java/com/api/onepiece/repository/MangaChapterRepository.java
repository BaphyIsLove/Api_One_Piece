package com.api.onepiece.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.api.onepiece.entity.MangaChapter;

public interface MangaChapterRepository extends JpaRepository<MangaChapter, Long>{
    
    @Query("SELECT MAX(SUBSTRING(m.uniqueKey, 3)) FROM MangaChapter m WHERE SUBSTRING(m.uniqueKey, 1, 1) = :prefix")
    public Long findMaxNumberByPrefix(String prefix);

    public Optional<MangaChapter> findByName(String name) throws Exception;

    public Optional<MangaChapter> findByUniqueKey(String uniqueKey) throws Exception;

    public boolean existsByName(String name);

    public boolean existsByUniqueKey(String uniqueKey);

}
