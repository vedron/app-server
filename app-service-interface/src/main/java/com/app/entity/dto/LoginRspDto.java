package com.app.entity.dto;

import java.io.Serializable;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;


public class LoginRspDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8781266438287531071L;


	private String token;

	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}


}
