package com.team.hairdresser.service.impl.login;


import com.team.hairdresser.constant.ValidationMessages;
import com.team.hairdresser.domain.Users;
import com.team.hairdresser.dto.login.LoginRequestDto;
import com.team.hairdresser.service.api.login.LoginRules;
import com.team.hairdresser.service.api.login.LoginService;
import com.team.hairdresser.service.api.password.PasswordRules;
import com.team.hairdresser.utils.util.ValidationHelper;
import com.team.hairdresser.utils.util.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class LoginRulesImpl implements LoginRules {

    private LoginService loginService;
    private PasswordRules passwordRules;

    @Override
    public Users login(LoginRequestDto loginRequestDto) throws Exception {

        StringBuilder message = new StringBuilder();
        boolean isValid = true;
        if (!ValidationHelper.notEmpty(loginRequestDto.getPassword())) {
            isValid = false;
            message.append(ValidationMessages.LOGIN_PASSWORD_NOT_NULL);
            message.append(System.lineSeparator());
        }
        if (!ValidationHelper.notEmpty(loginRequestDto.getUsername())) {
            isValid = false;
            message.append(ValidationMessages.LOGIN_USER_NAME_NOT_NULL);
            message.append(System.lineSeparator());
        }
        if (!isValid) {
            throw new ValidationException(message.toString());
        }
        return loginService.loggedIn(loginRequestDto);
    }


    @Override
    public Users loginLDAP(LoginRequestDto loginRequestDto) throws Exception {

        StringBuilder message = new StringBuilder();
        boolean isValid = true;
        if (!ValidationHelper.notEmpty(loginRequestDto.getPassword())) {
            isValid = false;
            message.append(ValidationMessages.LOGIN_PASSWORD_NOT_NULL);
            message.append(System.lineSeparator());
        }
        if (!ValidationHelper.notEmpty(loginRequestDto.getUsername())) {
            isValid = false;
            message.append(ValidationMessages.LOGIN_USER_NAME_NOT_NULL);
            message.append(System.lineSeparator());
        }
        if (!isValid) {
            throw new ValidationException(message.toString());
        }
        return loginService.loggedInLDAP(loginRequestDto);
    }

    @Autowired
    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

    @Autowired
    public void setPasswordRules(PasswordRules passwordRules) {
        this.passwordRules = passwordRules;
    }
}


