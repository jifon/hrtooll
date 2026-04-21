package com.example.hrtool.controller;

import com.example.hrtool.dto.WorkDaysOptionDto;
import com.example.hrtool.model.options.StatusOption;
import com.example.hrtool.model.options.WorkDaysOption;
import com.example.hrtool.repository.WorkDaysOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workdays-options")
@RequiredArgsConstructor
public class WorkDaysOptionController {

    private final WorkDaysOptionRepository repo;

    @GetMapping
    public List<WorkDaysOptionDto> getAll() {
        return repo.findAllByIsActiveTrue().stream().map(option -> {
            WorkDaysOptionDto dto = new WorkDaysOptionDto();
            dto.setId(option.getId());
            dto.setValue(option.getValue());
            dto.setActive(option.isActive());
            return dto;
        }).toList();
    }

    @PostMapping
    public WorkDaysOption create(@RequestBody WorkDaysOption option) {
        return repo.save(option);
    }

    @PutMapping("/{id}")
    public WorkDaysOption update(@PathVariable Long id, @RequestBody WorkDaysOption updated) {
        WorkDaysOption existing = repo.findById(id).orElseThrow();
        existing.setValue(updated.getValue());
        return repo.save(existing);
    }

    @DeleteMapping("/{id}")
    public void deactivate(@PathVariable Long id) {
        WorkDaysOption option = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Status not found"));

        option.setActive(false); // деактивируем
        repo.save(option);
    }
}
