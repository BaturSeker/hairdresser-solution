package com.team.hairdresser.controller;


import com.team.hairdresser.config.ActiveDirectoryHelper;
import com.team.hairdresser.config.security.JwtUtil;
import com.team.hairdresser.domain.AuthorityEntity;
import com.team.hairdresser.domain.UserEntity;
import com.team.hairdresser.dto.authority.AuthorityResponseDto;
import com.team.hairdresser.dto.login.LoginRequestDto;
import com.team.hairdresser.dto.login.LoginResponseDto;
import com.team.hairdresser.dto.login.LoginUserResponseDto;
import com.team.hairdresser.service.api.authority.AuthorityService;
import com.team.hairdresser.service.api.authority.AuthorizationService;
import com.team.hairdresser.service.api.login.LoginService;
import com.team.hairdresser.utils.util.ClearSession;
import com.team.hairdresser.utils.util.string.StringAppenderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("rest/")
public class LoginController {
    private LoginService loginService;
    private AuthorizationService authorizationService;
    private AuthorityService authorityService;
    private JwtUtil jwtUtil;
    private ActiveDirectoryHelper activeDirectoryHelper;

    @Autowired
    public LoginController(LoginService loginService, AuthorizationService authorizationService,
                           AuthorityService authorityService, JwtUtil jwtUtil,
                           ActiveDirectoryHelper activeDirectoryHelper) {
        this.loginService = loginService;
        this.authorizationService = authorizationService;
        this.authorityService = authorityService;
        this.jwtUtil = jwtUtil;
        this.activeDirectoryHelper = activeDirectoryHelper;
    }

    @PostMapping("login")
    public ResponseEntity login(@RequestBody LoginRequestDto loginRequestDto) throws Exception {

        if (activeDirectoryHelper.getLdapConfig().getEnabled()) {
            if (activeDirectoryHelper.authenticate(loginRequestDto.getUsername(), loginRequestDto.getPassword())) {
                UserEntity user = loginService.loginLDAP(loginRequestDto);
                authorizationService.authorize(user);
                List<AuthorityEntity> authorities = this.authorityService.getUserAuthorities(user);
                LoginResponseDto loginResponseDto = new LoginResponseDto();
                loginResponseDto.setLoginUserResponseDto(getUserResponse(user));
                loginResponseDto.setAuthorityResponseDto(getAuthorityResponse(authorities));

                HttpHeaders headers = new HttpHeaders();
                String token = jwtUtil.generateTokenWithId(user);
                headers.set("Authorization", StringAppenderUtil.append("Bearer ", token));
                loginResponseDto.setToken(token);
                return new ResponseEntity<>(loginResponseDto, headers, HttpStatus.OK);
            } else {
                LoginResponseDto loginResponseDto = new LoginResponseDto();
                return new ResponseEntity<>(loginResponseDto, HttpStatus.UNAUTHORIZED);
            }

        } else {
            UserEntity user = loginService.login(loginRequestDto);
            authorizationService.authorize(user);
            List<AuthorityEntity> authorities = this.authorityService.getUserAuthorities(user);
            LoginResponseDto loginResponseDto = new LoginResponseDto();
            loginResponseDto.setLoginUserResponseDto(getUserResponse(user));
            loginResponseDto.setAuthorityResponseDto(getAuthorityResponse(authorities));

            HttpHeaders headers = new HttpHeaders();
            String token = jwtUtil.generateTokenWithId(user);
            headers.set("Authorization", StringAppenderUtil.append("Bearer ", token));
            loginResponseDto.setToken(token);
            return new ResponseEntity<>(loginResponseDto, headers, HttpStatus.OK);
        }

    }


    @GetMapping("getAnonymousUserAuthorities")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity getCustomerUserAuthorities() {
        List<AuthorityEntity> authorities = this.authorityService.getAnonymousUserAuthorities();
        List<AuthorityResponseDto> authorityResponseDto = getAuthorityResponse(authorities);
        return new ResponseEntity<>(authorityResponseDto, HttpStatus.OK);
    }

    @PostMapping(value = "logout")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity logout() {

        SecurityContextHolder.getContext().setAuthentication(null);
        HttpStatus responseCode;

        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        responseCode = new ClearSession().clear(attr);

        return new ResponseEntity<>(true, responseCode);
    }

    private LoginUserResponseDto getUserResponse(UserEntity user) {
        LoginUserResponseDto loginUserResponseDto = new LoginUserResponseDto();
        loginUserResponseDto.setUserId(user.getId());
        loginUserResponseDto.setSurname(user.getSurname());
        loginUserResponseDto.setName(user.getFirstname());
        loginUserResponseDto.setUsername(user.getUsername());
        loginUserResponseDto.setPasswordTemporary(user.getPasswordTemporary());

        return loginUserResponseDto;
    }

    private List<AuthorityResponseDto> getAuthorityResponse(List<AuthorityEntity> authorities) {
        List<AuthorityResponseDto> authorityResponsDtos = new ArrayList<>();
        for (AuthorityEntity authorityEntity : authorities) {
            AuthorityResponseDto authorityResponseDto = new AuthorityResponseDto();
            if (!authorityEntity.getAuthorities().isEmpty()) {
                List<AuthorityResponseDto> newAuthorityResponsDtos = getAuthorityResponse(new ArrayList<>(authorityEntity.getAuthorities()));
                authorityResponseDto.setAuthorities(newAuthorityResponsDtos);
            }
            authorityResponseDto.setUrl(authorityEntity.getUrl());
            authorityResponseDto.setAuthorityId(authorityEntity.getId());
            authorityResponseDto.setTitle(authorityEntity.getTitle());
            authorityResponsDtos.add(authorityResponseDto);
            authorityResponseDto.setMenu(authorityEntity.getMenu());
            authorityResponseDto.setIcon(authorityEntity.getIcon());
            authorityResponseDto.setAuthorityCode(authorityEntity.getAuthorityCode());
        }
        return authorityResponsDtos;
    }
}


