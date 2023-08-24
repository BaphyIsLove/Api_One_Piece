package com.api.onepiece.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.api.onepiece.entity.AnimeChapter;

public interface AnimeChapterRepository extends JpaRepository<AnimeChapter, Long> {

    @Query("SELECT MAX(SUBSTRING(a.uniqueKey, 4)) FROM AnimeChapter a WHERE SUBSTRING(a.uniqueKey, 1, 1) = :prefix")
    public Long findMaxNumberByPrefix(String prefix);

    public boolean existsByUniqueKey(String uniqueKey);

    public boolean existsByName(String name);

    public Optional<AnimeChapter> findByUniqueKey(String uniqueKey);

    public Optional<AnimeChapter> findByName(String name);

}
