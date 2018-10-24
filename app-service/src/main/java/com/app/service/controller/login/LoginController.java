package com.app.service.controller.login;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

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
import com.app.entity.dto.UserInfoDto;
import com.app.entity.dto.VerifyCodeReqDto;
import com.app.entity.dto.VerifyCodeRspDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@RestController
@Api(tags = "登录模块") 
@RequestMapping("/login")
public class LoginController{

    private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private RedisUtil redisService;
	
	@Autowired
    private UserMapper userMapper;
	
	@ApiOperation(value="获取验证码",notes="根据手机号码获取验证码，用于登录")
	@RequestMapping(value = "/getVerifyCode", method = RequestMethod.POST)
	public Resp getVerifyCode(@RequestBody @Valid VerifyCodeReqDto dto) {
    	log.info("getVerifyCode phone: " + dto.getPhone());
    	if (dto.getPhone() == null || "".equals(dto.getPhone())) {
    		return new Resp(StatusCode.BAD_REQUEST.value());
    	}
    	
    	String strCode = redisService.hGet(RedisKeyPrefix.VERIFY_CODE, dto.getPhone());
    	if (strCode != null && !"".equals(strCode)) {
    		return new Resp(StatusCode.VERIFY_CODE_BUSY.value());
    	}
    	
    	VerifyCodeRspDto rspBody = new VerifyCodeRspDto();
    	rspBody.setVerifyCode(StringUtils.createRandomVcode());

    	redisService.put(RedisKeyPrefix.VERIFY_CODE, dto.getPhone(), rspBody.getVerifyCode(), 300L);
		return new Resp(rspBody);
	}

	@ApiOperation(value="使用验证码登录",notes="用户登录")
	@RequestMapping(value = "/loginByVerifyCode", method = RequestMethod.POST)
	public Resp loginByVerifyCode(@RequestBody @Valid LoginReqDto dto) {
    	log.info("loginByVerifyCode phone: " + dto.getPhone());

    	String strCode = redisService.hGet(RedisKeyPrefix.VERIFY_CODE, dto.getPhone());
    	if (strCode == null && !dto.getCode().equals(strCode)) {
    		return new Resp(StatusCode.LOGIN_FAIL.value());
    	}
    	
    	redisService.hDel(RedisKeyPrefix.VERIFY_CODE, dto.getPhone());

    	LoginRspDto rspBody = new LoginRspDto();
    	rspBody.setToken(CodecUtil.createToken());
    	
    	// 用户不存在，则新增
    	String strUserId = redisService.hGet(RedisKeyPrefix.USER_PHONE_TO_ID, dto.getPhone());
    	if (strUserId == null || "".equals(strUserId) ) {
    		UserInfoDto user = new UserInfoDto();
    		user.setUserMobilephone(dto.getPhone());
    		user.setUserDeviceType(dto.getDeviceType());
    		user.setUserDeviceId(dto.getDeviceId());
    		if (userMapper.createUser(user) > 0) {
    			strUserId = String.valueOf(user.getUserId());
    			redisService.put(RedisKeyPrefix.USER_PHONE_TO_ID, dto.getPhone(), strUserId);
    		}
    	} else {
    		String oldToken = redisService.hGet(RedisKeyPrefix.USER_INFO_ + strUserId, "token");
    		redisService.hDel(RedisKeyPrefix.USER_TOKEN, oldToken);
    		userMapper.updateUser(Long.valueOf(strUserId), dto.getDeviceType(), dto.getDeviceId());
    	}

		Map<String, String> map = new HashMap<String, String>();
		map.put("userMobilephone", dto.getPhone());
		map.put("userDeviceType", dto.getDeviceType());
		map.put("userDeviceId", dto.getDeviceId());
		map.put("token", rspBody.getToken());
		redisService.hSet(RedisKeyPrefix.USER_INFO_ + strUserId, map, null);
    	redisService.put(RedisKeyPrefix.USER_TOKEN, rspBody.getToken(), strUserId, 259200L);
        return new Resp(rspBody);
    }
}
