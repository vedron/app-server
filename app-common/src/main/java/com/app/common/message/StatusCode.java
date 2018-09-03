package com.app.common.message;

import com.app.common.message.Resources;

public enum StatusCode {
	/** 200请求成功 */
	OK(200),
	/** 303登录失败 */
	LOGIN_FAIL(303),
	/** 400请求参数出错 */
	BAD_REQUEST(400),
	/** 401没有登录 */
	UNAUTHORIZED(401),
	/** 403没有权限 */
	FORBIDDEN(403),
	/** 404找不到页面 */
	NOT_FOUND(404),
	/** 408请求超时 */
	REQUEST_TIMEOUT(408),
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
