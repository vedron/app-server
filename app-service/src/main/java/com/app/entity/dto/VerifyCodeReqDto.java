package com.app.entity.dto;

import java.io.Serializable;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

public class VerifyCodeReqDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8547866069292640724L;
	
	@NotBlank
	@Pattern(regexp = "^1\\d{10}$", message = "手机号格式错误")
	private String phone;
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
