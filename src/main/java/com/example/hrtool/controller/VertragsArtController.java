package com.example.hrtool.controller;

import com.example.hrtool.dto.VertragsartOptionDto;
import com.example.hrtool.repository.VertragsartOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import com.example.hrtool.model.options.VertragsartOption;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/vertragsart-options")
@RequiredArgsConstructor
public class VertragsArtController {

    private final VertragsartOptionRepository repo;

    @GetMapping
    public List<VertragsartOptionDto> getAll() {
        return repo.findAllByIsActiveTrue().stream().map(option -> {
            VertragsartOptionDto dto = new VertragsartOptionDto();
            dto.setId(option.getId());
            dto.setValue(option.getValue());
            dto.setActive(option.isActive());
            return dto;
        }).toList();
    }

    @PostMapping
    public VertragsartOption create(@RequestBody VertragsartOption option) {
        return repo.save(option);
    }

    @PutMapping("/{id}")
    public VertragsartOption update(@PathVariable Long id, @RequestBody VertragsartOption updated) {
        VertragsartOption existing = repo.findById(id).orElseThrow();
        existing.setValue(updated.getValue());
        return repo.save(existing);
    }

    @DeleteMapping("/{id}")
    public void deactivate(@PathVariable Long id) {
        VertragsartOption option = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Status not found"));

        option.setActive(false); // деактивируем
        repo.save(option);
    }
}


