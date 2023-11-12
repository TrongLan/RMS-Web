package com.dtl.rms_web.controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dtl.rms_web.configuration.TokenHolder;
import com.dtl.rms_web.constants.Endpoint;
import com.dtl.rms_web.constants.RMSWebMessage;
import com.dtl.rms_web.dtos.HiringNewsCreateDTO;
import com.dtl.rms_web.models.Category;
import com.dtl.rms_web.models.HiringNews;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Controller
@RequestMapping("/hiring-news")
@Slf4j
public class HiringNewsController {
	private final TokenHolder tokenHolder;
	private final RestTemplate restTemplate;

	@GetMapping("/edit")
	public String openNewsEditPage(Model model) {
		if (StringUtils.hasText(tokenHolder.getToken())) {
			hasToken(model);
			addCategories(model);
			model.addAttribute("hiring_news", new HiringNewsCreateDTO());
			return "upload_hiring_news";
		}
		return "redirect:/auth/login";
	}

	@PostMapping("/upload")
	public String uploadNews(@ModelAttribute HiringNewsCreateDTO hiringNews,
			RedirectAttributes attributes) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization",
				"Bearer %s".formatted(tokenHolder.getToken()));
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<HiringNewsCreateDTO> entity = new HttpEntity<>(hiringNews,
				headers);
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(
				Endpoint.UPLOAD_HIRING_NEWS.getUrl(), entity, String.class);
		HttpStatus code = (HttpStatus) responseEntity.getStatusCode();
		if (code == HttpStatus.CREATED) {
			attributes.addFlashAttribute("message",
					RMSWebMessage.CREATED.getContent());
			return "redirect:/hiring-news/detail/%s"
					.formatted(responseEntity.getBody());
		} else {
			log.error("Calling API '{}' should return 201 not {}",
					Endpoint.UPLOAD_HIRING_NEWS.getUrl(), code.value());
			return "redirect:/hiring-news/edit";
		}
	}

	@GetMapping("/detail/{id}")
	public String openNewsDetailPage(Model model, @PathVariable String id) {
		hasToken(model);
		addCategories(model);
		HiringNews hiringNews = restTemplate.getForObject(
				Endpoint.HIRING_NEWS_DETAILS.getUrl().formatted(id),
				HiringNews.class);
		model.addAttribute("news", hiringNews);
		HashMap<Integer, String> newsStatusMap = new HashMap<Integer, String>();
		newsStatusMap.put(0, "Chờ đánh giá");
		newsStatusMap.put(1, "Phù hợp");
		newsStatusMap.put(2, "Chưa phù hợp");
		model.addAttribute("statusMap", newsStatusMap);
		return "hiring_news_detail";
	}

	private void addCategories(Model model) {
		Category[] categories = restTemplate.getForObject(
				Endpoint.LIST_CATEGORY.getUrl(), Category[].class);
		model.addAttribute("categories", categories);
	}

	private void hasToken(Model model) {
		if (!StringUtils.hasText(tokenHolder.getToken())) {
			model.addAttribute("hasToken", false);
		} else {
			model.addAttribute("hasToken", true);
		}
	}

	@GetMapping("/list/{id}")
	public String openListPage(@PathVariable long id, Model model) {
		hasToken(model);
		Category[] categories = restTemplate.getForObject(
				Endpoint.LIST_CATEGORY.getUrl(), Category[].class);
		model.addAttribute("categories", categories);
		Map<Long, String> categoryMap = Arrays.asList(categories).stream()
				.collect(Collectors.toMap(Category::getId, Category::getName));
		model.addAttribute("categoryName", categoryMap.get(id));
		HiringNews[] hiringNewsList = restTemplate.getForObject(
				Endpoint.LIST_HIRING_NEWS_IN_CATEGORY.getUrl().formatted(id),
				HiringNews[].class);
		model.addAttribute("hiringNewsList", hiringNewsList);
		return "hiring_news_list";
	}

}
