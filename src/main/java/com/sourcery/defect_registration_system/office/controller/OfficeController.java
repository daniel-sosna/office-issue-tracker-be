package com.sourcery.defect_registration_system.office.controller;

import com.sourcery.defect_registration_system.office.dto.CreateOfficeRequest;
import com.sourcery.defect_registration_system.office.dto.OfficeResponse;
import com.sourcery.defect_registration_system.office.service.OfficeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/offices")
public class OfficeController {

    private final OfficeService officeService;

    @GetMapping("/{id}")
    public OfficeResponse getOfficeById(@PathVariable("id") UUID id) {
        return officeService.getOfficeById(id);
    }

    @GetMapping
    public List<OfficeResponse> getAllOffices() {
        return officeService.getAllOffices();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OfficeResponse createOffice(@RequestBody @Valid CreateOfficeRequest request) {
        return officeService.createOffice(request);
    }
}
