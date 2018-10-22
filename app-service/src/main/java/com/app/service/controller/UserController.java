package com.app.service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.common.message.StatusCode;
import com.app.common.redis.RedisKeyPrefix;
import com.app.common.redis.RedisUtil;
import com.app.dao.UserMapper;
import com.app.entity.Resp;
import com.app.entity.dto.UserInfoDto;


@RestController
@RequestMapping("/user")
public class UserController{

    private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private RedisUtil redisService;
	
	@Autowired
    private UserMapper userMapper;

	@RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
	public Resp getUserInfo(@RequestHeader(value="userId") String userId) {
    	log.info("getUserInfo: " + userId);
    	if (userId == null) {
    		return new Resp(StatusCode.BAD_REQUEST.value());
    	}
    	
    	UserInfoDto user = new UserInfoDto();
    	user.setUserId(Long.valueOf(userId));
    	user.setUserDeviceId(redisService.hGet(RedisKeyPrefix.USER_INFO_ + userId, "userDeviceId"));
    	user.setUserMobilephone(redisService.hGet(RedisKeyPrefix.USER_INFO_ + userId, "userMobilephone"));
    	user.setUserDeviceType(redisService.hGet(RedisKeyPrefix.USER_INFO_ + userId, "userDeviceType"));
    	
		return new Resp(user);
	}
}
