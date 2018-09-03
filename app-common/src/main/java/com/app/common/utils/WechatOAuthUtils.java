package com.app.common.utils;

public class WechatOAuthUtils {

	public static final  String WECHAT_APPID = "wx79b3a889e50b5077";
	
	public static String getRequestCodeUrl(String redirect_url) {
		return String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect",
				WECHAT_APPID, redirect_url, "snsapi_userinfo", "STATE");
	}
}
