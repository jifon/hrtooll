package com.example.hrtool.controller;


import com.example.hrtool.dto.ProbezeitOptionDto;
import com.example.hrtool.model.options.ProbezeitOption;
import com.example.hrtool.repository.ProbezeitOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/probezeit-options")
@RequiredArgsConstructor
public class ProbezeitOptionController {

    private final ProbezeitOptionRepository repo;

    @GetMapping
    public List<ProbezeitOptionDto> getAll() {
        return repo.findAllByIsActiveTrue().stream().map(option -> {
            ProbezeitOptionDto dto = new ProbezeitOptionDto();
            dto.setId(option.getId());
            dto.setValue(option.getValue());
            dto.setActive(option.isActive());
            return dto;
        }).toList();
    }

    @PostMapping
    public ProbezeitOption create(@RequestBody ProbezeitOption option) {
        return repo.save(option);
    }

    @PutMapping("/{id}")
    public ProbezeitOption update(@PathVariable Long id, @RequestBody ProbezeitOption updated) {
        ProbezeitOption existing = repo.findById(id).orElseThrow();
        existing.setValue(updated.getValue());
        return repo.save(existing);
    }

    @DeleteMapping("/{id}")
    public void deactivate(@PathVariable Long id) {
        ProbezeitOption option = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Status not found"));

        option.setActive(false); // деактивируем
        repo.save(option);
    }
}
