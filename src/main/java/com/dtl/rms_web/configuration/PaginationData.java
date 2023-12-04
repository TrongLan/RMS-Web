package com.dtl.rms_web.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaginationData {
	private Integer totalPages;
	private Integer number;
	private Integer size;
	private Integer totalElements;
}
