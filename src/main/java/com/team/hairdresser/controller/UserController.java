package com.team.hairdresser.controller;


import com.team.hairdresser.constant.SuccessMessages;
import com.team.hairdresser.domain.UserEntity;
import com.team.hairdresser.domain.UserRoleEntity;
import com.team.hairdresser.dto.SuccessResponseDto;
import com.team.hairdresser.dto.password.ResetPasswordDto;
import com.team.hairdresser.dto.user.UserInfoResponseDto;
import com.team.hairdresser.dto.user.UserResponseDto;
import com.team.hairdresser.service.api.authority.AuthorizationService;
import com.team.hairdresser.service.api.password.PasswordService;
import com.team.hairdresser.service.api.user.UserService;
import com.team.hairdresser.utils.pageablesearch.model.PageRequestDto;
import com.team.hairdresser.utils.pageablesearch.model.PageableSearchFilterDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("rest/user/")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private UserService userService;
    private PasswordService passwordService;
    private AuthorizationService authorizationService;

    @Autowired
    public UserController(UserService userService, PasswordService passwordService, AuthorizationService authorizationService) {
        this.userService = userService;
        this.passwordService = passwordService;
        this.authorizationService = authorizationService;
    }

    @GetMapping("{userId}")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity getUser(@PathVariable Long userId) {
        try {
            UserEntity user = userService.getUser(userId);
            UserResponseDto userResponseDto = new UserResponseDto();
            userResponseDto.setUsername(user.getUsername());
            userResponseDto.setName(user.getFirstname());
            userResponseDto.setSurname(user.getSurname());
            userResponseDto.setUserId(user.getId());
            userResponseDto.setTemporaryPassword(user.getPasswordTemporary());
            userResponseDto.setPhone(user.getTelephone());
            userResponseDto.setEmail(user.getEmail());
            userResponseDto.setActive(user.getActive());

            return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
        } catch (Exception e) {
            // TODO baseController handleException metodu silindi
            throw e;
        }
    }

    @GetMapping("index")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity getAllUsers() {
        List<UserEntity> userList = userService.getAllUsers();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @PostMapping("getAuthorizeList{userId}")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity getAuthorizeList(@Valid @RequestBody Long userId) {
        List<UserRoleEntity> userRoleEntityList = authorizationService.getAuthorizeList(userId);
        return new ResponseEntity<>(userRoleEntityList, HttpStatus.OK);
    }

    @GetMapping("getComboUsers")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity getComboUsers() {
        List userList = userService.getComboUsers();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping(value = "getAll")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity getAll() {
        return ResponseEntity.ok(userService.getUserInfoPage(PageRequest.of(0, 10000)).getContent());
    }

    @GetMapping(value = "getUser/{viewId}")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity getAll(@Valid @PathVariable Integer viewId) {
        return ResponseEntity.ok(userService.getUserInfoPage(PageRequest.of(0, 10000)).getContent().get(viewId - 1));
    }

    @PostMapping("getUserInfoPage")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity<Page<UserInfoResponseDto>> getUserInfoPage(@RequestBody PageRequestDto pageRequest) {
        return ResponseEntity.ok(userService.getUserInfoPage(PageRequest.of(pageRequest.getPage(), pageRequest.getSize())));
    }

    @PostMapping("getFilteredUsers")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity<Page<UserInfoResponseDto>> getFilteredUsers(@RequestBody PageableSearchFilterDto pageRequest) {
        Page<UserInfoResponseDto> response = userService.getUsersFiltered(pageRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("resetPassword")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        String message = null;
        try {
            message = userService.resetPassword(resetPasswordDto);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("error in userController", e);
        }
        return new ResponseEntity<>(new SuccessResponseDto(SuccessMessages.USER_TEMP_PASSWORD_TITLE, message), HttpStatus.OK);
    }
}

