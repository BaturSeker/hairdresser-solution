package com.team.hairdresser.service.impl.token;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class TokenInjectInterceptor implements ClientHttpRequestInterceptor {

    private String token;

    public TokenInjectInterceptor(String token) {
        this.token = token;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        String bearerPrefix = "Bearer ";
        if (!token.startsWith(bearerPrefix)) {
            token = bearerPrefix + token;
        }
        headers.add("Authorization", token);
        return execution.execute(request, body);
    }
}
