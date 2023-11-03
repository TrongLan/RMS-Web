package com.dtl.rms_web.models;

import java.time.LocalDate;
import java.util.List;

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
public class HiringNews {
	private String id;
	private String title;
	private LocalDate dueDate;
	private int quantity;
	private int salaryMin;
	private int salaryMax;
	private String description;
	private String benefits;
	private String requirements;
	private int status;
	private int isActive;
	private Account account;
	private Category category;
	private List<ApplyInfo> applyInfos;
}
