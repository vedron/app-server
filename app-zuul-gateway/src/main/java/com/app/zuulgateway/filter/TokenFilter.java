package com.app.zuulgateway.filter;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.common.exception.AppException;
import com.app.common.message.StatusCode;
import com.app.common.redis.RedisKeyPrefix;
import com.app.common.redis.RedisUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class TokenFilter extends ZuulFilter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

	private final String TOKEN_NAME = "token";

	@Autowired
	private RedisUtil redisService;
	
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0; //数值越小优先级越高
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
    	RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String uri = request.getRequestURI();
		logger.info("请求接口 URI：" + uri);
		
		String token = request.getParameter(TOKEN_NAME);
        if (token == null) {
        	if (uri.contains("login/getVerifyCode") || uri.contains("login/loginByVerifyCode")){
                return ctx;
            }
        	
        	if (uri.contains("/v2/api-docs")) {
        		return ctx;
        	}

        	logger.warn("token is empty");
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            return null;
        } else {
        	String userId = redisService.hGet(RedisKeyPrefix.USER_TOKEN, token);
			if (userId == null) {
				throw new AppException(StatusCode.UNAUTHORIZED);
			} else {
				redisService.put(RedisKeyPrefix.USER_TOKEN, token, userId, 259200L);
			}
			
        	ctx.addZuulRequestHeader("userId", userId);
        	logger.info("token: " + token + ", userId: " + userId);
        }
        
        return null;
    }
}

