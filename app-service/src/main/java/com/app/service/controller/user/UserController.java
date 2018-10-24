package com.app.service.controller.user;

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
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping("/user")
@Api(tags = "用户模块") 
public class UserController{

    private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private RedisUtil redisService;
	
	@Autowired
    private UserMapper userMapper;

	@ApiOperation(value="获取用户信息",notes="根据用户ID获取")
	@ApiImplicitParams({
	  @ApiImplicitParam(name = "token", value = "url参数携带token"),
	  @ApiImplicitParam(name = "userId", value = "由zuul根据token自动填写", dataType = "String")
	})
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
