package com.app.common.message;

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
	
	private final int value;

	StatusCode(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public String toString() {
		return String.valueOf(this.value);
	}
}
