package com.app.entity.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModelProperty;


public class LoginReqDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8781266438287531071L;
	
	@NotBlank
	@Pattern(regexp = "^1\\d{10}$", message = "手机号格式错误")
	@ApiModelProperty(value = "手机号码",required = true)
	private String phone;

	@Length(min = 6, max = 6,message="验证码长度错误")
	@ApiModelProperty(value = "验证码",required = true)
	private String code;

	@NotBlank
	@Length(max = 50,message="设备类型错误")
	@ApiModelProperty(value = "设备类型",required = true)
	private String deviceType;

	@NotBlank
	@Length(max = 100,message="设备ID错误")
	@ApiModelProperty(value = "设备ID",required = true)
	private String deviceId;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
}
