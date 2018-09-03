package com.app.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;

public class AlipayUtils {
	public static final String ALIPAY_APPID = "2017080908103933";
	public static final String ALIPAY_APP_PRIVATE_KEY =
	 "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCkO8xs4hu0ySjYW8ZMg3sQitJc51nK4oNcPMoOaaIvwE4n9CEvbENmngLXwt0FLXyqHBGziBsuXzCc4idv7jhgbHd32tF1dEsUvEFdVpW3kh5vyi73sCm4wRn0Uq3IuRAoQz7qEb2DPkRyMOm12HNZIsCApxUi0WhmBrx4qC8QnvfQZ+LL54o8tItq79VzN8S4KFHQQ1GH7PO3Osk35vWobdJeZ378JZqShDurMdqnsAvIxt0Uj9F0EeksbdKXgrbkZTq2KsD2C/y8kdxrDFK3N+dnVyOEJrjk9vKEM0i4dY37880tqNe0ud6z79674onMszVZ2LLdHadfgtpemkz/AgMBAAECggEAPIL38EpjMPpJPWabKsfrCQ5NMgPf9rv88HhJboQHSRbO9TOjIoj+UIn5vCvFKco3u6m57+8/LJfQHo3JQBmC1ixA3H1YXbFqlIpEfzKO2WNSKZhSM9rJlJQ5jucDtwhQ5rTYMoA01m2T5IsXIXROE9KVIRHu3LRRhf4HZWF6beG9RyjsvK++PjBb3ni1gFMUizn34DlPL0+eWOU+bBoLlxYSns3w8fCnvMS/Iz1faWm8sS3gf6UNLOHdrhtaihMLk8EmHCm0k7Q+lRyWqCQPKX8fwpgx6f4mR3czOdtYi9TUGH21KcLUJ6WHDBJnWLqinh9zrF4C6SQZqi977Y9eiQKBgQDZy3ljVqWBqZClTdYI+PbfLJkQoj/ODL3WOVddOCwqcySyvZly2O3elgv6BCBz8ullmOlEqJdrHoEcCGvBJXmVLEnTyaYRH3ybm9kmm5fwbMFFujtXKHjiVv99+BHs0pgaeMxvnVdddz0+XUYjZrzILbalFmS9hV9cYSFz/2IDfQKBgQDBCwvzWYTLGJakC6TqOcSCCLfbr/UJtAA0xz7/CpH9sTPGV3d8p64TAuZd9Zml0g5usBOwWCZcMx7DXbvxAp2qaM8aUrXZj+wxzEQtrSk5neKpFEZGJhjkOWmpDqGtlRYjsvg73dEnsKgjmlGIEmN51tth3JO9Si8Ag3OqXEJDKwKBgG3y/7P7oRAlRyV8duoOraqa/CbekfF2kzubtRQVcI55lHQ7rUTCaMtu2lr8TFncrqEgodqMdeyY+LrrMWLPhWKeeiLV8OVCDQkNk4GfGQRCEwwdaSml/+jb3J1+Z6Yz5p+ujh5mwx0nPh7GvcrPacxw73zGIlNfCnY7fsTtj1CNAoGBAI2Vj49NMeNPxnTNAYi1E9aXlFnxhRQ3zArps9+VHhxmkmW/kyh/R2I0N0fksWt4VBOE2vv0uzXWXJLHted/lqn8syFIVo4aTA7+FWc24Mycazi9YQlkOc9x+HgA/j304ccPJZerChNFMxzhuzjE2+bp2f+9qzIjLbP/tZ7Widb/AoGAJFZRoe4n+DH6yxKH0S9HggnMKh45Lv2oyvucRg6Oze8rFY0I6V8K8LLipHKgf5YhDIZ96UkhJA3SMQOWNihtl4IYDEBPQSO559Z9P2RU/T1bON2x6ZqzGj6xQSskjGoOoujoTDULnbgKYItg7wBhYR62cbY/tB/ZDGKwn8LMKnk=";
	public static final String ALIPAY_APP_PUBLIC_KEY =
	 "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApDvMbOIbtMko2FvGTIN7EIrSXOdZyuKDXDzKDmmiL8BOJ/QhL2xDZp4C18LdBS18qhwRs4gbLl8wnOInb+44YGx3d9rRdXRLFLxBXVaVt5Ieb8ou97ApuMEZ9FKtyLkQKEM+6hG9gz5EcjDptdhzWSLAgKcVItFoZga8eKgvEJ730Gfiy+eKPLSLau/VczfEuChR0ENRh+zztzrJN+b1qG3SXmd+/CWakoQ7qzHap7ALyMbdFI/RdBHpLG3Sl4K25GU6tirA9gv8vJHcawxStzfnZ1cjhCa45PbyhDNIuHWN+/PNLajXtLnes+/eu+KJzLM1Wdiy3R2nX4LaXppM/wIDAQAB";
	public static final String ALIPAY_PUBLIC_KEY =
	 "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgqT2y4k+8Q+BUrLzzjeAq1vpB3Etf0mOQprGejXNJMhPXk7GeJx0HlQ7BiQOBg8r3nFZj9Q4vh4bdBATnxmWpMXOWEyzguEOxv4idQ67UjSLUgfee7lqLVgq4TVwPvwd39gwv5oXxc9L4dUSk1NdN+uIzAvLDY83VT4gex8BMKvqp5DapDfV5Z1psN89t+tuLV9gCNiO7iclu1mOFmBDSeC8X/cUUFRXGpkiOdOVrZhBVfpIF6QrpHYioxB8bIJ5cIpNN6anfn6OR6dzdP1FhU0sNrg/MtsXMW+shyfvTdxQcpyYxbqpg99uFG03Cd2eNs3Cjy8xfht37lCCPmxrYQIDAQAB";
	public static final String ALIPAY_CHARSET = "utf-8";
	public static final String ALIPAY_NOTIFY_URL = "http://test.ool.vc/chargerbaby-app-server/pay/alipayPayNotify";
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	public static String alipay(String payMoney, String title, String passback) {
		// 实例化客户端
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
				ALIPAY_APPID, ALIPAY_APP_PRIVATE_KEY, "json",
				ALIPAY_CHARSET, ALIPAY_APP_PUBLIC_KEY, "RSA2");
		// 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		// SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		try {
			model.setPassbackParams(URLEncoder.encode(passback, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		; // 描述信息 添加附加数据
		model.setSubject(title); // 商品标题
		model.setOutTradeNo(StringUtils.createOrderSerialNumber()); // 商家订单编号
		model.setTimeoutExpress("30m"); // 超时关闭该订单时间
		model.setTotalAmount(payMoney); // 订单总金额
		model.setProductCode("QUICK_MSECURITY_PAY"); // 销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY
		request.setBizModel(model);
		request.setNotifyUrl(ALIPAY_NOTIFY_URL); // 回调地址
		String orderStr = "";
		AlipayTradeAppPayResponse response;
		try {
			response = alipayClient.sdkExecute(request);
			orderStr = response.getBody();
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return orderStr;
	}

	public static boolean checkSign(Map<String, String> params) {
		boolean ret = false;
		try {
			ret = AlipaySignature.rsaCheckV1(params, ALIPAY_PUBLIC_KEY,
					ALIPAY_CHARSET, "RSA2");
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
}
