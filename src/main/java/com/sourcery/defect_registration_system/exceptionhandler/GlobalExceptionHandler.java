package com.sourcery.defect_registration_system.exceptionhandler;

import com.sourcery.defect_registration_system.exception.BadRequestException;
import com.sourcery.defect_registration_system.exception.NotFoundException;
import com.sourcery.defect_registration_system.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, Object>> handle401(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                Map.of("timestamp", Instant.now(), "status", 401, "error", "Unauthorized", "message", ex.getMessage())
        );
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handle404(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("timestamp", Instant.now(), "status", 404, "error", "Not Found", "message", ex.getMessage())
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handle400(BadRequestException ex) {
        return ResponseEntity.badRequest().body(
                Map.of("timestamp", Instant.now(), "status", 400, "error", "Bad Request", "message", ex.getMessage())
        );
    }
}
