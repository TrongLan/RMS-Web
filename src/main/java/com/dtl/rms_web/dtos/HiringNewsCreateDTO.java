package com.dtl.rms_web.dtos;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HiringNewsCreateDTO {
	private String title;
	private LocalDate dueDate;
	private int quantity;
	private int salaryMin;
	private int salaryMax;
	private String description;
	private String benefits;
	private String requirements;
	private Long categoryId;
}
