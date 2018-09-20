package com.app.entity.dto;

public class UserInfoDto extends UserIdDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5205094535097551076L;

	private Long userId;
	
	private String userMobilephone;
	
	private String userDeviceType;

	private String userDeviceId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserMobilephone() {
		return userMobilephone;
	}

	public void setUserMobilephone(String userMobilephone) {
		this.userMobilephone = userMobilephone;
	}

	public String getUserDeviceType() {
		return userDeviceType;
	}

	public void setUserDeviceType(String userDeviceType) {
		this.userDeviceType = userDeviceType;
	}

	public String getUserDeviceId() {
		return userDeviceId;
	}

	public void setUserDeviceId(String userDeviceId) {
		this.userDeviceId = userDeviceId;
	}

}
