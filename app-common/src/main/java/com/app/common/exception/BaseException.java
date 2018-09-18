package com.app.common.exception;

import com.app.common.message.Resources;
import com.app.common.message.StatusCode;
import com.app.common.model.Meta;
import com.app.common.utils.StringUtils;

@SuppressWarnings("serial")
public abstract class BaseException  extends RuntimeException{

	protected StatusCode statusCode;

	public BaseException() {
	}

	public BaseException(Throwable ex) {
		super(ex);
	}

	public BaseException(String message) {
		super(message);
	}
	public BaseException(StatusCode statusCode) {
		this.statusCode = statusCode;
	}
	public BaseException(StatusCode statusCode, String message) {
		super(message);
		this.statusCode = statusCode;
	}

	public BaseException(String message, Throwable ex) {
		super(message, ex);
	}

	public void handler(Meta meta) {
		meta.setSuccess(false);
		meta.setCode(getStatusCode().value());
		if (StringUtils.isNotBlank(getMessage())) {
			meta.setMsg(getMessage()); // 取系统的错误消息
		}else {
			meta.setMsg(Resources.getMessage(getStatusCode().value() + ""));
		}
	}

	protected abstract StatusCode getStatusCode();
}