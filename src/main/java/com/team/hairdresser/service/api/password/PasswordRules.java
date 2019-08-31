package com.team.hairdresser.service.api.password;

import com.team.hairdresser.domain.UserEntity;
import com.team.hairdresser.dto.password.SetNewPasswordRequestDto;
import com.team.hairdresser.dto.user.UserRequestDto;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface PasswordRules {
    void passwordValidation(UserRequestDto userRequestDto);

    boolean isPasswordValid(String password);

    boolean isUsernamePasswordValid(String userName, String password);

    void setIsTemproraryPassword(UserEntity userEntity) throws NoSuchAlgorithmException;

    void setIsTemproraryPassword(List<UserEntity> users) throws NoSuchAlgorithmException;

    void setNewPassword(SetNewPasswordRequestDto setNewPasswordRequestDto) throws NoSuchAlgorithmException;

    void resetPassword(Long userId) throws NoSuchAlgorithmException;

    void basePasswordRules(String password);

    void checkSlangWord(String password);
}
