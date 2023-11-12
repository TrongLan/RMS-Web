package com.dtl.rms_web.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Endpoint {
	GET_JWT_TOKEN("http://localhost:8080/auth/login"),
	UPLOAD_HIRING_NEWS("http://localhost:8080/api/hr/hiring-news"),
	APPROVE_APPLY_INFO("http://localhost:8080/api/hr/approve/%s/%d"),
	JOB_APPLY("http://localhost:8080/api/common/apply"),
	LIST_CATEGORY("http://localhost:8080/api/common/categories"),
	LIST_HIRING_NEWS_IN_CATEGORY("http://localhost:8080/api/common/hiring-news/list/%d"),
	HIRING_NEWS_DETAILS("http://localhost:8080/api/common/hiring-news/details/%s");
	private final String url;
}
