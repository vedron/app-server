package com.app.entity.dto;

import java.io.Serializable;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

public class VerifyCodeRspDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8547866069292640724L;
	
	private String verifyCode;

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
	
}
