package com.sourcery.defect_registration_system.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.sourcery.defect_registration_system.issue.dto.IssueResponseDto;
import com.sourcery.defect_registration_system.issue.dto.PageResponseDto;
import com.sourcery.defect_registration_system.issue.entity.Issue;
import com.sourcery.defect_registration_system.issue.enums.IssueStatus;
import com.sourcery.defect_registration_system.issue.repository.IssueRepository;
import com.sourcery.defect_registration_system.issue.service.IssueService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class IssueServiceTest {
  private Issue buildIssue(String summary, IssueStatus status) {
    return Issue.builder()
        .id(UUID.randomUUID())
        .summary(summary)
        .description("desc")
        .office("office")
        .status(status)
        .createdBy(UUID.randomUUID())
        .build();
  }
  @Mock
  private IssueRepository issueRepository;

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
    assertThat(result.getContent()).hasSize(2);

    IssueResponseDto first = result.getContent().getFirst();
    assertThat(first.getSummary()).isEqualTo("summary1");
    assertThat(first.getStatus()).isEqualTo(IssueStatus.OPEN);

    assertThat(result.getTotalElements()).isEqualTo(2);
    assertThat(result.getTotalPages()).isEqualTo(1);
    assertThat(result.getPage()).isEqualTo(1);
    assertThat(result.getSize()).isEqualTo(5);
  }


  @Test
  void shouldReturnSecondPageOfIssues() {
    Issue issue3 = buildIssue("summary3", IssueStatus.RESOLVED);

    int page = 2;
    int size = 2;

    when(issueRepository.getAllIssuesPaged(size, 2)).thenReturn(List.of(issue3));
    when(issueRepository.countAllIssues()).thenReturn(3L);

    PageResponseDto<IssueResponseDto> result = issueService.getAllIssues(page, size);

    assertThat(result.getContent()).hasSize(1);
    assertThat(result.getContent().getFirst().getSummary()).isEqualTo("summary3");
    assertThat(result.getTotalPages()).isEqualTo(2);
    assertThat(result.getPage()).isEqualTo(2);
  }

  @Test
  void shouldReturnEmptyPageWhenNoIssuesExist() {
    int page = 1;
    int size = 5;

    when(issueRepository.getAllIssuesPaged(size, 0)).thenReturn(List.of());
    when(issueRepository.countAllIssues()).thenReturn(0L);

    PageResponseDto<IssueResponseDto> result = issueService.getAllIssues(page, size);

    assertThat(result.getContent()).isEmpty();
    assertThat(result.getTotalElements()).isEqualTo(0);
    assertThat(result.getTotalPages()).isEqualTo(0);
    assertThat(result.getPage()).isEqualTo(1);
    assertThat(result.getSize()).isEqualTo(5);
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

    assertThat(result.getContent()).hasSize(2);
    assertThat(result.getTotalElements()).isEqualTo(3);
    assertThat(result.getTotalPages()).isEqualTo(2);
  }
}

