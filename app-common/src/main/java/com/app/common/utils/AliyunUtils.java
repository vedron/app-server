package com.app.common.utils;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.push.model.v20160801.PushMessageToAndroidRequest;
import com.aliyuncs.push.model.v20160801.PushMessageToAndroidResponse;
import com.aliyuncs.push.model.v20160801.PushMessageToiOSRequest;
import com.aliyuncs.push.model.v20160801.PushMessageToiOSResponse;
import com.aliyuncs.push.model.v20160801.PushNoticeToAndroidRequest;
import com.aliyuncs.push.model.v20160801.PushNoticeToAndroidResponse;
import com.aliyuncs.push.model.v20160801.PushNoticeToiOSRequest;
import com.aliyuncs.push.model.v20160801.PushNoticeToiOSResponse;
import com.aliyuncs.push.model.v20160801.PushRequest;
import com.aliyuncs.push.model.v20160801.PushResponse;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;

public class AliyunUtils {
	protected final static Logger logger = LoggerFactory.getLogger(AliyunUtils.class);
	// 产品名称:云通信短信API产品,开发者无需替换
	static final String product = "Dysmsapi";
	// 产品域名,开发者无需替换
	static final String domain = "dysmsapi.aliyuncs.com";

	// TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
	static final String accessKeyId = "LTAIxwZQ322IWdI7";
	static final String accessKeySecret = "r8s2NU9zMtYQDXj9647yFrOy6m7FjZ";

	private static final String defaultAppKey = "24587519";
	private static final String iosUserAppKey = "24880651";
	private static final String androidUserAppKey = "24881022";
	
