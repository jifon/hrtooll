package com.example.hrtool.controller;


import com.example.hrtool.dto.*;
import com.example.hrtool.model.AccessData;
import com.example.hrtool.payload.BaseResponse;
import com.example.hrtool.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "AccessData", description = "Zugriffsverwaltung für Mitarbeiter")
@RestController
@RequestMapping("/api/access")
@RequiredArgsConstructor
public class AccessDataController {

    private final AccessDataService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Hole alle Zugriffsdaten")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
    public BaseResponse<List<AccessDataDto>> getAll() {
        return new BaseResponse<>(true, "OK", service.getAll());
    }

    @Operation(summary = "Zugriff nach ID abrufen")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Zugriff gefunden"),
            @ApiResponse(responseCode = "404", description = "Nicht gefunden")
    })
    @GetMapping("/{id}")//emplID
    public BaseResponse<AccessDataDto> get(@PathVariable Long id) {
        return new BaseResponse<>(true, "OK", service.findByEmployeeId(id));
    }

    @Operation(summary = "Neuen Zugriff erstellen")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Erstellt"),
            @ApiResponse(responseCode = "400", description = "Ungültige Anfrage")
    })
    @PostMapping
    public BaseResponse<AccessDataDto> create(@RequestBody AccessDataDto dto) {
        return new BaseResponse<>(true, "Created", service.create(dto));
    }

    @Operation(summary = "Zugriff aktualisieren")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aktualisiert"),
            @ApiResponse(responseCode = "404", description = "Nicht gefunden")
    })
    @PutMapping("/{id}")
    public BaseResponse<AccessDataDto> update(@PathVariable Long id, @RequestBody AccessDataDto dto) {
        return new BaseResponse<>(true, "Updated", service.update(id, dto));
    }

    @Operation(summary = "Zugriff löschen")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Gelöscht"),
            @ApiResponse(responseCode = "404", description = "Nicht gefunden")
    })
    @DeleteMapping("/{id}")
    public BaseResponse<String> delete(@PathVariable Long id) {
        service.delete(id);
        return new BaseResponse<>(true, "Deleted", null);
    }
}

