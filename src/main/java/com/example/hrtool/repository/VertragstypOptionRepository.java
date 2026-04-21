package com.example.hrtool.repository;


import com.example.hrtool.model.options.VertragsartOption;
import com.example.hrtool.model.options.VertragstypOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VertragstypOptionRepository extends JpaRepository<VertragstypOption, Long> {
    List<VertragstypOption> findAllByIsActiveTrue();

}
