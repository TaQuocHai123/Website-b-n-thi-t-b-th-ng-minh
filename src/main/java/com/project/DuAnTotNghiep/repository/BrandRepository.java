package com.project.DuAnTotNghiep.repository;

import com.project.DuAnTotNghiep.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    boolean existsByCode(String code);
    List<Brand> findAllByDeleteFlagFalse();
    Brand findFirstByOrderByIdDesc();
    Page<Brand> findAllByDeleteFlagFalse(Pageable pageable);
    Optional<Brand> findByCode(String code);
}