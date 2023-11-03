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
public class Category {
	private long id;
	private String name;
	private int isActive;
	private List<HiringNews> newsList;
}
