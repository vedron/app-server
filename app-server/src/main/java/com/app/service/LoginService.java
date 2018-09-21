package com.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.app.entity.Resp;
import com.app.entity.dto.LoginReqDto;
import com.app.entity.dto.VerifyCodeReqDto;

@Service
public class LoginService {

    @Autowired
    RestTemplate restTemplate;
    
    private final String URL_PREFIX = "http://APP-SERVICE/";

    public Resp getVerifyCode(VerifyCodeReqDto dto) {
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	HttpEntity<VerifyCodeReqDto> entity = new HttpEntity<VerifyCodeReqDto>(dto, headers);
    	ResponseEntity<Resp> resp = restTemplate.postForEntity(URL_PREFIX + "login/getVerifyCode", entity, Resp.class);
        return resp.getBody();
    }
    
    public Resp loginByVerifyCode(LoginReqDto dto) {
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	HttpEntity<LoginReqDto> entity = new HttpEntity<LoginReqDto>(dto, headers);
    	ResponseEntity<Resp> resp = restTemplate.postForEntity(URL_PREFIX + "login/loginByVerifyCode", entity, Resp.class);
        return resp.getBody();
    }

}
