package com.SmartInventory.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// 404
	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApiError handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {

		return ApiError.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.NOT_FOUND.value())
				.error("Not Found")
				.message(ex.getMessage())
				.path(request.getRequestURI()).build();
	}

	// 409
	@ExceptionHandler(DuplicateResourceException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ApiError handleDuplicate(DuplicateResourceException ex, HttpServletRequest request) {

		return ApiError.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.CONFLICT.value())
				.error("Conflict")
				.message(ex.getMessage())
				.path(request.getRequestURI()).build();
	}

	// 422
	@ExceptionHandler(InsufficientStockException.class)
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	public ApiError handleStock(InsufficientStockException ex, HttpServletRequest request) {
		

		return ApiError.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.UNPROCESSABLE_ENTITY.value())
				.error("Unprocessable Entity")
				.message(ex.getMessage())
				.path(request.getRequestURI()).build();
	}

	// 400 - validation & bad request
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiError handleGeneric(Exception ex, HttpServletRequest request) {

		return ApiError.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value())
				.error("Bad Request")
				.message(ex.getMessage())
				.path(request.getRequestURI()).build();
	}
}
