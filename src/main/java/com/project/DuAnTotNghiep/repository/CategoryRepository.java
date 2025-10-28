package com.project.DuAnTotNghiep.repository;

import com.project.DuAnTotNghiep.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByCode(String code);
    List<Category> findAllByDeleteFlagFalse();
    Category findFirstByOrderByIdDesc();
    Optional<Category> findByCode(String code);
    Page<Category> findAllByDeleteFlagFalse(Pageable pageable);
}