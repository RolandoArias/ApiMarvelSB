package com.api.ApiMarvel.repository;

import com.api.ApiMarvel.entity.Comic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComicRepository extends JpaRepository<Comic, Long> {
    List<Comic> findByComicId(Long comicId);  // Usa el nombre del campo real en la entidad
}
