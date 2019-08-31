package com.team.hairdresser.controller;


import com.team.hairdresser.config.ActiveDirectoryHelper;
import com.team.hairdresser.config.security.JwtUtil;
import com.team.hairdresser.domain.AuthorityEntity;
import com.team.hairdresser.domain.Users;
import com.team.hairdresser.dto.authority.AuthorityResponse;
import com.team.hairdresser.dto.login.LoginRequestDto;
import com.team.hairdresser.dto.login.LoginResponseDto;
import com.team.hairdresser.dto.login.LoginUserResponseDto;
import com.team.hairdresser.service.api.authority.AuthorityListRules;
import com.team.hairdresser.service.api.authority.AuthorityRules;
import com.team.hairdresser.service.api.login.LoginRules;
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
    private LoginRules loginRules;
    private AuthorityListRules authorityListRules;
    private AuthorityRules authorityRules;
    private JwtUtil jwtUtil;
    private ActiveDirectoryHelper activeDirectoryHelper;


    @PostMapping("login")
    public ResponseEntity login(@RequestBody LoginRequestDto loginRequestDto) throws Exception {

        if (activeDirectoryHelper.getLdapConfig().getEnabled()) {
            if (activeDirectoryHelper.authenticate(loginRequestDto.getUsername(), loginRequestDto.getPassword())) {
                Users user = loginRules.loginLDAP(loginRequestDto);
                authorityListRules.authorize(user);
                List<AuthorityEntity> authorities = this.authorityRules.getUserAuthorities(user);
                LoginResponseDto loginResponseDto = new LoginResponseDto();
                loginResponseDto.setLoginUserResponseDto(getUserResponse(user));
                loginResponseDto.setAuthorityResponse(getAuthorityResponse(authorities));

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
            Users user = loginRules.login(loginRequestDto);
            authorityListRules.authorize(user);
            List<AuthorityEntity> authorities = this.authorityRules.getUserAuthorities(user);
            LoginResponseDto loginResponseDto = new LoginResponseDto();
            loginResponseDto.setLoginUserResponseDto(getUserResponse(user));
            loginResponseDto.setAuthorityResponse(getAuthorityResponse(authorities));

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
        List<AuthorityEntity> authorities = this.authorityRules.getAnonymousUserAuthorities();
        List<AuthorityResponse> authorityResponse = getAuthorityResponse(authorities);
        return new ResponseEntity<>(authorityResponse, HttpStatus.OK);
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

    private LoginUserResponseDto getUserResponse(Users user) {
        LoginUserResponseDto loginUserResponseDto = new LoginUserResponseDto();
        loginUserResponseDto.setUserId(user.getId());
        loginUserResponseDto.setSurname(user.getSurname());
        loginUserResponseDto.setName(user.getFirstname());
        loginUserResponseDto.setUsername(user.getUsername());
        loginUserResponseDto.setPasswordTemporary(user.getPasswordTemporary());

        return loginUserResponseDto;
    }

    private List<AuthorityResponse> getAuthorityResponse(List<AuthorityEntity> authorities) {
        List<AuthorityResponse> authorityResponses = new ArrayList<>();
        for (AuthorityEntity authorityEntity : authorities) {
            AuthorityResponse authorityResponse = new AuthorityResponse();
            if (!authorityEntity.getAuthorities().isEmpty()) {
                List<AuthorityResponse> newAuthorityResponses = getAuthorityResponse(new ArrayList<>(authorityEntity.getAuthorities()));
                authorityResponse.setAuthorities(newAuthorityResponses);
            }
            authorityResponse.setUrl(authorityEntity.getUrl());
            authorityResponse.setAuthorityId(authorityEntity.getId());
            authorityResponse.setTitle(authorityEntity.getTitle());
            authorityResponses.add(authorityResponse);
            authorityResponse.setMenu(authorityEntity.getMenu());
            authorityResponse.setIcon(authorityEntity.getIcon());
            authorityResponse.setAuthorityCode(authorityEntity.getAuthorityCode());
        }
        return authorityResponses;
    }

    @Autowired
    public void setLoginRules(LoginRules loginRules) {
        this.loginRules = loginRules;
    }

    @Autowired
    public void setAuthorityListRules(AuthorityListRules authorityListRules) {
        this.authorityListRules = authorityListRules;
    }

    @Autowired
    public void setAuthorityRules(AuthorityRules authorityRules) {
        this.authorityRules = authorityRules;
    }

    @Autowired
    public void setJwtUtil(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Autowired
    public void setActiveDirectoryHelper(ActiveDirectoryHelper activeDirectoryHelper) {
        this.activeDirectoryHelper = activeDirectoryHelper;
    }
}


