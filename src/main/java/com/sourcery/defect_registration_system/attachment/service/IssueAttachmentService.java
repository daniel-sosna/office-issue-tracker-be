package com.sourcery.defect_registration_system.attachment.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.sourcery.defect_registration_system.attachment.dto.IssueAttachmentResponse;
import com.sourcery.defect_registration_system.attachment.entity.IssueAttachment;
import com.sourcery.defect_registration_system.attachment.exceptions.InvalidFileException;
import com.sourcery.defect_registration_system.attachment.repository.IssueAttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IssueAttachmentService {

    private static final int MAX_FILES = 10;
    private static final long MAX_SIZE_BYTES = 5 * 1024 * 1024;
    private static final Set<String> ALLOWED_TYPES = Set.of("image/png", "image/jpeg", "image/jpg", "image/webp");
    private final IssueAttachmentRepository issueAttachmentRepository;
    private final Cloudinary cloudinary;

    @Transactional
    public List<IssueAttachmentResponse> uploadAttachments(UUID issueId, UUID uploadedBy, List<MultipartFile> files) {

        if (files == null || files.isEmpty()) {
            return List.of();
        }

        if (files.size() > MAX_FILES) {
            throw new InvalidFileException("Cannot upload more than " + MAX_FILES + " files at once");
        }

        List<IssueAttachmentResponse> responses = new ArrayList<>();

        for (MultipartFile file : files) {
            validateFile(file);

            try {
                Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "image"));

                IssueAttachment attachment = IssueAttachment.builder()
                        .id(UUID.randomUUID())
                        .issueId(issueId)
                        .uploadedBy(uploadedBy)
                        .publicId((String) uploadResult.get("public_id"))
                        .url((String) uploadResult.get("secure_url"))
                        .format((String) uploadResult.get("format"))
                        .originalFilename(FilenameUtils.getName(file.getOriginalFilename()))
                        .fileSize(file.getSize())
                        .dateCreated(OffsetDateTime.now())
                        .build();

                issueAttachmentRepository.insertAttachment(attachment);

                responses.add(new IssueAttachmentResponse(
                        attachment.getId(),
                        attachment.getUrl(),
                        attachment.getFormat(),
                        attachment.getOriginalFilename(),
                        attachment.getFileSize()
                ));
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload file to Cloudinary", e);
            }
        }
        return responses;
    }

    private void validateFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new InvalidFileException("File cannot be null or empty");
        }

        String contentType = file.getContentType();
        if (!ALLOWED_TYPES.contains(contentType)) {
            throw new InvalidFileException("Unsupported file type: " + contentType);
        }

        if (file.getSize() > MAX_SIZE_BYTES) {
            throw new InvalidFileException("File is too large. Max allowed: 5MB");
        }
    }

    public List<IssueAttachmentResponse> getAttachmentsByIssueId(UUID issueId) {

        List<IssueAttachment> attachments = issueAttachmentRepository.getAttachmentsByIssueId(issueId);

        return attachments.stream()
                .map(attachment -> new IssueAttachmentResponse(
                        attachment.getId(),
                        attachment.getUrl(),
                        attachment.getFormat(),
                        attachment.getOriginalFilename(),
                        attachment.getFileSize()
                ))
                .toList();
    }
}
