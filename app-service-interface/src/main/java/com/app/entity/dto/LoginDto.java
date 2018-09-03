package com.app.entity.dto;

import java.io.Serializable;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;


public class LoginDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8998323885631150315L;

	@Pattern(regexp = "^1\\d{10}$", message = "手机号格式错误")
	private String phone;

	@Length(min = 6, max = 6,message="验证码长度错误")
	private String code;

	@Length(max = 50,message="设备类型错误")
	private String device;

	@Length(max = 100,message="设备类型错误")
	private String deviceId;

	private String LoginIp;
	
	private Integer accountType = 1;

	public String getLoginIp() {
		return LoginIp;
	}

	public void setLoginIp(String loginIp) {
		LoginIp = loginIp;
	}

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

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}


	public Integer getAccountType() {
		return accountType;
	}

	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}

}
