package com.dtl.rms_web.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FileInfoDTO {
	private String name;
	private long size;
	private byte[] content;
}
