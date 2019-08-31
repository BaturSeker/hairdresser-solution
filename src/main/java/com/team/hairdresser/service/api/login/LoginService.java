package com.team.hairdresser.service.api.login;

import com.team.hairdresser.domain.UserEntity;
import com.team.hairdresser.dto.login.LoginRequestDto;

public interface LoginService {

    UserEntity login(LoginRequestDto loginRequestDto) throws Exception;

    UserEntity loginLDAP(LoginRequestDto loginRequestDto) throws Exception;

}

