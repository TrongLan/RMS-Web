package com.dtl.rms_web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class RmsWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(RmsWebApplication.class, args);
	}

	@Bean
	RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(
				new HttpComponentsClientHttpRequestFactory());
		restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
		return restTemplate;
	}

}
