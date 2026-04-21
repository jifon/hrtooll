package com.example.hrtool.service.impl;


import com.example.hrtool.dto.HardwareDto;
import com.example.hrtool.dto.InventoryDto;
import com.example.hrtool.dto.SoftwareDto;
import com.example.hrtool.model.HardwareItem;
import com.example.hrtool.model.Inventory;
import com.example.hrtool.model.SoftwareItem;
import com.example.hrtool.repository.CommunicationTypeOptionRepository;
import com.example.hrtool.repository.EmployeeProfileRepository;
import com.example.hrtool.repository.InventoryRepository;
import com.example.hrtool.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository repository;
    private final EmployeeProfileRepository employeeProfileRepository;
    private final CommunicationTypeOptionRepository communicationTypeOptionRepository;

    @Override
    public List<InventoryDto> getAll() {
        return repository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public InventoryDto getById(Long id) {
        return repository.findById(id)
                .map(this::toDto)
                .orElse(null);
    }

    @Override
    public InventoryDto create(InventoryDto dto) {
        return toDto(repository.save(toEntity(dto)));
    }

    @Override
    public InventoryDto update(Long id, InventoryDto dto) {
        Inventory entity = toEntity(dto);
        entity.setId(id);
        return toDto(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public InventoryDto getByEmployeeId(Long employeeId) {
        return repository.findByEmployee_Id(employeeId)
                .map(this::toDto)
                .orElse(null);
    }

    @Override
    public InventoryDto updateByEmployeeId(Long employeeId, InventoryDto dto) {
        Inventory existing = repository.findByEmployee_Id(employeeId).orElse(null);

        Inventory entity = existing != null ? existing : new Inventory();

//        entity.setCommunication(dto.getCommunication());
        if (dto.getCommunicationTypeId() != null) {
            entity.setCommunicationType(
                    communicationTypeOptionRepository.findById(dto.getCommunicationTypeId())
                            .orElseThrow(() -> new RuntimeException("CommunicationType not found"))
            );
        }

        // set employee (нужно employeeRepository)
        entity.setEmployee(employeeProfileRepository.findById(employeeId).orElse(null));

        // hardware
        List<HardwareItem> hardwareItems = dto.getHardware().stream()
                .map(name -> {
                    HardwareItem item = new HardwareItem();
                    item.setName(name.getName());
                    item.setInventory(entity);
                    return item;
                }).toList();
        entity.setHardwareItems(hardwareItems);

        // software
        List<SoftwareItem> softwareItems = dto.getSoftware().stream()
                .map(name -> {
                    SoftwareItem item = new SoftwareItem();
                    item.setName(name.getName());
                    item.setInventory(entity);
                    item.setCategory(name.getCategory());
                    item.setVersion(name.getVersion());
                    item.setLicense(name.getLicense());
                    return item;
                }).toList();
        entity.setSoftwareItems(softwareItems);

        return toDto(repository.save(entity));
    }



    private InventoryDto toDto(Inventory entity) {
        InventoryDto dto = new InventoryDto();
        dto.setId(entity.getId());
        dto.setEmployeeId(entity.getEmployee().getId());
//        dto.setCommunication(entity.getCommunication());
        dto.setCommunicationTypeId(
                entity.getCommunicationType() != null ?
                        entity.getCommunicationType().getId() : null
        );



        dto.setHardware(entity.getHardwareItems().stream()
                .map(h -> {
                    HardwareDto hw = new HardwareDto();
                    hw.setName(h.getName());
                    hw.setInventoryNr(h.getInventoryNr());
                    return hw;
                }).toList());

        dto.setSoftware(entity.getSoftwareItems().stream()
                .map(s -> {
                    SoftwareDto sw = new SoftwareDto();
                    sw.setName(s.getName());
                    sw.setCategory(s.getCategory());
                    sw.setVersion(s.getVersion());
                    sw.setLicense(s.getLicense());
                    return sw;
                }).toList());

        return dto;
    }

    private Inventory toEntity(InventoryDto dto) {
        Inventory entity = new Inventory();
        entity.setId(dto.getId());
//        entity.setCommunication(dto.getCommunication());
        if (dto.getCommunicationTypeId() != null) {
            entity.setCommunicationType(
                    communicationTypeOptionRepository.findById(dto.getCommunicationTypeId())
                            .orElseThrow(() -> new RuntimeException("CommunicationType not found"))
            );
        }

        // 🔐 Привязка employeeId к employee entity
        entity.setEmployee(
                employeeProfileRepository.findById(dto.getEmployeeId())
                        .orElseThrow(() -> new RuntimeException("Employee not found"))
        );


        List<HardwareItem> hardwareItems = dto.getHardware().stream()
                .map(hw -> {
                    HardwareItem item = new HardwareItem();
                    item.setName(hw.getName());
                    item.setInventoryNr(hw.getInventoryNr());
                    item.setInventory(entity);
                    return item;
                }).toList();

        List<SoftwareItem> softwareItems = dto.getSoftware().stream()
                .map(name -> {
                    SoftwareItem item = new SoftwareItem();
                    item.setName(name.getName());
                    item.setInventory(entity);
                    item.setCategory(name.getCategory());
                    item.setVersion(name.getVersion());
                    item.setLicense(name.getLicense());
                    return item;
                }).toList();

        entity.setHardwareItems(hardwareItems);
        entity.setSoftwareItems(softwareItems);

        // employee надо подгрузить отдельно, например, через репозиторий
        // entity.setEmployee(employeeRepository.findById(dto.getEmployeeId()).orElse(null));

        return entity;
    }


}
