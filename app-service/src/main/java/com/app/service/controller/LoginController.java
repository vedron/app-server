package com.app.service.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.common.redis.RedisKeyPrefix;
import com.app.common.redis.RedisUtil;
import com.app.entity.dto.LoginDto;
import com.app.service.ILoginService;


@RestController
@RequestMapping("/login")
public class LoginController implements ILoginService {

    private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private RedisUtil redisService;
	
	@RequestMapping(value = "/checkPassword", method = RequestMethod.POST)
	public String checkPassword(@RequestBody @Valid LoginDto dto) {
    	log.info("checkPassword phone: " + dto.getPhone());
		String ret = null;
    	if (redisService.exists(RedisKeyPrefix.User_Base_info + dto.getPhone())) {
    		ret = redisService.hGet(RedisKeyPrefix.User_Base_info + dto.getPhone(), "token");
    	}
    	log.info("checkPassword ret: " + ret);
        return ret;
    }
}
