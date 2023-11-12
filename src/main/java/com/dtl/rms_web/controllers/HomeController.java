package com.dtl.rms_web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import com.dtl.rms_web.configuration.TokenHolder;
import com.dtl.rms_web.constants.Endpoint;
import com.dtl.rms_web.models.Category;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {
	private final TokenHolder tokenHolder;
	private final RestTemplate restTemplate;

	@GetMapping("/")
	public String homePage(Model model) {
		if (!StringUtils.hasText(tokenHolder.getToken())) {
			model.addAttribute("hasToken", false);
		} else {
			model.addAttribute("hasToken", true);
		}
		Category[] categories = restTemplate.getForObject(
				Endpoint.LIST_CATEGORY.getUrl(), Category[].class);
		model.addAttribute("categories", categories);
		return "index";
	}

}
