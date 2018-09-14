package com.app.server.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.common.security.IgnoreSecurity;
import com.app.entity.Resp;
import com.app.entity.dto.LoginReqDto;
import com.app.entity.dto.VerifyCodeReqDto;
import com.app.service.LoginService;


@RestController
@RequestMapping("/login")
public class LoginController{
    @Autowired
    private LoginService loginService;

	@IgnoreSecurity
	@RequestMapping(value = "/getVerifyCode", method = RequestMethod.POST)
	public Resp getVerifyCode(@RequestBody @Valid VerifyCodeReqDto dto, BindingResult error) {
        return loginService.getVerifyCode(dto);
    }
	
	@IgnoreSecurity
	@RequestMapping(value = "/verifyCode", method = RequestMethod.POST)
	public Resp verifyCode(@RequestBody @Valid LoginReqDto dto, BindingResult error) {
        return loginService.verifyCode(dto);
    }
}
