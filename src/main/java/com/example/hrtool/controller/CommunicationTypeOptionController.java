package com.example.hrtool.controller;

import com.example.hrtool.dto.CommunicationTypeOptionDto;
import com.example.hrtool.model.options.CommunicationTypeOption;
import com.example.hrtool.repository.CommunicationTypeOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/communicationtype-options")
@RequiredArgsConstructor
public class CommunicationTypeOptionController {

    private final CommunicationTypeOptionRepository repo;

    @GetMapping
    public List<CommunicationTypeOptionDto> getAll() {
        return repo.findAllByIsActiveTrue().stream().map(option -> {
            CommunicationTypeOptionDto dto = new CommunicationTypeOptionDto();
            dto.setId(option.getId());
            dto.setValue(option.getValue());
            dto.setActive(option.isActive());
            return dto;
        }).toList();
    }

    @PostMapping
    public CommunicationTypeOption create(@RequestBody CommunicationTypeOption option) {
        return repo.save(option);
    }

    @PutMapping("/{id}")
    public CommunicationTypeOption update(@PathVariable Long id, @RequestBody CommunicationTypeOption updated) {
        CommunicationTypeOption existing = repo.findById(id).orElseThrow();
        existing.setValue(updated.getValue());
        return repo.save(existing);
    }

    @DeleteMapping("/{id}")
    public void deactivate(@PathVariable Long id) {
        CommunicationTypeOption option = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Status not found"));

        option.setActive(false); // деактивируем
        repo.save(option);
    }
}
