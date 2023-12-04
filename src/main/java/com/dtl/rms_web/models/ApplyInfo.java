package com.dtl.rms_web.models;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ApplyInfo {
	private String id;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private LocalDateTime applyDate;
	private int status;
	private String cvURL;
	private HiringNews hiringNews;

}
