package com.dtl.rms_web.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException.BadRequest;
import org.springframework.web.client.HttpClientErrorException.Forbidden;
import org.springframework.web.client.HttpClientErrorException.NotFound;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

	private final ObjectMapper mapper;

	@ExceptionHandler(value = {BadRequest.class})
	public String handlingBadRequest(BadRequest badRequest,
			HttpServletRequest request, RedirectAttributes attributes) {
		ExceptionResponse responseBody;
		try {
			responseBody = mapper.readValue(
					badRequest.getResponseBodyAsString(),
					ExceptionResponse.class);
			attributes.addFlashAttribute("error_message",
					responseBody.getMessage());
			return "redirect:" + request.getHeader("Referer");
		} catch (JsonProcessingException e) {
			log.error(e.getMessage());
			return "error/internal_server_error";
		}

	}

	@ExceptionHandler(value = {Forbidden.class})
	public String handlingForbiden(Forbidden forbidden) {
		log.error(forbidden.getMessage());
		return "redirect:/auth/login";
	}

	@ExceptionHandler(value = {InternalServerError.class})
	public String handlingInternalServerError(
			InternalServerError internalServerError) {
		log.error(internalServerError.getMessage());
		return "error/internal_server_error";
	}

	@ExceptionHandler(value = {NotFound.class})
	public String handlingNotFound(NotFound notFound) {
		log.error(notFound.getMessage());
		return "error/not_found";
	}
}
