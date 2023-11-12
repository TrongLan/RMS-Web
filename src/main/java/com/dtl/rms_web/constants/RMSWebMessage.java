package com.dtl.rms_web.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RMSWebMessage {
	CREATED("Tạo mới thành công.");

	private final String content;
}
