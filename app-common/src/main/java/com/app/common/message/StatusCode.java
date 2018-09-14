package com.app.common.message;

import com.app.common.message.Resources;

public enum StatusCode {
	/** 200请求成功 */
	OK(200),
	/** 301获取验证码频繁 */
	VERIFY_CODE_BUSY(301),
	/** 400请求参数出错 */
	BAD_REQUEST(400),
	/** 401没有登录 */
	UNAUTHORIZED(401),
	/** 500服务器出错 */
	INTERNAL_SERVER_ERROR(500)
	;
	
	private final Integer value;

	StatusCode(Integer value) {
		this.value = value;
	}

	/**
	 * Return the integer value of this status code.
	 */
	public Integer value() {
		return this.value;
	}

	public String msg() {
		return Resources.getMessage("" + this.value);
	}

	public String toString() {
		return this.value.toString();
	}
}
