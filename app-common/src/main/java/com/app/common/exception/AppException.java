package com.app.common.exception;

import com.app.common.message.StatusCode;

public class AppException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7907292008776031560L;
	
	private StatusCode statusCode;

	public AppException(StatusCode unauthorized) {
		super();
		this.statusCode = unauthorized;
	}

	public StatusCode getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(StatusCode statusCode) {
		this.statusCode = statusCode;
	}
}