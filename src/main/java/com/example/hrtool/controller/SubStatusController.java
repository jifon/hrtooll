package com.example.hrtool.controller;

import com.example.hrtool.dto.SubStatusOptionDto;
import com.example.hrtool.model.options.SubStatusOption;
import com.example.hrtool.repository.SubStatusOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/substatus-options")
@RequiredArgsConstructor
public class SubStatusController {

    private final SubStatusOptionRepository subRepo;
    private List<SubStatusOption> subStatusOptions;

    @GetMapping
    public List<SubStatusOptionDto> getAllSubStatusOptions() {
        List<SubStatusOption> subStatusOptions = subRepo.findAllByIsActiveTrue();

        return subStatusOptions.stream().map(subStatus -> {
            SubStatusOptionDto dto = new SubStatusOptionDto();
            dto.setId(subStatus.getId());
            dto.setValue(subStatus.getValue());
            dto.setActive(subStatus.isActive());
            return dto;
        }).toList();
    }

    @PostMapping
    public SubStatusOption create(@RequestBody SubStatusOption option) {
        return subRepo.save(option);
    }


    @PutMapping("/{id}")
    public SubStatusOption update(@PathVariable Long id, @RequestBody SubStatusOption updated) {
        SubStatusOption existing = subRepo.findById(id).orElseThrow();
        existing.setValue(updated.getValue());
        if (updated.getParentStatus() != null) {
            existing.setParentStatus(updated.getParentStatus());
        }
        return subRepo.save(existing);
    }

    @DeleteMapping("/{id}")
    public void deactivate(@PathVariable Long id) {
        SubStatusOption option = subRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Status not found"));

        option.setActive(false); // деактивируем
        subRepo.save(option);
    }
}
