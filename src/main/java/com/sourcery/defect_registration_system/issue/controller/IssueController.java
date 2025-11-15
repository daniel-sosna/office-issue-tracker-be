package com.sourcery.defect_registration_system.issue.controller;

import com.sourcery.defect_registration_system.issue.dto.CreateIssueRequest;
import com.sourcery.defect_registration_system.issue.dto.IssueResponseDto;
import com.sourcery.defect_registration_system.issue.dto.PageResponseDto;
import com.sourcery.defect_registration_system.issue.service.IssueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/issues")
public class IssueController {
    private final IssueService issueService;

    @GetMapping
    public PageResponseDto<IssueResponseDto> getAllIssuesPaginated(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return issueService.getAllIssues(page, size);
    }

    @GetMapping("/{id}")
    public IssueResponseDto getIssueById(@PathVariable("id") UUID id) {
        return issueService.getIssueById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IssueResponseDto createIssue(@RequestBody @Valid CreateIssueRequest request){
        return issueService.createIssue(request);
    }
}
