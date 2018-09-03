package com.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.app.entity.dto.LoginDto;

@Service
public class LoginService {

    @Autowired
    RestTemplate restTemplate;
    
    private final String URL_PREFIX = "http://APP-SERVICE/";

    public String checkPassword(LoginDto dto) {
        JSONObject postData = new JSONObject();
        postData.put("descp", "request for post");
        JSONObject json = restTemplate.postForEntity(URL_PREFIX + "checkPassword", postData, JSONObject.class).getBody();
        return json.toJSONString();
    }

}
