package com.team.hairdresser.service.api.login;

import com.team.hairdresser.domain.Users;
import com.team.hairdresser.dto.login.LoginRequestDto;

public interface LoginRules {

    Users login(LoginRequestDto loginRequestDto) throws Exception;

    Users loginLDAP(LoginRequestDto loginRequestDto) throws Exception;

}