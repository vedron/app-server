package com.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.app.entity.dto.LoginDto;

@Service
public class LoginService {

    @Autowired
    RestTemplate restTemplate;
    
    private final String URL_PREFIX = "http://APP-SERVICE/";

    public String checkPassword(LoginDto dto) {
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	HttpEntity<LoginDto> entity = new HttpEntity<LoginDto>(dto, headers);
    	ResponseEntity<String> resp = restTemplate.postForEntity(URL_PREFIX + "login/checkPassword", entity, String.class);
        return resp.getBody();
    }

}
