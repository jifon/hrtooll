package com.example.hrtool.repository;


import com.example.hrtool.model.*;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LocationRepository extends JpaRepository<Location, Long> {
    Location findLocationById(Long id);
}
