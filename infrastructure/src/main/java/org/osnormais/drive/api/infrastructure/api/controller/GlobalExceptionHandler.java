package org.osnormais.drive.api.infrastructure.api.controller;

import org.osnormais.drive.api.application.exception.InvalidInputException;
import org.osnormais.drive.api.application.exception.NotFoundException;
import org.osnormais.drive.api.domain.exception.FileAlreadyExistsException;
import org.osnormais.drive.api.domain.exception.FolderAlreadyExistsException;
import org.osnormais.drive.api.domain.exception.InconsistentStateException;
import org.osnormais.drive.api.domain.exception.InvalidArgumentException;
import org.osnormais.drive.api.domain.exception.InvalidFilterException;
import org.osnormais.drive.api.domain.exception.QuotaExceededException;
import org.osnormais.drive.api.domain.exception.TransferChannelExpiredException;
import org.osnormais.drive.api.domain.exception.TransferChannelFileMismatchException;
import org.osnormais.drive.api.domain.exception.TransferChannelNotOwnedByUserException;
import org.osnormais.drive.api.domain.exception.ValidationException;
import org.osnormais.drive.api.infrastructure.api.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handle(final Exception ex) {
        return ResponseEntity.internalServerError().body(ApiError.with("Internal Error"));
    }

    @ExceptionHandler(InconsistentStateException.class)
    public ResponseEntity<ApiError> handle(final InconsistentStateException ex) {
        return ResponseEntity.internalServerError().body(ApiError.with("Internal Error"));
    }

    @ExceptionHandler(FileAlreadyExistsException.class)
    public ResponseEntity<ApiError> handle(final FileAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiError.from(ex));
    }

    @ExceptionHandler(FolderAlreadyExistsException.class)
    public ResponseEntity<ApiError> handle(final FolderAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiError.from(ex));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiError> handle(final ValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiError.from(ex));
    }

    @ExceptionHandler(InvalidArgumentException.class)
    public ResponseEntity<ApiError> handle(final InvalidArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiError.from(ex));
    }

    @ExceptionHandler(InvalidFilterException.class)
    public ResponseEntity<ApiError> handle(final InvalidFilterException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiError.from(ex));
    }

    @ExceptionHandler(TransferChannelNotOwnedByUserException.class)
    public ResponseEntity<ApiError> handle(final TransferChannelNotOwnedByUserException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiError.with("TranseferChannel NotFound"));
    }

    @ExceptionHandler(TransferChannelFileMismatchException.class)
    public ResponseEntity<ApiError> handle(final TransferChannelFileMismatchException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiError.with("TranseferChannel NotFound"));
    }

    @ExceptionHandler(TransferChannelExpiredException.class)
    public ResponseEntity<ApiError> handle(final TransferChannelExpiredException ex) {
        return ResponseEntity.status(HttpStatus.GONE).body(ApiError.from(ex));
    }

    @ExceptionHandler(QuotaExceededException.class)
    public ResponseEntity<ApiError> handle(final QuotaExceededException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiError.from(ex));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handle(final NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiError.from(ex));
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ApiError> handle(final InvalidInputException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiError.from(ex));
    }

}
