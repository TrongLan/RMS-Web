package com.dtl.rms_web.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ApplyInfoCreateDTO {
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private String newsId;
}
