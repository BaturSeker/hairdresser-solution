package com.team.hairdresser.service.api.authority;

import com.team.hairdresser.domain.AuthorityEntity;
import com.team.hairdresser.domain.UserEntity;
import com.team.hairdresser.dto.authority.AuthorityRequestDto;
import com.team.hairdresser.dto.authority.AuthorityResponseDto;
import com.team.hairdresser.dto.authority.RoleAuthorityRequestDto;

import java.util.List;

public interface AuthorityRules {
    void save(AuthorityRequestDto authorityRequestDto);

    void update(Long authorityId, AuthorityRequestDto authorityRequestDto);

    AuthorityEntity read(Long authorityId);

    List<AuthorityEntity> readAll();

    void assignRoleAuthorities(RoleAuthorityRequestDto roleAuthorityRequestDto);

    List<AuthorityEntity> getUserAuthorities(UserEntity userEntity) throws Exception;

    List<AuthorityEntity> getAnonymousUserAuthorities();

    AuthorityEntity findByAuthorityCode(String authorityCode);

    List<AuthorityEntity> readAllAuthority();

    List<AuthorityResponseDto> findAuthoritiesByRoleId(Long roleId);

    boolean shouldIconExist(Long authorityId);
}