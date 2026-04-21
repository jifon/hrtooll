package com.example.hrtool.controller;

import com.example.hrtool.dto.StatusOptionDto;
import com.example.hrtool.dto.SubStatusOptionDto;
import com.example.hrtool.model.options.StatusOption;
import com.example.hrtool.repository.StatusOptionRepository;
import com.example.hrtool.repository.SubStatusOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/status-options")
@RequiredArgsConstructor
public class StatusController {

    private final StatusOptionRepository statusRepo;
    private final SubStatusOptionRepository subRepo;

    @GetMapping
    public List<StatusOptionDto> getAllStatusOptions() {
        List<StatusOption> statusOptions = statusRepo.findAllByIsActiveTrue();

        return statusOptions.stream().map(status -> {
            StatusOptionDto dto = new StatusOptionDto();
            dto.setId(status.getId());
            dto.setValue(status.getValue());
            dto.setActive(status.isActive());

            List<SubStatusOptionDto> subDtos = subRepo.findAllByParentStatusAndIsActiveTrue(status).stream()
                    .map(sub -> {
                        SubStatusOptionDto subDto = new SubStatusOptionDto();
                        subDto.setId(sub.getId());
                        subDto.setValue(sub.getValue());
                        subDto.setActive(sub.isActive());
                        return subDto;
                    })
                    .collect(Collectors.toList());

//            List<SubStatusOptionDto> subDtos = status.getSubStatuses().stream()
//                    .map(sub -> {
//                        SubStatusOptionDto subDto = new SubStatusOptionDto();
//                        subDto.setId(sub.getId());
//                        subDto.setValue(sub.getValue());
//                        subDto.setActive(sub.isActive());
//                        return subDto;
//                    })
//                    .collect(Collectors.toList());

            dto.setSubStatuses(subDtos);
            return dto;
        }).toList();
    }


    @PostMapping
    public StatusOption create(@RequestBody StatusOption option) {
        return statusRepo.save(option);
    }

    @PutMapping("/{id}")
    public StatusOption update(@PathVariable Long id, @RequestBody StatusOption updated) {
        StatusOption option = statusRepo.findById(id).orElseThrow();
        option.setValue(updated.getValue());
        return statusRepo.save(option);
    }

    @DeleteMapping("/{id}")
    public void deactivate(@PathVariable Long id) {
        StatusOption status = statusRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Status not found"));

        status.setActive(false); // деактивируем
        statusRepo.save(status);
    }

}

