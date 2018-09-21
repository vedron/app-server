package com.app.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.app.entity.Resp;
import com.app.entity.dto.LoginReqDto;
import com.app.entity.dto.VerifyCodeReqDto;

@FeignClient(value="login")
public interface ILoginService {
	@GetMapping("/getVerifyCode")
	public Resp getVerifyCode(VerifyCodeReqDto dto);
	
	@GetMapping("/loginByVerifyCode")
	public Resp loginByVerifyCode(LoginReqDto dto);
	
}
