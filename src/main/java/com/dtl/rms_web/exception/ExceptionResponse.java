package com.dtl.rms_web.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class ExceptionResponse {
	private final String message;
	@JsonIgnore
	private final Throwable throwable;
	private final LocalDateTime dateTime;
	private final HttpStatus httpStatus;

}
