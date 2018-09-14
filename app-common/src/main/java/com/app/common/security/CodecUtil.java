package com.app.common.security;


import java.util.UUID;

import org.apache.commons.text.RandomStringGenerator;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class CodecUtil {
//	private static Logger logger = LoggerFactory.getLogger(CodecUtil.class);

    /**
     * 创建随机数
     */
    public static String createRandomNum(int count) {
    	RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('a', 'z').build();
    	return generator.generate(count);
        //return RandomStringGenerator.randomNumeric(count);
    }

    /**
     * 获取 UUID（32位）
     */
    public static String createUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }
    
    /**
     * 获取 TOKEN（36位）
     */
    public static String createToken() {
    	return CodecUtil.createUUID()+CodecUtil.createRandomNum(4).toUpperCase();
    }
}
