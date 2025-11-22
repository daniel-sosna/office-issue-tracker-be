package com.sourcery.defect_registration_system.unit.issue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.sourcery.defect_registration_system.issue.dto.CreateIssueRequest;
import com.sourcery.defect_registration_system.issue.dto.IssueResponseDto;
import com.sourcery.defect_registration_system.issue.dto.PageResponseDto;
import com.sourcery.defect_registration_system.issue.entity.Issue;
import com.sourcery.defect_registration_system.issue.enums.IssueStatus;
import com.sourcery.defect_registration_system.issue.exceptions.IssueNotFoundException;
import com.sourcery.defect_registration_system.issue.repository.IssueRepository;
import com.sourcery.defect_registration_system.issue.service.IssueService;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sourcery.defect_registration_system.user.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.core.user.OAuth2User;

@ExtendWith(MockitoExtension.class)
public class IssueServiceTest {
    private Issue buildIssue(String summary, IssueStatus status) {
        return Issue.builder()
                .id(UUID.randomUUID())
                .summary(summary)
                .description("desc")
                .officeId(UUID.randomUUID())
                .status(status)
                .createdBy(UUID.randomUUID())
                .build();
    }

    @Mock
    private IssueRepository issueRepository;

    @Mock
    private AuthService  authService;

    @Mock
    private OAuth2User principal;

    @InjectMocks
    private IssueService issueService;


    @Test
    void shouldReturnFirstPageOfIssues() {
        Issue issue1 = buildIssue("summary1", IssueStatus.OPEN);
        Issue issue2 = buildIssue("summary2", IssueStatus.OPEN);
        int page = 1;
        int size = 5;
        when(issueRepository.getAllIssuesPaged(size, 0)).thenReturn(List.of(issue1, issue2));
        when(issueRepository.countAllIssues()).thenReturn(2L);

        PageResponseDto<IssueResponseDto> result = issueService.getAllIssues(page, size);

        assertThat(result).isNotNull();
        assertThat(result.content()).hasSize(2);

        IssueResponseDto first = result.content().getFirst();
        assertThat(first.summary()).isEqualTo("summary1");
        assertThat(first.status()).isEqualTo(IssueStatus.OPEN);

        assertThat(result.totalElements()).isEqualTo(2);
        assertThat(result.totalPages()).isEqualTo(1);
        assertThat(result.page()).isEqualTo(1);
        assertThat(result.size()).isEqualTo(5);
    }


    @Test
    void shouldReturnSecondPageOfIssues() {
        Issue issue3 = buildIssue("summary3", IssueStatus.RESOLVED);

        int page = 2;
        int size = 2;

        when(issueRepository.getAllIssuesPaged(size, 2)).thenReturn(List.of(issue3));
        when(issueRepository.countAllIssues()).thenReturn(3L);

        PageResponseDto<IssueResponseDto> result = issueService.getAllIssues(page, size);

        assertThat(result.content()).hasSize(1);
        assertThat(result.content().getFirst().summary()).isEqualTo("summary3");
        assertThat(result.totalPages()).isEqualTo(2);
        assertThat(result.page()).isEqualTo(2);
    }

    @Test
    void shouldReturnEmptyPageWhenNoIssuesExist() {
        int page = 1;
        int size = 5;

        when(issueRepository.getAllIssuesPaged(size, 0)).thenReturn(List.of());
        when(issueRepository.countAllIssues()).thenReturn(0L);

        PageResponseDto<IssueResponseDto> result = issueService.getAllIssues(page, size);

        assertThat(result.content()).isEmpty();
        assertThat(result.totalElements()).isEqualTo(0);
        assertThat(result.totalPages()).isEqualTo(0);
        assertThat(result.page()).isEqualTo(1);
        assertThat(result.size()).isEqualTo(5);
    }

    @Test
    void shouldReturnCorrectTotalPagesForMultiplePages() {
        Issue issue1 = buildIssue("summary1", IssueStatus.OPEN);
        Issue issue2 = buildIssue("summary2", IssueStatus.OPEN);
        Issue issue3 = buildIssue("summary3", IssueStatus.OPEN);

        int page = 1;
        int size = 2;

        when(issueRepository.getAllIssuesPaged(size, 0)).thenReturn(List.of(issue1, issue2));
        when(issueRepository.countAllIssues()).thenReturn(3L);

        PageResponseDto<IssueResponseDto> result = issueService.getAllIssues(page, size);

        assertThat(result.content()).hasSize(2);
        assertThat(result.totalElements()).isEqualTo(3);
        assertThat(result.totalPages()).isEqualTo(2);
    }

    @Test
    void createIssue_shouldSetDefaultOpenStatusAndReturnDto() {

        UUID officeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        CreateIssueRequest request = new CreateIssueRequest(
                "We’re out of bread kvass",
                "Critical resource unavailable. Productivity may be affected. Requesting resolution.",
                officeId
        );

        when(authService.getCurrentUserId(principal)).thenReturn(userId);

        doAnswer(invocation -> {
            Issue issue = invocation.getArgument(0);
            issue.setId(UUID.randomUUID());
            return null;
        }).when(issueRepository).insertIssue(any(Issue.class));

        IssueResponseDto result = issueService.createIssue(request, principal);

        assertThat(result.status()).isEqualTo(IssueStatus.OPEN);
        assertThat(result.officeId()).isEqualTo(officeId);
        verify(issueRepository).insertIssue(any(Issue.class));
    }

    @Test
    void getIssueById_whenFound_shouldReturnDto() {
        UUID issueId = UUID.randomUUID();
        UUID officeId = UUID.randomUUID();

        Issue issue = Issue.builder()
                .id(issueId)
                .summary("Test issue")
                .description("Test desc")
                .officeId(officeId)
                .status(IssueStatus.OPEN)
                .createdBy(UUID.randomUUID())
                .dateCreated(OffsetDateTime.now())
                .build();

        when(issueRepository.getIssueById(issueId)).thenReturn(Optional.of(issue));

        IssueResponseDto result = issueService.getIssueById(issueId);

        assertThat(result.id()).isEqualTo(issueId);
        assertThat(result.summary()).isEqualTo("Test issue");
        assertThat(result.status()).isEqualTo(IssueStatus.OPEN);
        assertThat(result.officeId()).isEqualTo(officeId);
    }

    @Test
    void getIssueById_whenNotFound_shouldThrowException() {
        UUID id = UUID.randomUUID();
        when(issueRepository.getIssueById(id)).thenReturn(Optional.empty());

        assertThrows(IssueNotFoundException.class, () ->
                issueService.getIssueById(id)
        );
    }
}

