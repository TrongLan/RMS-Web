package com.dtl.rms_web.controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dtl.rms_web.configuration.TokenHolder;
import com.dtl.rms_web.constants.Endpoint;
import com.dtl.rms_web.constants.RMSWebMessage;
import com.dtl.rms_web.dtos.ApplyInfoCreateDTO;
import com.dtl.rms_web.dtos.FileInfoDTO;
import com.dtl.rms_web.dtos.HiringNewsCreateDTO;
import com.dtl.rms_web.models.Category;
import com.dtl.rms_web.models.HiringNews;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
			attributes.addFlashAttribute("success_message",
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
		model.addAttribute("dto",
				ApplyInfoCreateDTO.builder().newsId(id).build());
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

	@PostMapping(path = "/apply")
	public String jobApplying(@ModelAttribute ApplyInfoCreateDTO dto,
			@RequestParam MultipartFile file, RedirectAttributes attributes) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		ContentDisposition contentDisposition = ContentDisposition
				.builder("form-data").name("file")
				.filename(file.getOriginalFilename()).build();
		body.add(HttpHeaders.CONTENT_DISPOSITION,
				contentDisposition.toString());
		body.add("file", file.getResource());
		body.add("Content-Type", file.getContentType());
		String dtoJson = String.format("""
				{
					"firstName": "%s",
					"lastName": "%s",
					"email": "%s",
					"phoneNumber": "%s",
					"newsId": "%s"
				}
				""", dto.getFirstName(), dto.getLastName(), dto.getEmail(),
				dto.getPhoneNumber(), dto.getNewsId());
		body.add("dto", dtoJson);

		HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(
				body, headers);
		ResponseEntity<Object> postForEntity = restTemplate.postForEntity(
				Endpoint.JOB_APPLY.getUrl(), entity, Object.class);
		HttpStatusCode statusCode = postForEntity.getStatusCode();
		if (statusCode == HttpStatus.CREATED) {
			attributes.addFlashAttribute("success_message",
					RMSWebMessage.CREATED.getContent());
			return "redirect:/hiring-news/detail/%s".formatted(dto.getNewsId());
		} else {
			log.error("Calling API {} should return 201 not {}.",
					Endpoint.JOB_APPLY.getUrl(), statusCode);
			return "error/internal_server_error";
		}

	}

	@GetMapping("/approve/{id}/{status}")
	public String approveApplyInfo(@PathVariable String id,
			@PathVariable Integer status, RedirectAttributes attributes,
			HttpServletRequest request) {
		// Prepare headers
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization",
				"Bearer %s".formatted(tokenHolder.getToken()));
		HttpEntity<String> entity = new HttpEntity<>(headers);

		// Send the GET request
		ResponseEntity<String> responseEntity = restTemplate.exchange(
				Endpoint.APPROVE_APPLY_INFO.getUrl().formatted(id, status),
				HttpMethod.GET, entity, String.class);
		HttpStatusCode statusCode = responseEntity.getStatusCode();
		if (statusCode == HttpStatus.OK) {
			attributes.addFlashAttribute("success_message",
					RMSWebMessage.APPROVED_SUCCESS.getContent());
			return "redirect:" + request.getHeader("Referer");
		} else {
			log.error("Calling API {} should return 200 not {}.",
					Endpoint.JOB_APPLY.getUrl(), statusCode);
			return "error/internal_server_error";
		}
	}

	@GetMapping("/download-cv/{id}")
	public void downloadFile(@PathVariable String id,
			HttpServletResponse response) throws URISyntaxException {
		// Prepare headers
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization",
				"Bearer %s".formatted(tokenHolder.getToken()));
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<FileInfoDTO> responseEntity = restTemplate.exchange(
				Endpoint.DOWNLOAD_FILE_CV.getUrl().formatted(id),
				HttpMethod.GET, entity, FileInfoDTO.class);
		FileInfoDTO body = responseEntity.getBody();
		response.setContentType("application/octet-stream");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + body.getName();
		response.setHeader(headerKey, headerValue);

		try {
			ServletOutputStream outputStream = response.getOutputStream();
			outputStream.write(body.getContent());
			outputStream.close();
		} catch (IOException e) {
			log.error(e.getMessage());
		}

	}

}
