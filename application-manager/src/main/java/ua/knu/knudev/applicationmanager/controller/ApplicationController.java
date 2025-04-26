package ua.knu.knudev.applicationmanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.knu.knudev.applicationmanager.dto.ApplicationDto;
import ua.knu.knudev.applicationmanager.service.ApplicationService;

import java.util.UUID;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<ApplicationDto> create(@RequestBody ApplicationDto dto) {
        return ResponseEntity.ok(applicationService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(applicationService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationDto> update(@PathVariable UUID id, @RequestBody ApplicationDto dto) {
        return ResponseEntity.ok(applicationService.update(id, dto));
    }
}