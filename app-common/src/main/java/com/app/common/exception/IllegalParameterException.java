package com.app.common.exception;

import com.app.common.exception.BaseException;
import com.app.common.message.StatusCode;

@SuppressWarnings("serial")
public class IllegalParameterException extends BaseException{
	public IllegalParameterException() {
	}

	public IllegalParameterException(Throwable ex) {
		super(ex);
	}

	public IllegalParameterException(String message) {
		super(message);
	}

	public IllegalParameterException(StatusCode statusCode) {
		super(statusCode);
	}
	public IllegalParameterException(StatusCode statusCode, String message) {
		super(statusCode, message);
	}
	public IllegalParameterException(String message, Throwable ex) {
		super(message, ex);
	}

	@Override
	protected StatusCode getStatusCode() {
		// TODO Auto-generated method stub
		return super.statusCode != null ? super.statusCode : StatusCode.BAD_REQUEST;
	}

}
