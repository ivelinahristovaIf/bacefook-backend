package com.bacefook.controller;

import java.sql.SQLIntegrityConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bacefook.exception.UserNotFoundException;

@RestController
public class BaseController {
	@ExceptionHandler({ NullPointerException.class })
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleNullPointerError() {
		return "Something went wrong!";
	}

	@ExceptionHandler({ SQLIntegrityConstraintViolationException.class })
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleSQLIntegrityConstraintViolatingError() {
		return "Some DB columns are not filled";
	}

	@ExceptionHandler({ UserNotFoundException.class })
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleUserNotFoundError() {
		return "No such user!";
	}
}
