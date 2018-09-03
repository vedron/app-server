package com.app.common.utils;

import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class StringUtils extends org.apache.commons.lang3.StringUtils{

	/**
     * 隐藏手机号 中间六位
     * @param phone
     * @return
     */
	public static String hidePhone(String phone) {
        return substring(phone, 0, 3) + "******" + substring(phone, phone.length()-2, phone.length());
    }
	
	public static boolean isMobilephone(String str) {
		return isChinaPhoneLegal(str) || isHKPhoneLegal(str);  
	}
	
	/** 
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数 
     * 此方法中前三位格式有： 
     * 13+任意数 
     * 15+除4的任意数 
     * 18+除1和4的任意数 
     * 17+除9的任意数 
     * 147 
     */  
    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {  
        String regExp = "^((13[0-9])|(15[0-9])|(18[0-9])|(17[0-8])|(147))\\d{8}$";  
        Pattern p = Pattern.compile(regExp);  
        Matcher m = p.matcher(str);  
        return m.matches();  
    }  
  
    /** 
     * 香港手机号码8位数，5|6|8|9开头+7位任意数 
     */  
    public static boolean isHKPhoneLegal(String str)throws PatternSyntaxException {  
        String regExp = "^(5|6|8|9)\\d{7}$";  
        Pattern p = Pattern.compile(regExp);  
        Matcher m = p.matcher(str);  
        return m.matches();  
    } 
    
    /**
     * 随机生成6位随机验证码
     */
    public static String createRandomVcode(){
        //验证码
        String vcode = "";
        for (int i = 0; i < 6; i++) {
            vcode = vcode + (int)(Math.random() * 9);
        }
        return vcode;
    }
    
    
    /**
     * 订单序列号
     */
    public static String createOrderSerialNumber() {
    	int hashCodeV = UUID.randomUUID().toString().hashCode();  
    	if(hashCodeV < 0) {
    		hashCodeV = - hashCodeV;
    	}
    	return DateUtils.toStr(new Date(), DateUtils.YYYYMMDDHHMMSS)+String.format("%010d", hashCodeV);
    }
}
