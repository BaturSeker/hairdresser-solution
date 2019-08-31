package com.team.hairdresser.service.api.authority;

import com.team.hairdresser.domain.UserEntity;
import com.team.hairdresser.domain.UserRoleEntity;

import java.util.List;

public interface AuthorizationService {

    List<UserRoleEntity> getAuthorizeList(Long userId);

    void authorize(UserEntity user);
}