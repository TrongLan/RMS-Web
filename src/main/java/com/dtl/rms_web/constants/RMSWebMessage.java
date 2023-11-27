package com.dtl.rms_web.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RMSWebMessage {
	CREATED("Đăng tải thành công."),
	APPROVED_SUCCESS("Cập nhật trạng thái thành công.");

	private final String content;
}
