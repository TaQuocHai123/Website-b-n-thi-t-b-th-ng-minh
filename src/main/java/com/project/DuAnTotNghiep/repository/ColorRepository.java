package com.project.DuAnTotNghiep.repository;

import com.project.DuAnTotNghiep.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ColorRepository extends JpaRepository<Color, Long> {
    List<Color> findAllByDeleteFlagFalse();
    boolean existsByCode(String code);
    Color findFirstByOrderByIdDesc();
}