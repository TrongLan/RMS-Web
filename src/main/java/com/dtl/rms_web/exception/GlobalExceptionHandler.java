package com.dtl.rms_web.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException.BadRequest;
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
			attributes.addFlashAttribute("error_message", responseBody);
			return "redirect:" + request.getHeader("Referer");
		} catch (JsonProcessingException e) {
			log.error(e.getMessage());
			return "internal_server_error";
		}

	}
}
