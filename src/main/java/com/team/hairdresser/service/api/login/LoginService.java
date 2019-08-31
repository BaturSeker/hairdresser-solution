package com.team.hairdresser.service.api.login;

import com.team.hairdresser.domain.Users;
import com.team.hairdresser.dto.login.LoginRequestDto;

public interface LoginService {

    Users loggedIn(LoginRequestDto loginRequestDto) throws Exception;

    Users loggedInLDAP(LoginRequestDto loginRequestDto) throws Exception;

}

