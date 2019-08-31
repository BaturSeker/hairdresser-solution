package com.team.hairdresser.service.api.login;

import com.team.hairdresser.domain.UserEntity;
import com.team.hairdresser.dto.login.LoginRequestDto;

public interface LoginService {

    UserEntity loggedIn(LoginRequestDto loginRequestDto) throws Exception;

    UserEntity loggedInLDAP(LoginRequestDto loginRequestDto) throws Exception;

}

