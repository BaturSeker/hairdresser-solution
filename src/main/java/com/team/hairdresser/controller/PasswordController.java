package com.team.hairdresser.controller;


import com.team.hairdresser.constant.SuccessMessages;
import com.team.hairdresser.dto.SuccessResponseDto;
import com.team.hairdresser.dto.password.SetNewPasswordRequestDto;
import com.team.hairdresser.dto.user.UserRequestDto;
import com.team.hairdresser.service.api.password.PasswordService;
import com.team.hairdresser.utils.util.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/rest/password/")
public class PasswordController {

    private PasswordService passwordService;

    @GetMapping(value = "getPassword")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity getPassword() {
        PasswordGenerator passwordGenerator = new PasswordGenerator();
        String password = null;
        password = passwordGenerator.generate(10);
        while (!passwordService.isPasswordValid(password)) {
            password = passwordGenerator.generate(10);
        }
        return new ResponseEntity<>(password, HttpStatus.OK);
    }

    @PostMapping(value = "validatePassword")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity validatePassword(@Valid @RequestBody UserRequestDto userRequestDto) {
        passwordService.passwordValidation(userRequestDto);
        return new ResponseEntity<>(new SuccessResponseDto(SuccessMessages.VALIDATE_PASSWORD_TITLE, SuccessMessages.VALIDATE_PASSWORD_MESSAGE), HttpStatus.OK);

    }

    @PutMapping(value = "setNewPassword/{userId}")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity setNewPassword(@PathVariable Long userId, @Valid @RequestBody SetNewPasswordRequestDto setNewPasswordRequestDto) throws NoSuchAlgorithmException {
        setNewPasswordRequestDto.setUserId(userId);
        passwordService.setNewPassword(setNewPasswordRequestDto);
        return new ResponseEntity<>(new SuccessResponseDto(SuccessMessages.NEW_PASSWORD_TITLE, SuccessMessages.NEW_PASSWORD_MESSAGE), HttpStatus.OK);
    }

    @GetMapping(value = "resetPassword")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity resetPassword(@RequestParam("userId") Long userId) throws NoSuchAlgorithmException {
        passwordService.resetPassword(userId);
        return new ResponseEntity<>(new SuccessResponseDto(SuccessMessages.RESET_PASSWORD_TITLE, SuccessMessages.RESET_PASSWORD_MESSAGE), HttpStatus.OK);
    }

    @Autowired
    public void setPasswordService(PasswordService passwordService) {
        this.passwordService = passwordService;
    }
}

