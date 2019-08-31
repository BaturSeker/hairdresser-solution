package com.team.hairdresser.service.api.authority;

import com.team.hairdresser.domain.UserRole;
import com.team.hairdresser.domain.Users;

import java.util.List;

public interface AuthorityListRules {
    List<UserRole> getAuthorizeList(Long userId);

    void authorize(Users user);

}
