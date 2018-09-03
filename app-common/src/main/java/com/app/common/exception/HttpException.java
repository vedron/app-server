package com.app.common.exception;

import com.app.common.message.StatusCode;

@SuppressWarnings("serial")
public class HttpException extends BaseException{
	public HttpException() {
	}

	public HttpException(Throwable ex) {
		super(ex);
	}

	public HttpException(String message) {
		super(message);
	}

	public HttpException(StatusCode statusCode) {
		super(statusCode);
	}
	public HttpException(StatusCode statusCode, String message) {
		super(statusCode, message);
	}
	public HttpException(String message, Throwable ex) {
		super(message, ex);
	}

	@Override
	protected StatusCode getStatusCode() {
		// TODO Auto-generated method stub
		return super.statusCode != null ? super.statusCode : StatusCode.INTERNAL_SERVER_ERROR;
	}
}
