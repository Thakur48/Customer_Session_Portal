package com.session.advice;

import static com.session.constant.ConstantUsage.ERR_MSG;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.session.exception.CustomerNameInvalidException;
import com.session.exception.CustomerNotFoundExcpetion;
import com.session.exception.SessionCannotBeDeleted;
import com.session.exception.SessionCantBeArchivedException;
import com.session.exception.SessionNotActiveException;
import com.session.exception.SessionNotCreatedException;
import com.session.exception.SessionNotFoundByStatus;
import com.session.exception.SessionNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException ex) {
		Map<String, String> err = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error -> {

			err.put(error.getField(), error.getDefaultMessage());

		});
		return err;
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(SessionNotCreatedException.class)
	public Map<String, String> sessionNotCreatedException(SessionNotCreatedException ex) {
		Map<String, String> err = new HashMap<>();
		err.put(ERR_MSG, ex.getMessage());
		return err;

	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(CustomerNotFoundExcpetion.class)
	public Map<String, String> customerNotFoundExcpetion(CustomerNotFoundExcpetion ex) {
		Map<String, String> err = new HashMap<>();
		err.put(ERR_MSG, ex.getMessage());
		return err;

	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(SessionCantBeArchivedException.class)
	public Map<String, String> sessionCantBeArchivedException(SessionCantBeArchivedException ex) {
		Map<String, String> err = new HashMap<>();
		err.put(ERR_MSG, ex.getMessage());
		return err;

	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(SessionNotFoundException.class)
	public Map<String, String> sessionNotFoundExceptionGlobal(SessionNotFoundException ex) {
		Map<String, String> err = new HashMap<>();
		err.put(ERR_MSG, ex.getMessage());
		return err;

	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(SessionCannotBeDeleted.class)
	public Map<String, String> sessionCannotBeDeleted(SessionCannotBeDeleted ex) {
		Map<String, String> err = new HashMap<>();
		err.put(ERR_MSG, ex.getMessage());
		return err;

	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(SessionNotActiveException.class)
	public Map<String, String> sessionNotActive(SessionNotActiveException ex) {
		Map<String, String> err = new HashMap<>();
		err.put(ERR_MSG, ex.getMessage());
		return err;

	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(CustomerNameInvalidException.class)
	public Map<String, String> customerNameInvalidException(CustomerNameInvalidException ex) {
		Map<String, String> err = new HashMap<>();
		err.put(ERR_MSG, ex.getMessage());
		return err;

	}
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(SessionNotFoundByStatus.class)
	public Map<String, String> sessionNotFoundByStatus(SessionNotFoundByStatus ex) {
		Map<String, String> err = new HashMap<>();
		err.put(ERR_MSG, ex.getMessage());
		return err;

	}

}
