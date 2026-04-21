package com.example.hrtool.controller;

import com.example.hrtool.dto.VertragstypOptionDto;
import com.example.hrtool.model.options.VertragstypOption;
import com.example.hrtool.model.options.WorkDaysOption;
import com.example.hrtool.repository.VertragstypOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vertragstyp-options")
@RequiredArgsConstructor
public class VertragsTypController {

    private final VertragstypOptionRepository vertragRepo;

    @GetMapping
    public List<VertragstypOptionDto> getAllVertragstypOptions() {
        List<VertragstypOption> options = vertragRepo.findAllByIsActiveTrue();

        return options.stream().map(option -> {
            VertragstypOptionDto dto = new VertragstypOptionDto();
            dto.setId(option.getId());
            dto.setValue(option.getValue());
            dto.setActive(option.isActive());
            return dto;
        }).toList();
    }

    @PostMapping
    public VertragstypOption create(@RequestBody VertragstypOption option) {
        return vertragRepo.save(option);
    }

    @PutMapping("/{id}")
    public VertragstypOption update(@PathVariable Long id, @RequestBody VertragstypOption updated) {
        VertragstypOption existing = vertragRepo.findById(id).orElseThrow();
        existing.setValue(updated.getValue());
        return vertragRepo.save(existing);
    }

    @DeleteMapping("/{id}")
    public void deactivate(@PathVariable Long id) {
        VertragstypOption option = vertragRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Status not found"));

        option.setActive(false); // деактивируем
        vertragRepo.save(option);
    }
}

