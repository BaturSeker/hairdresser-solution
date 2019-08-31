package com.team.hairdresser.dto.login;

import com.team.hairdresser.dto.authority.AuthorityResponseDto;

import java.util.List;

public class LoginResponseDto {
    private LoginUserResponseDto loginUserResponseDto;
    private List<AuthorityResponseDto> authorityResponseDto;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<AuthorityResponseDto> getAuthorityResponseDto() {
        return authorityResponseDto;
    }

    public void setAuthorityResponseDto(List<AuthorityResponseDto> authorityResponseDto) {
        this.authorityResponseDto = authorityResponseDto;
    }

    public LoginUserResponseDto getLoginUserResponseDto() {
        return loginUserResponseDto;
    }

    public void setLoginUserResponseDto(LoginUserResponseDto loginUserResponseDto) {
        this.loginUserResponseDto = loginUserResponseDto;
    }
}
