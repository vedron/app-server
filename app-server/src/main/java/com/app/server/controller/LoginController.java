package com.app.server.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.common.security.IgnoreSecurity;
import com.app.entity.Resp;
import com.app.entity.dto.LoginDto;
import com.app.service.LoginService;


@RestController
@RequestMapping("/login")
public class LoginController{

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    
    @Autowired
    private LoginService loginService;
    
	@IgnoreSecurity
	@RequestMapping(value = "/checkPassword", method = RequestMethod.POST)
	public Resp checkPassword(@RequestBody @Valid LoginDto dto, BindingResult error) {
		String ret = loginService.checkPassword(dto);
        return new Resp(ret);
    }
}
