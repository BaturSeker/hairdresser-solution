package com.team.hairdresser.service.api.authority;

import com.team.hairdresser.domain.UserRole;

import java.util.List;

public interface AuthorizationService {

    List<UserRole> getAuthorizeList(Long userId);
}