package com.sourcery.defect_registration_system.office.service;

import com.sourcery.defect_registration_system.office.dto.CreateOfficeRequest;
import com.sourcery.defect_registration_system.office.dto.OfficeResponse;
import com.sourcery.defect_registration_system.office.entity.Office;
import com.sourcery.defect_registration_system.office.exceptions.OfficeNotFoundException;
import com.sourcery.defect_registration_system.office.repository.OfficeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OfficeService {

    private final OfficeRepository officeRepository;

    public OfficeResponse getOfficeById(UUID id) {

        Office office = officeRepository.getOfficeById(id)
                .orElseThrow(() -> new OfficeNotFoundException("Office with " + id + " id not found"));

        return OfficeResponse.from(office);
    }

    public List<OfficeResponse> getAllOffices() {

        return officeRepository.getAllOffices().stream()
                .map(OfficeResponse::from)
                .toList();
    }

    @Transactional
    public OfficeResponse createOffice(CreateOfficeRequest request) {

        Office office = Office.builder()
                .id(UUID.randomUUID())
                .title(request.title())
                .country(request.country())
                .dateCreated(OffsetDateTime.now())
                .build();

        officeRepository.insertOffice(office);

        return OfficeResponse.from(office);
    }
}
