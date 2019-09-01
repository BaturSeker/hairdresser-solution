package com.team.hairdresser.service.impl.token;


import com.team.hairdresser.config.RestTemplateResponseErrorHandler;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserTokenHolderServiceImpl {

    private String token;

    public RestTemplate getTokenizedRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        if (token != null) {
            restTemplate.setInterceptors(Collections.singletonList(new TokenInjectionInterceptor(token)));
            restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
        }
        return restTemplate;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