	protected static IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);

	protected static final DefaultAcsClient client = new DefaultAcsClient(profile);
	
	public static boolean sendMsgCode(String phone, String code) {
		// TODO Auto-generated method stub

		logger.info("sendMsgCode:" + phone + ":" + code);

		// 可自助调整超时时间
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");

		// 初始化acsClient,暂不支持region化
		// IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou",
		// accessKeyId, accessKeySecret);
		try {
			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		IAcsClient acsClient = new DefaultAcsClient(profile);

		// 组装请求对象-具体描述见控制台-文档部分内容
		SendSmsRequest request = new SendSmsRequest();
		// 必填:待发送手机号
		request.setPhoneNumbers(phone);
		// 必填:短信签名-可在短信控制台中找到
		request.setSignName("OOL租售");
		// 必填:短信模板-可在短信控制台中找到
		request.setTemplateCode("SMS_97050057");
		// 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		request.setTemplateParam("{\"code\":\"" + code + "\"}");

		// hint 此处可能会抛出异常，注意catch
		SendSmsResponse sendSmsResponse;
		try {
			sendSmsResponse = acsClient.getAcsResponse(request);
			if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
				return true;
			}
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return false;
	}

	public static void pushMessageToAndroidUserByAccount(String account, String title, String body, String appKey) throws Exception {
		PushMessageToAndroidRequest androidRequest = new PushMessageToAndroidRequest();
		// 安全性比较高的内容建议使用HTTPS
		androidRequest.setProtocol(ProtocolType.HTTPS);
		// 内容较大的请求，使用POST请求
		androidRequest.setMethod(MethodType.POST);
		androidRequest.setAppKey(Long.valueOf(appKey));
		androidRequest.setTarget("ACCOUNT");
		androidRequest.setTargetValue(account);
		androidRequest.setTitle(title);
		androidRequest.setBody(body);
		PushMessageToAndroidResponse pushMessageToAndroidResponse = client.getAcsResponse(androidRequest);
		logger.info("pushMessageToAndroidUserByAccount:" + "RequestId(" + pushMessageToAndroidResponse.getRequestId()
				+ ") MessageId(" + pushMessageToAndroidResponse.getMessageId() + ")");
	}

	public static void pushNoticeToAndroidUserByAccount(String account, String title, String body, String appKey) throws Exception {
		PushRequest pushRequest = new PushRequest();
		pushRequest.setAppKey(Long.parseLong(appKey));
        pushRequest.setTarget("ACCOUNT"); //推送目标: DEVICE:按设备推送 ALIAS : 按别名推送 ACCOUNT:按帐号推送  TAG:按标签推送; ALL: 广播推送
        pushRequest.setTargetValue(account); //根据Target来设定，如Target=DEVICE, 则对应的值为 设备id1,设备id2. 多个值使用逗号分隔.(帐号与设备有一次最多100个的限制)
        pushRequest.setPushType("NOTICE"); // 消息类型 MESSAGE NOTICE
        pushRequest.setDeviceType("ALL"); // 设备类型 ANDROID iOS ALL.
        // 推送配置
        pushRequest.setTitle(title); // 消息的标题
        pushRequest.setBody(body); // 消息的内容
        pushRequest.setAndroidNotifyType("NONE");
        pushRequest.setAndroidNotificationBarType(1);//通知栏自定义样式0-100
        pushRequest.setAndroidNotificationBarPriority(1);//通知栏自定义样式0-100
        pushRequest.setAndroidOpenType("NONE"); //点击通知后动作 "APPLICATION" : 打开应用 
        pushRequest.setStoreOffline(true);
        pushRequest.setAndroidNotificationChannel("1");
		
        PushResponse pushResponse = client.getAcsResponse(pushRequest);
        System.out.printf("RequestId: %s, MessageID: %s\n",
        pushResponse.getRequestId(), pushResponse.getMessageId());
        
		logger.info("PushNoticeToAndroidResponse:" + "RequestId(" + pushResponse.getRequestId()
				+ ") MessageId(" + pushResponse.getMessageId() + ")");
	}

	public static void pushNoticeToAllAndroidUser(String title, String body, String appKey) throws Exception {
		PushNoticeToAndroidRequest androidRequest = new PushNoticeToAndroidRequest();
		// 安全性比较高的内容建议使用HTTPS
		androidRequest.setProtocol(ProtocolType.HTTPS);
		// 内容较大的请求，使用POST请求
		androidRequest.setMethod(MethodType.POST);
		androidRequest.setAppKey(Long.valueOf(appKey));
		androidRequest.setTarget("ALL");
		androidRequest.setTargetValue("ALL");
		androidRequest.setTitle(title);
		androidRequest.setBody(body);
		PushNoticeToAndroidResponse pushMessageToAndroidResponse = client.getAcsResponse(androidRequest);
		logger.info("pushMessageToAndroidUserByAccount:" + "RequestId(" + pushMessageToAndroidResponse.getRequestId()
				+ ") MessageId(" + pushMessageToAndroidResponse.getMessageId() + ")");
	}

	public static void pushMessageToIosUserByAccount(String account, String title, String body, String appKey) throws Exception {
		PushMessageToiOSRequest iOSRequest = new PushMessageToiOSRequest();
		// 安全性比较高的内容建议使用HTTPS
		iOSRequest.setProtocol(ProtocolType.HTTPS);
		// 内容较大的请求，使用POST请求
		iOSRequest.setMethod(MethodType.POST);
		iOSRequest.setAppKey(Long.valueOf(appKey));
		iOSRequest.setTarget("ACCOUNT");
		iOSRequest.setTargetValue(account);
		iOSRequest.setTitle(title);
		iOSRequest.setBody(body);
		PushMessageToiOSResponse pushNoticeToiOSResponse = client.getAcsResponse(iOSRequest);
		logger.info("pushMessageToIosUserByAccount:" + "RequestId(" + pushNoticeToiOSResponse.getRequestId()
				+ ") MessageId(" + pushNoticeToiOSResponse.getMessageId() + ")");
	}

	public static void pushNoticeToIosUserByAccount(String account, String title, String body, String appKey) throws Exception {
		PushNoticeToiOSRequest iOSRequest = new PushNoticeToiOSRequest();
		// 安全性比较高的内容建议使用HTTPS
		iOSRequest.setProtocol(ProtocolType.HTTPS);
		// 内容较大的请求，使用POST请求
		iOSRequest.setMethod(MethodType.POST);
		iOSRequest.setAppKey(Long.valueOf(appKey));
		iOSRequest.setTarget("ACCOUNT");
		iOSRequest.setTargetValue(account);
		iOSRequest.setTitle(title);
		iOSRequest.setBody(body);
		iOSRequest.setApnsEnv("PRODUCT");//DEV表示开发环境，PRODUCT表示生产环境
		PushNoticeToiOSResponse pushNoticeToiOSResponse = client.getAcsResponse(iOSRequest);
		logger.info("pushNoticeToIosUserByAccount:" + "RequestId(" + pushNoticeToiOSResponse.getRequestId()
				+ ") MessageId(" + pushNoticeToiOSResponse.getMessageId() + ")");
	}

	public static void pushNoticeToAllIosUser(String title, String body, String appKey) throws Exception {
		PushNoticeToiOSRequest iOSRequest = new PushNoticeToiOSRequest();
		// 安全性比较高的内容建议使用HTTPS
		iOSRequest.setProtocol(ProtocolType.HTTPS);
		// 内容较大的请求，使用POST请求
		iOSRequest.setMethod(MethodType.POST);
		iOSRequest.setAppKey(Long.valueOf(appKey));
		iOSRequest.setTarget("ALL");
		iOSRequest.setTargetValue("ALL");
		iOSRequest.setTitle(title);
		iOSRequest.setBody(body);
		iOSRequest.setApnsEnv("DEV");
		PushNoticeToiOSResponse pushNoticeToiOSResponse = client.getAcsResponse(iOSRequest);
		logger.info("pushNoticeToAllIosUser:" + "RequestId(" + pushNoticeToiOSResponse.getRequestId() + ") MessageId("
				+ pushNoticeToiOSResponse.getMessageId() + ")");
	}

	public static Map<String, Object> getAliyunStsToken() {
		String token_accessKeyId = "LTAIWiVpzjZltT3c";
		String token_accessKeySecret = "eqLpwvMaa8tUA4CNPdWk2qsP7aJCKQ";

		// RoleArn 需要在 RAM 控制台上获取
		String roleArn = "acs:ram::1986458631612961:role/oolsts";
		// RoleSessionName 是临时Token的会话名称，自己指定用于标识你的用户，主要用于审计，或者用于区分Token颁发给谁
		// 但是注意RoleSessionName的长度和规则，不要有空格，只能有'-' '.' '@' 字母和数字等字符
		// 具体规则请参考API文档中的格式要求
		String roleSessionName = "alice-001";

		// 此处必须为 HTTPS
		ProtocolType protocolType = ProtocolType.HTTPS;
		IClientProfile token_profile = DefaultProfile.getProfile("cn-hangzhou", token_accessKeyId,
				token_accessKeySecret);
		DefaultAcsClient token_client = new DefaultAcsClient(token_profile);
		AssumeRoleRequest request = new AssumeRoleRequest();
		request.setVersion("2015-04-01");
		request.setMethod(MethodType.POST);
		request.setProtocol(protocolType);
		request.setRoleArn(roleArn);
		request.setRoleSessionName(roleSessionName);
		AssumeRoleResponse response = null;
		try {
			response = token_client.getAcsResponse(request);

		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (response == null) {
			return null;
		}
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("expiration", response.getCredentials().getExpiration());
		ret.put("token", response.getCredentials().getSecurityToken());
		ret.put("id", response.getCredentials().getAccessKeyId());
		ret.put("secret", response.getCredentials().getAccessKeySecret());
		return ret;

	}

	public static void pushMessage(String deviceId, String phone, String deviceType, String title, String body, String appKey)
			throws Exception {
		if (deviceType.equals("ANDROID")) {
			AliyunUtils.pushMessageToAndroidUserByAccount(deviceId, title, body, appKey);
		}

		if (deviceType.equals("IOS")) {
			AliyunUtils.pushMessageToIosUserByAccount(deviceId, title, body, appKey);
		}
	}

	public static void pushNotice(String deviceId,  String phone,String deviceType, String title, String body, String appKey)
			throws Exception {
		if (deviceType.equals("ANDROID")) {
			AliyunUtils.pushNoticeToAndroidUserByAccount(deviceId, title, body, appKey);
		}

		if (deviceType.equals("IOS")) {
			AliyunUtils.pushNoticeToIosUserByAccount(deviceId, title, body, appKey);
		}
	}
}
