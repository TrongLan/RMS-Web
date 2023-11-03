package com.dtl.rms_web.models;

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
public class Account {
	private int id;
	private String email;
	private String password;
	private String staffCode;
	private int isActive;
	private List<HiringNews> newsList;
}
