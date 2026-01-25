package com.sourcery.defect_registration_system.office.service;

import com.sourcery.defect_registration_system.exception.UnauthorizedException;
import com.sourcery.defect_registration_system.issue.repository.IssueRepository;
import com.sourcery.defect_registration_system.office.dto.CreateOfficeRequest;
import com.sourcery.defect_registration_system.office.dto.OfficeResponse;
import com.sourcery.defect_registration_system.office.dto.UpsertOfficeRequest;
import com.sourcery.defect_registration_system.office.entity.Office;
import com.sourcery.defect_registration_system.office.enums.Country;
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
    private final IssueRepository issueRepository;

    public OfficeResponse getOfficeById(UUID id) {

        Office office = officeRepository.getOfficeById(id)
                .orElseThrow(() -> new OfficeNotFoundException("Office with " + id + " id not found"));

        return OfficeResponse.from(office);
    }

    public String getOfficeDisplayNameById(UUID id) {
        OfficeResponse office = getOfficeById(id);

        return office.title() + ", " + office.country();
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
                .country(Country.fromDisplayName(request.countryName()))
                .dateCreated(OffsetDateTime.now())
                .build();

        officeRepository.insertOffice(office);

        return OfficeResponse.from(office);
    }

    @Transactional
    public List<OfficeResponse> saveOffices(List<UpsertOfficeRequest> requests, OAuth2User principal) {

        UserDto currentUser = authService.getCurrentUserInfo(principal);

        if (!Role.ADMIN.equals(currentUser.role())) {
            throw new UnauthorizedException("Only admins can save offices.");
        }

        for (UpsertOfficeRequest request : requests) {
            if (request.id() == null) {
                Office office = Office.builder()
                        .id(UUID.randomUUID())
                        .title(request.title().trim())
                        .country(Country.fromDisplayName(request.countryName()))
                        .dateCreated(OffsetDateTime.now())
                        .build();

                officeRepository.insertOffice(office);
            } else {
                Office office = Office.builder()
                        .id(request.id())
                        .title(request.title().trim())
                        .country(Country.fromDisplayName(request.countryName()))
                        .build();

                int updated = officeRepository.updateOffice(office);
                if (updated == 0) {
                    throw new OfficeNotFoundException("Office with " + request.id() + " id not found");
                }
            }
        }
        return getAllOffices();
    }

    @Transactional
    public void softDeleteOffice(UUID id, OAuth2User principal) {

        UserDto currentUser = authService.getCurrentUserInfo(principal);

        if (!Role.ADMIN.equals(currentUser.role())) {
            throw new UnauthorizedException("Only admins can delete offices.");
        }

        if (officeRepository.getOfficeById(id).isEmpty()) {
            throw new OfficeNotFoundException("Office with " + id + " id not found");
        }

        issueRepository.softDeleteIssuesByOffice(id);

        int updated = officeRepository.markOfficeAsDeleted(id);

        if (updated == 0) {
            throw new OfficeNotFoundException("Office with " + id + " id not found when trying to delete");
        }
    }
}
