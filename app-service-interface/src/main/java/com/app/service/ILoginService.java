package com.app.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.app.entity.dto.LoginDto;

@FeignClient(value="login")
public interface ILoginService {
	
	@GetMapping("/checkPassword")
	public String checkPassword(LoginDto dto);
	
}
