package com.app.common.exception;

public class BindingResultException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5463646154732830613L;
	
	private String errorMsg;
	
	public BindingResultException(String errorMsg) {
		super();
		this.errorMsg = errorMsg;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}