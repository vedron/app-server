package com.app.server.aopAspect;

import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.app.common.exception.AppException;
import com.app.common.exception.BindingResultException;
import com.app.common.message.Resources;
import com.app.common.message.StatusCode;
import com.app.common.redis.RedisKeyPrefix;
import com.app.common.redis.RedisUtil;
import com.app.common.security.IgnoreSecurity;
import com.app.common.security.LoginCompatibleSecurity;
import com.app.entity.Resp;
/**
 * Desc: 用于检查 token 的切面
 *
 */
@Aspect
@Component
public class SecurityAspect {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private String tokenName = "token";

	@Autowired
	private RedisUtil redisService;

	public String getTokenName() {
		return tokenName;
	}

	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;
	}
	
	@Around("execution(* com.app.server.controller.*.*(..))")
	public Object execute(ProceedingJoinPoint pjp) throws Throwable {

		// 从切点上获取目标方法
		MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
		Method method = methodSignature.getMethod();
		String token = null;

		logger.info("请求接口：" + pjp.getTarget().getClass().getName() + "/" + method.getName());

		// 登陆验证
		if (!method.isAnnotationPresent(IgnoreSecurity.class)) {
			token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getParameter(tokenName);

			if ((null == token || !redisService.exists(RedisKeyPrefix.App_Token + token))
					&& !method.isAnnotationPresent(LoginCompatibleSecurity.class)) {
				throw new AppException(StatusCode.UNAUTHORIZED);
			} else if (method.isAnnotationPresent(LoginCompatibleSecurity.class) && null != token
					&& !redisService.exists(RedisKeyPrefix.App_Token + token)) {
				throw new AppException(StatusCode.UNAUTHORIZED);
			} else {
				if (token != null) {
					redisService.flushExpireTime(RedisKeyPrefix.App_Token + token,
							Long.parseLong(redisService.hGet(RedisKeyPrefix.System_Params, "app_token_expire_second")));
				}
			}

		}

		BindingResult bindingResult = null;

		for (Object arg : pjp.getArgs()) {
			// 判断参数
			if (arg instanceof BindingResult) {
				bindingResult = (BindingResult) arg;
				if (bindingResult.hasErrors()) {
					StringBuilder msg = new StringBuilder();
					List<ObjectError> errors = bindingResult.getAllErrors();
					for (ObjectError error : errors) {
						msg.append(error.getDefaultMessage());
						msg.append("#");
					}

					throw new BindingResultException(msg.toString());
				}
			} else if (!(arg instanceof MultipartFile) && !(arg instanceof HttpServletRequest)
					&& !(arg instanceof HttpServletResponse)) {
				// 判断参数入参带上用户ID
				if (!method.isAnnotationPresent(IgnoreSecurity.class)
						&& ((method.isAnnotationPresent(LoginCompatibleSecurity.class) && token != null)
								|| !method.isAnnotationPresent(LoginCompatibleSecurity.class))) {
					if (!getUserId(arg, token)) {
						throw new AppException(StatusCode.UNAUTHORIZED);
					}
				}

				if (arg instanceof MultipartFile){
					logger.info("请求报文：[Name]" + ((MultipartFile)arg).getName()
							+ ", [OriginalFilename]" + ((MultipartFile)arg).getOriginalFilename()
							+ ", [Size]" + ((MultipartFile)arg).getSize()
							+ ", [ContentType]" + ((MultipartFile)arg).getContentType());
				} else {
					logger.info("请求报文：" + JSONObject.toJSONString(arg));
				}
			}
		}
		// 调用目标方法
		return pjp.proceed();
	}

	public void afterReturning(JoinPoint joinPoint, Resp resp) {
		// 国际化
		if (resp.getCode() != 200) {
			try {
				String msg = Resources.getMessage(resp.getCode() + "");

				if (msg.contains("?") && resp.getDatas() != null) {
					String newMsg = msg.replace("?", resp.getDatas().toString());
					resp.setDatas(null);
					resp.setMsg(newMsg);
				} else {
					resp.setMsg(msg);
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		logger.info(JSONObject.toJSONString(resp));
	}

	private boolean getUserId(Object arg, String token) {
		// 国际化
		String userId = redisService.get(token);
		// 反射userId到对象中
		if (null != userId) {
			// 在此可以黑名单校验
			
			try {
				arg.getClass().getMethod("setUserId", Long.class).invoke(arg, Long.parseLong(userId));
			} catch (NoSuchMethodException e) {
				return true;
			} catch (Exception e) {
				return false;
			}
			return true;
		}

		return false;
	}
}
