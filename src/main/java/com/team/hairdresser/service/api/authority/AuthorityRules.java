package com.team.hairdresser.service.api.authority;

import com.team.hairdresser.domain.Authority;
import com.team.hairdresser.domain.Users;
import com.team.hairdresser.dto.authority.AuthorityRequestDto;
import com.team.hairdresser.dto.authority.AuthorityResponse;
import com.team.hairdresser.dto.authority.RoleAuthorityRequestDto;

import java.util.List;

public interface AuthorityRules {
    void save(AuthorityRequestDto authorityRequestDto);

    void update(Long authorityId, AuthorityRequestDto authorityRequestDto);

    Authority read(Long authorityId);

    List<Authority> readAll();

    void assignRoleAuthorities(RoleAuthorityRequestDto roleAuthorityRequestDto);

    List<Authority> getUserAuthorities(Users users) throws Exception;

    List<Authority> getAnonymousUserAuthorities();

    Authority findByAuthorityCode(String authorityCode);

    List<Authority> readAllAuthority();

    List<AuthorityResponse> findAuthoritiesByRoleId(Long roleId);

    boolean shouldIconExist(Long authorityId);
}