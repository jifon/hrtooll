package com.example.hrtool.controller;

import com.example.hrtool.model.options.GenderOption;
import com.example.hrtool.repository.GenderOptionRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gender-options")
//@RequiredArgsConstructor
public class GenderController {

    private final GenderOptionRepository repository;

    public GenderController(GenderOptionRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public GenderOption create(@RequestBody GenderOption option) {
        return repository.save(option);
    }

    @GetMapping
    public List<GenderOption> getAll() {
        return repository.findAllByIsActiveTrue();
    }

    @DeleteMapping("/{id}")
    public void deactivate(@PathVariable Long id) {
        GenderOption option = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Status not found"));

        option.setActive(false); // деактивируем
        repository.save(option);
    }

    @PutMapping("/{id}")
    public GenderOption update(@PathVariable Long id, @RequestBody GenderOption updated) {
        return repository.findById(id).map(option -> {
            option.setValue(updated.getValue());
            return repository.save(option);
        }).orElseThrow(() -> new RuntimeException("Gender not found"));
    }
}
