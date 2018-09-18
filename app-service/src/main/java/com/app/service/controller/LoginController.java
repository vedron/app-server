package com.app.service.controller;

import javax.validation.Valid;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.common.message.StatusCode;
import com.app.common.redis.RedisKeyPrefix;
import com.app.common.redis.RedisUtil;
import com.app.common.security.CodecUtil;
import com.app.common.utils.StringUtils;
import com.app.dao.UserMapper;
import com.app.entity.Resp;
import com.app.entity.dto.LoginReqDto;
import com.app.entity.dto.LoginRspDto;
import com.app.entity.dto.VerifyCodeReqDto;
import com.app.entity.dto.VerifyCodeRspDto;
import com.app.service.ILoginService;


@RestController
@RequestMapping("/login")
public class LoginController implements ILoginService {

    private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private RedisUtil redisService;
	
	@Autowired
    private UserMapper userMapper;

	@Override
	@RequestMapping(value = "/getVerifyCode", method = RequestMethod.POST)
	public Resp getVerifyCode(@RequestBody @Valid VerifyCodeReqDto dto) {
    	log.info("getVerifyCode phone: " + dto.getPhone());
    	if (dto.getPhone() == null || "".equals(dto.getPhone())) {
    		return new Resp(StatusCode.BAD_REQUEST.value());
    	}
    	if (redisService.exists(RedisKeyPrefix.VERIFY_CODE + dto.getPhone())) {
    		return new Resp(StatusCode.VERIFY_CODE_BUSY.value());
    	}
    	
    	VerifyCodeRspDto rspBody = new VerifyCodeRspDto();
    	rspBody.setVerifyCode(StringUtils.createRandomVcode());

    	redisService.set(RedisKeyPrefix.VERIFY_CODE + dto.getPhone(), rspBody.getVerifyCode(), 300L);
		return new Resp(rspBody);
	}

	@Override
	@RequestMapping(value = "/verifyCode", method = RequestMethod.POST)
	public Resp verifyCode(@RequestBody @Valid LoginReqDto dto) {
    	log.info("verifyCode phone: " + dto.getPhone());
    	
    	if (! redisService.exists(RedisKeyPrefix.VERIFY_CODE + dto.getPhone())) {
    		return new Resp(StatusCode.UNAUTHORIZED.value());
    	}

    	LoginRspDto rspBody = new LoginRspDto();
    	rspBody.setToken(CodecUtil.createToken());
    	
    	// 用户不存在，则新增
    	String strUserId = redisService.hGet(RedisKeyPrefix.USER_PHONE_TO_ID, dto.getPhone());
    	if (strUserId == null || "".equals(strUserId) ) {
    		Long userId = null;
    		userMapper.createUser(userId, dto.getPhone(), dto.getDeviceType(), dto.getDeviceId());
    	}

        return new Resp(rspBody);
    }
}
