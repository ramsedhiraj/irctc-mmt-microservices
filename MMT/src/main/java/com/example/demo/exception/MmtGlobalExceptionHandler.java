package com.example.demo.exception;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestControllerAdvice
public class MmtGlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(MmtGlobalExceptionHandler.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Object> handleHttpClientErrorException(HttpClientErrorException ex) {
        log.warn("HTTP client error from IRCTC: status={}, body={}", ex.getStatusCode(), ex.getResponseBodyAsString());
        try {
            String body = ex.getResponseBodyAsString();
            if (body != null && !body.isEmpty()) {
                Map<String, Object> map = objectMapper.readValue(body, Map.class);
                return new ResponseEntity<>(map, ex.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Failed to parse IRCTC error response body", e);
        }

        MmtErrorDetails errorDetails = new MmtErrorDetails(
            LocalDateTime.now(),
            ex.getStatusCode().value(),
            "Client Error from IRCTC",
            ex.getStatusText(),
            ""
        );
        return new ResponseEntity<>(errorDetails, ex.getStatusCode());
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<MmtErrorDetails> handleHttpServerErrorException(HttpServerErrorException ex, WebRequest request) {
        log.error("HTTP server error from IRCTC: status={}, body={}", ex.getStatusCode(), ex.getResponseBodyAsString());
        MmtErrorDetails errorDetails = new MmtErrorDetails(
            LocalDateTime.now(),
            ex.getStatusCode().value(),
            "Server Error from IRCTC",
            "IRCTC service is currently experiencing issues. Please try again later.",
            request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, ex.getStatusCode());
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<MmtErrorDetails> handleResourceAccessException(ResourceAccessException ex, WebRequest request) {
        log.error("Connection failure to IRCTC service: {}", ex.getMessage());
        MmtErrorDetails errorDetails = new MmtErrorDetails(
            LocalDateTime.now(),
            HttpStatus.SERVICE_UNAVAILABLE.value(),
            "Service Unavailable",
            "Cannot connect to IRCTC service. Please verify if the provider is running.",
            request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<MmtErrorDetails> handleRestClientException(RestClientException ex, WebRequest request) {
        log.error("REST client communication exception: {}", ex.getMessage());
        MmtErrorDetails errorDetails = new MmtErrorDetails(
            LocalDateTime.now(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            "Error communicating with IRCTC service: " + ex.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MmtErrorDetails> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        String details = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));
        
        MmtErrorDetails errorDetails = new MmtErrorDetails(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request",
            "Validation Failed: " + details,
            request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MmtErrorDetails> handleGlobalException(Exception ex, WebRequest request) {
        log.error("Unexpected error in MMT service", ex);
        MmtErrorDetails errorDetails = new MmtErrorDetails(
            LocalDateTime.now(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            ex.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
