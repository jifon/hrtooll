package com.example.hrtool.controller;

import com.example.hrtool.model.options.DegreeOption;
import com.example.hrtool.repository.DegreeOptionRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/degree-options")
public class DegreeController {

    private final DegreeOptionRepository repository;

    public DegreeController(DegreeOptionRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public DegreeOption create(@RequestBody DegreeOption option) {
        return repository.save(option);
    }

    @GetMapping
    public List<DegreeOption> getAll() {
        return repository.findAllByIsActiveTrue();
    }

    @DeleteMapping("/{id}")
    public void deactivate(@PathVariable Long id) {
        DegreeOption option = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Status not found"));

        option.setActive(false); // деактивируем
        repository.save(option);
    }

    @PutMapping("/{id}")
    public DegreeOption update(@PathVariable Long id, @RequestBody DegreeOption updated) {
        return repository.findById(id).map(option -> {
            option.setValue(updated.getValue());
            return repository.save(option);
        }).orElseThrow(() -> new RuntimeException("Degree not found"));
    }
}

