package com.team.hairdresser.service.api.authority;

import com.team.hairdresser.domain.UserRoleEntity;
import com.team.hairdresser.domain.Users;

import java.util.List;

public interface AuthorityListRules {
    List<UserRoleEntity> getAuthorizeList(Long userId);

    void authorize(Users user);

}
