package com.sourcery.defect_registration_system.unit.office;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.sourcery.defect_registration_system.office.dto.CreateOfficeRequest;
import com.sourcery.defect_registration_system.office.dto.OfficeResponse;
import com.sourcery.defect_registration_system.office.entity.Office;
import com.sourcery.defect_registration_system.office.enums.Country;
import com.sourcery.defect_registration_system.office.exceptions.OfficeNotFoundException;
import com.sourcery.defect_registration_system.office.repository.OfficeRepository;
import com.sourcery.defect_registration_system.office.service.OfficeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OfficeServiceTest {

    @Mock
    private OfficeRepository officeRepository;

    @InjectMocks
    private OfficeService officeService;

    private Office buildOffice(String title, Country country) {
        return Office.builder()
                .id(UUID.randomUUID())
                .title(title)
                .country(country)
                .dateCreated(OffsetDateTime.now())
                .build();
    }

    @Test
    void getOfficeById_whenOfficeExists_shouldReturnResponse() {
        UUID id = UUID.randomUUID();
        Office office = buildOffice("Vilnius Office", Country.LITHUANIA);
        office.setId(id);

        when(officeRepository.getOfficeById(id)).thenReturn(Optional.of(office));

        OfficeResponse response = officeService.getOfficeById(id);

        assertThat(response.id()).isEqualTo(id);
        assertThat(response.title()).isEqualTo("Vilnius Office");
        assertThat(response.country()).isEqualTo(Country.LITHUANIA);
    }

    @Test
    void getOfficeById_whenOfficeNotFound_shouldThrowException() {
        UUID id = UUID.randomUUID();

        when(officeRepository.getOfficeById(id)).thenReturn(Optional.empty());

        assertThrows(OfficeNotFoundException.class,
                () -> officeService.getOfficeById(id));
    }

    @Test
    void getAllOffices_whenOfficesExist_shouldReturnList() {
        Office office1 = buildOffice("Vilnius HQ", Country.LITHUANIA);
        Office office2 = buildOffice("Kaunas Office", Country.LITHUANIA);

        when(officeRepository.getAllOffices()).thenReturn(List.of(office1, office2));

        List<OfficeResponse> result = officeService.getAllOffices();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).title()).isEqualTo("Vilnius HQ");
        assertThat(result.get(1).country()).isEqualTo(Country.LITHUANIA);
    }

    @Test
    void getAllOffices_whenNoOfficesExist_shouldReturnEmptyList() {
        when(officeRepository.getAllOffices()).thenReturn(List.of());

        List<OfficeResponse> result = officeService.getAllOffices();

        assertThat(result).isEmpty();
    }

    @Test
    void createOffice_shouldCreateOfficeAndReturnResponse() {
        CreateOfficeRequest request = new CreateOfficeRequest(
                "Riga Office",
                Country.LATVIA
        );

        doAnswer(invocation -> {
            Office office = invocation.getArgument(0);
            assertThat(office.getId()).isNotNull();
            return null;
        }).when(officeRepository).insertOffice(any(Office.class));

        OfficeResponse response = officeService.createOffice(request);

        assertThat(response.id()).isNotNull();
        assertThat(response.title()).isEqualTo("Riga Office");
        assertThat(response.country()).isEqualTo(Country.LATVIA);

        verify(officeRepository).insertOffice(any(Office.class));
    }
}

