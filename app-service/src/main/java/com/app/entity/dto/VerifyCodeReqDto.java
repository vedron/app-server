package com.app.entity.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(value = "VerifyCodeReqDto", description = "获取验证码接口的请求内容")
public class VerifyCodeReqDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8547866069292640724L;
	
	@NotBlank
	@Pattern(regexp = "^1\\d{10}$", message = "手机号格式错误")
	@ApiModelProperty(value = "手机号码",required = true)
	private String phone;
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
