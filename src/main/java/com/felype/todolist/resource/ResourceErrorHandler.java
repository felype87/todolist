package com.felype.todolist.resource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.felype.todolist.exception.BackendServiceException;
import com.felype.todolist.exception.ItemNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ResourceErrorHandler {

	@ExceptionHandler({ ItemNotFoundException.class, EmptyResultDataAccessException.class })
	public ResponseEntity<Object> handleItemNotFoundException(ItemNotFoundException e) {
		log.info(e.getMessage(), e);

		return new ResponseEntity<Object>(new HttpHeaders(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(BackendServiceException.class)
	public ResponseEntity<Object> handleBackendServiceException(BackendServiceException backendServiceException) {
		// Don't expose implementation details when failing.
		log.error("Unexpected Error", backendServiceException.getCause());

		return new ResponseEntity<Object>(new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
