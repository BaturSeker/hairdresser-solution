package com.team.hairdresser.dto.login;

import com.team.hairdresser.dto.authority.AuthorityResponse;

import java.util.List;

public class LoginResponseDto {
    private LoginUserResponseDto loginUserResponseDto;
    private List<AuthorityResponse> authorityResponse;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<AuthorityResponse> getAuthorityResponse() {
        return authorityResponse;
    }

    public void setAuthorityResponse(List<AuthorityResponse> authorityResponse) {
        this.authorityResponse = authorityResponse;
    }

    public LoginUserResponseDto getLoginUserResponseDto() {
        return loginUserResponseDto;
    }

    public void setLoginUserResponseDto(LoginUserResponseDto loginUserResponseDto) {
        this.loginUserResponseDto = loginUserResponseDto;
    }
}
