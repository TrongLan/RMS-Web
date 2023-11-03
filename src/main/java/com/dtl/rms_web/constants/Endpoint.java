package com.dtl.rms_web.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Endpoint {
	GET_JWT_TOKEN("http://localhost:8080/auth/login");
	private final String url;
}
