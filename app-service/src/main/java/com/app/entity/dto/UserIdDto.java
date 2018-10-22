package com.app.entity.dto;

import java.io.Serializable;

public class UserIdDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1853426932136999460L;
	
	private Long userId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
