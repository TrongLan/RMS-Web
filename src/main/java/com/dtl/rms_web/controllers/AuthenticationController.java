package com.dtl.rms_web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.dtl.rms_web.configuration.TokenHolder;
import com.dtl.rms_web.constants.Endpoint;
import com.dtl.rms_web.dtos.LoginRequest;
import com.dtl.rms_web.dtos.LoginResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class AuthenticationController {
	private final RestTemplate restTemplate;
	private final TokenHolder tokenHolder;

	@GetMapping("/login")
	public String openLoginPage(Model model) {
		model.addAttribute("loginRequest", new LoginRequest());
		return "login_page";
	}

	@PostMapping("/login")
	public String logingIn(@ModelAttribute LoginRequest loginRequest) {
		LoginResponse loginResponse = restTemplate.postForObject(
				Endpoint.GET_JWT_TOKEN.getUrl(), loginRequest,
				LoginResponse.class);
		tokenHolder.setToken(loginResponse.getAccessToken());
		log.info("Login");
		return "redirect:/";
	}
}
