package com.sourcery.defect_registration_system.office.service;

import com.sourcery.defect_registration_system.exception.UnauthorizedException;
import com.sourcery.defect_registration_system.office.dto.CreateOfficeRequest;
import com.sourcery.defect_registration_system.office.dto.OfficeResponse;
import com.sourcery.defect_registration_system.office.entity.Office;
import com.sourcery.defect_registration_system.office.exceptions.OfficeNotFoundException;
import com.sourcery.defect_registration_system.office.repository.OfficeRepository;
import com.sourcery.defect_registration_system.user.dto.UserDto;
import com.sourcery.defect_registration_system.user.enums.Role;
import com.sourcery.defect_registration_system.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OfficeService {

    private final OfficeRepository officeRepository;
    private final AuthService authService;

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
    public OfficeResponse createOffice(CreateOfficeRequest request, OAuth2User principal) {

        UserDto currentUser = authService.getCurrentUserInfo(principal);

        if (!Role.ADMIN.equals(currentUser.role())) {
            throw new UnauthorizedException("Only admins can create offices.");
        }

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
