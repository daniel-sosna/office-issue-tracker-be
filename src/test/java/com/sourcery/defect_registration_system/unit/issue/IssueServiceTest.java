package com.sourcery.defect_registration_system.unit.issue;

import com.sourcery.defect_registration_system.attachment.service.IssueAttachmentService;
import com.sourcery.defect_registration_system.exception.UnauthorizedException;
import com.sourcery.defect_registration_system.issue.dto.ChangeIssueStatusRequest;
import com.sourcery.defect_registration_system.issue.dto.CreateIssueRequest;
import com.sourcery.defect_registration_system.issue.dto.IssueDetailsResponseDto;
import com.sourcery.defect_registration_system.issue.dto.IssueResponseDto;
import com.sourcery.defect_registration_system.issue.dto.PageIssueResponseDto;
import com.sourcery.defect_registration_system.issue.dto.UpdateIssueRequest;
import com.sourcery.defect_registration_system.issue.entity.Issue;
import com.sourcery.defect_registration_system.issue.enums.IssueStatus;
import com.sourcery.defect_registration_system.issue.exceptions.IssueNotFoundException;
import com.sourcery.defect_registration_system.issue.repository.IssueRepository;
import com.sourcery.defect_registration_system.issue.service.IssueService;
import com.sourcery.defect_registration_system.office.service.OfficeService;
import com.sourcery.defect_registration_system.user.dto.UserDto;
import com.sourcery.defect_registration_system.user.enums.Role;
import com.sourcery.defect_registration_system.user.service.AuthService;
import com.sourcery.defect_registration_system.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IssueServiceTest {
    @Mock
    private IssueRepository issueRepository;
    @Mock
    private AuthService authService;
    @Mock
    private OAuth2User principal;
    @InjectMocks
    private IssueService issueService;
    @Mock
    private OfficeService officeService;
    @Mock
    private UserService userService;
    @Mock
    private IssueAttachmentService issueAttachmentService;

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

    @Test
    void getAllIssues_shouldReturnFirstPageOfIssues() {
        Issue issue1 = buildIssue("summary1", IssueStatus.OPEN);
        Issue issue2 = buildIssue("summary2", IssueStatus.OPEN);
        int page = 1;
        int size = 5;
        when(authService.getCurrentUserInfo(principal))
                .thenReturn(new UserDto(UUID.randomUUID(), "email", "name", Role.USER, null));
        when(issueRepository.getAllIssues(
                null,
                null,
                null,
                null,
                size,
                0,
                false
        )).thenReturn(List.of(issue1, issue2));

        PageIssueResponseDto<IssueResponseDto> result =
                issueService.getAllIssues(page, size, principal);

        assertThat(result.content()).hasSize(2);
        assertThat(result.content().getFirst().summary()).isEqualTo("summary1");
        assertThat(result.totalElements()).isEqualTo(2);
        assertThat(result.totalPages()).isEqualTo(1);
        assertThat(result.page()).isEqualTo(1);
    }


    @Test
    void getAllIssues_shouldReturnSecondPageOfIssues() {
        Issue issue3 = buildIssue("summary3", IssueStatus.RESOLVED);

        int page = 2;
        int size = 2;

        when(authService.getCurrentUserInfo(principal))
                .thenReturn(new UserDto(UUID.randomUUID(), "email", "name", Role.USER, null));

        when(issueRepository.getAllIssues(
                null,
                null,
                null,
                null,
                size,
                2,
                false
        )).thenReturn(List.of(issue3));

        PageIssueResponseDto<IssueResponseDto> result =
                issueService.getAllIssues(page, size, principal);

        assertThat(result.content()).hasSize(1);
        assertThat(result.content().getFirst().summary()).isEqualTo("summary3");
        assertThat(result.totalPages()).isEqualTo(2);
    }


    @Test
    void getAllIssues_shouldReturnEmptyPageWhenNoIssuesExist() {
        int page = 1;
        int size = 5;

        when(authService.getCurrentUserInfo(principal))
                .thenReturn(new UserDto(UUID.randomUUID(), "email", "name", Role.USER, null));

        when(issueRepository.getAllIssues(
                null,
                null,
                null,
                null,
                size,
                0,
                false
        )).thenReturn(List.of());

        PageIssueResponseDto<IssueResponseDto> result =
                issueService.getAllIssues(page, size, principal);

        assertThat(result.content()).isEmpty();
        assertThat(result.totalElements()).isEqualTo(0);
        assertThat(result.totalPages()).isEqualTo(0);
    }


    @Test
    void getAllIssues_shouldReturnCorrectTotalPagesForMultiplePages() {
        Issue issue1 = buildIssue("summary1", IssueStatus.OPEN);
        Issue issue2 = buildIssue("summary2", IssueStatus.OPEN);

        int page = 1;
        int size = 2;

        when(authService.getCurrentUserInfo(principal))
                .thenReturn(new UserDto(UUID.randomUUID(), "email", "name", Role.USER, null));

        when(issueRepository.getAllIssues(
                null,
                null,
                null,
                null,
                size,
                0,
                false
        )).thenReturn(List.of(issue1, issue2));

        PageIssueResponseDto<IssueResponseDto> result =
                issueService.getAllIssues(page, size, principal);

        assertThat(result.content()).hasSize(2);
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

        IssueResponseDto result = issueService.createIssue(request, null, principal);

        assertThat(result.summary()).isEqualTo("We’re out of bread kvass");
        assertThat(result.status()).isEqualTo(IssueStatus.OPEN);
        verify(issueRepository).insertIssue(any(Issue.class));
    }

    @Test
    void getIssueById_whenFound_shouldReturnDto() {
        UUID issueId = UUID.randomUUID();
        UUID officeId = UUID.randomUUID();
        OffsetDateTime dateCreated = OffsetDateTime.now();

        Issue issue = Issue.builder()
                .id(issueId)
                .summary("Test issue")
                .description("Test desc")
                .officeId(officeId)
                .status(IssueStatus.OPEN)
                .createdBy(UUID.randomUUID())
                .dateCreated(dateCreated)
                .build();

        when(issueRepository.getIssueById(issueId)).thenReturn(Optional.of(issue));

        IssueResponseDto result = issueService.getIssueById(issueId);

        assertThat(result.id()).isEqualTo(issueId);
        assertThat(result.summary()).isEqualTo("Test issue");
        assertThat(result.status()).isEqualTo(IssueStatus.OPEN);
        assertThat(result.dateCreated()).isEqualTo(dateCreated);
    }

    @Test
    void getIssueById_whenNotFound_shouldThrowException() {
        UUID id = UUID.randomUUID();
        when(issueRepository.getIssueById(id)).thenReturn(Optional.empty());

        assertThrows(IssueNotFoundException.class, () ->
                issueService.getIssueById(id)
        );
    }

    @Test
    void getIssueDetailsById_whenFound_shouldReturnDto() {
        UUID issueId = UUID.randomUUID();
        UUID officeId = UUID.randomUUID();
        UUID createdById = UUID.randomUUID();
        OffsetDateTime dateCreated = OffsetDateTime.now();

        Issue issue = Issue.builder()
                .id(issueId)
                .summary("Test issue")
                .description("Test desc")
                .officeId(officeId)
                .status(IssueStatus.OPEN)
                .createdBy(createdById)
                .dateCreated(dateCreated)
                .build();

        UserDto user = new UserDto(UUID.randomUUID(),"test@gmail.com", "User", Role.USER, "http://image.jpg");

        when(issueRepository.getIssueById(issueId)).thenReturn(Optional.of(issue));
        when(officeService.getOfficeDisplayNameById(officeId)).thenReturn("Vilnius, Lithuania");
        when(userService.getUserById(createdById)).thenReturn(user);
        when(issueAttachmentService.getAttachmentsByIssueId(issueId)).thenReturn(List.of());

        IssueDetailsResponseDto result = issueService.getIssueDetailsById(issueId);

        assertThat(result.issue().id()).isEqualTo(issueId);
        assertThat(result.issue().summary()).isEqualTo("Test issue");
        assertThat(result.issue().status()).isEqualTo(IssueStatus.OPEN);
        assertThat(result.issue().dateCreated()).isEqualTo(dateCreated);
        assertThat(result.officeName()).isEqualTo("Vilnius, Lithuania");
        assertThat(result.reportedBy()).isEqualTo("User");
        assertThat(result.reportedByAvatar()).isEqualTo("http://image.jpg");
    }

    @Test
    void getIssueDetailsById_whenNotFound_shouldThrowException() {
        UUID id = UUID.randomUUID();
        when(issueRepository.getIssueById(id)).thenReturn(Optional.empty());

        assertThrows(IssueNotFoundException.class, () ->
                issueService.getIssueDetailsById(id)
        );
    }

    @Test
    void updateIssue_whenUserIsOwner_shouldSucceed() {
        UUID issueId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        Issue oldIssue = buildIssue("Old summary", IssueStatus.OPEN);
        oldIssue.setId(issueId);
        oldIssue.setCreatedBy(ownerId);

        Issue updatedIssue = buildIssue("New summary", IssueStatus.OPEN);
        updatedIssue.setId(issueId);
        updatedIssue.setCreatedBy(ownerId);

        when(authService.getCurrentUserId(principal)).thenReturn(ownerId);
        when(issueRepository.getIssueById(issueId))
                .thenReturn(Optional.of(oldIssue))
                .thenReturn(Optional.of(updatedIssue));

        UpdateIssueRequest request = new UpdateIssueRequest(
                "New summary", "New desc", UUID.randomUUID());

        when(issueRepository.updateIssue(issueId, request))
                .thenReturn(1);

        IssueResponseDto result = issueService.updateIssue(issueId, request, List.of(), List.of(), principal);

        verify(issueRepository).updateIssue(issueId, request);
        assertThat(result.summary()).isEqualTo("New summary");
    }

    @Test
    void updateIssue_whenUserIsNotOwner_shouldThrowUnauthorized() {
        UUID issueId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();

        Issue existing = buildIssue("Test", IssueStatus.OPEN);
        existing.setId(issueId);
        existing.setCreatedBy(ownerId);

        when(authService.getCurrentUserId(principal)).thenReturn(otherUserId);
        when(issueRepository.getIssueById(issueId)).thenReturn(Optional.of(existing));

        UpdateIssueRequest request = new UpdateIssueRequest("x", "y", UUID.randomUUID());

        assertThrows(UnauthorizedException.class, () ->
                issueService.updateIssue(issueId, request, List.of(), List.of(), principal));

        verify(issueRepository, never()).updateIssue(any(), any());
    }

    @Test
    void updateIssueStatus_whenAdmin_shouldUpdateStatus() {
        UUID issueId = UUID.randomUUID();

        Issue oldIssue = buildIssue("Test", IssueStatus.OPEN);
        oldIssue.setId(issueId);

        Issue updatedIssue = buildIssue("Test", IssueStatus.RESOLVED);
        updatedIssue.setId(issueId);

        UserDto admin = new UserDto(UUID.randomUUID(),"admin@x.lt", "Admin", Role.ADMIN, null);

        when(authService.getCurrentUserInfo(principal)).thenReturn(admin);
        when(issueRepository.getIssueById(issueId))
                .thenReturn(Optional.of(oldIssue))
                .thenReturn(Optional.of(updatedIssue));

        when(issueRepository.updateIssueStatus(issueId, IssueStatus.RESOLVED))
                .thenReturn(1);

        ChangeIssueStatusRequest request = new ChangeIssueStatusRequest(IssueStatus.RESOLVED);

        IssueResponseDto result = issueService.updateIssueStatus(issueId, request, principal);

        verify(issueRepository).updateIssueStatus(issueId, IssueStatus.RESOLVED);
        assertThat(result.status()).isEqualTo(IssueStatus.RESOLVED);
    }

    @Test
    void updateIssueStatus_whenNotAdmin_shouldThrowAccessDenied() {
        UUID issueId = UUID.randomUUID();
        UserDto regularUser = new UserDto(UUID.randomUUID(),"user@x.lt", "User", Role.USER, null);

        when(authService.getCurrentUserInfo(principal)).thenReturn(regularUser);

        ChangeIssueStatusRequest request = new ChangeIssueStatusRequest(IssueStatus.CLOSED);

        assertThrows(AccessDeniedException.class, () ->
                issueService.updateIssueStatus(issueId, request, principal));

        verify(issueRepository, never()).updateIssueStatus(any(), any());
    }

    @Test
    void softDeleteIssue_whenUserIsOwner_shouldSucceed() {
        UUID issueId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        Issue issue = new Issue();
        issue.setId(issueId);
        issue.setCreatedBy(ownerId);

        when(authService.getCurrentUserId(principal)).thenReturn(ownerId);
        when(issueRepository.getIssueById(issueId)).thenReturn(Optional.of(issue));

        issueService.softDeleteIssue(issueId, principal);

        verify(issueRepository).updateIssueStatus(issueId, IssueStatus.DELETED);
    }

    @Test
    void softDeleteIssue_whenUserIsAdmin_shouldSucceed() {
        UUID issueId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        Issue issue = new Issue();
        issue.setId(issueId);
        issue.setCreatedBy(ownerId);

        UserDto admin = new UserDto(UUID.randomUUID(),"admin@mail.com", "Admin", Role.ADMIN, null);

        when(authService.getCurrentUserId(principal)).thenReturn(UUID.randomUUID());
        when(authService.getCurrentUserInfo(principal)).thenReturn(admin);
        when(issueRepository.getIssueById(issueId)).thenReturn(Optional.of(issue));

        issueService.softDeleteIssue(issueId, principal);

        verify(issueRepository).updateIssueStatus(issueId, IssueStatus.DELETED);
    }

    @Test
    void softDeleteIssue_whenUserNotOwnerAndNotAdmin_shouldThrowException() {
        UUID issueId = UUID.randomUUID();

        UUID ownerId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();

        Issue issue = new Issue();
        issue.setId(issueId);
        issue.setCreatedBy(ownerId);

        UserDto regular = new UserDto(UUID.randomUUID(),"user@mail.com", "User", Role.USER, null);

        when(authService.getCurrentUserId(principal)).thenReturn(otherUserId);
        when(authService.getCurrentUserInfo(principal)).thenReturn(regular);
        when(issueRepository.getIssueById(issueId)).thenReturn(Optional.of(issue));

        assertThrows(UnauthorizedException.class,
                () -> issueService.softDeleteIssue(issueId, principal));

        verify(issueRepository, never()).updateIssueStatus(any(), any());
    }

}
