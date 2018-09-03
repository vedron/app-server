package com.app.service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.common.redis.RedisKeyPrefix;
import com.app.common.redis.RedisUtil;
import com.app.entity.dto.LoginDto;
import com.app.service.ILoginService;


@RestController
public class LoginController implements ILoginService {

    private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private RedisUtil redisService;
	
    @GetMapping("/checkPassword")
	public String checkPassword(LoginDto dto) {
		String ret = null;
    	if (redisService.exists(RedisKeyPrefix.User_Base_info + "1")) {
    		ret = redisService.hGet(RedisKeyPrefix.User_Base_info + "1", "token");
    		log.info("checkPassword");
    	}
        return ret;
    }
}
