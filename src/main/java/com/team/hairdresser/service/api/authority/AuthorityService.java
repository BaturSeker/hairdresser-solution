package com.team.hairdresser.service.api.authority;

import com.team.hairdresser.domain.AuthorityEntity;
import com.team.hairdresser.domain.UserEntity;
import com.team.hairdresser.dto.authority.AuthorityPageDto;
import com.team.hairdresser.dto.authority.AuthorityRequestDto;
import com.team.hairdresser.dto.authority.AuthorityResponse;
import com.team.hairdresser.dto.authority.RoleAuthorityRequestDto;
import com.team.hairdresser.utils.pageablesearch.model.PageableSearchFilterDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AuthorityService {

    void save(AuthorityRequestDto authorityRequestDto);

    void update(Long authorityId, AuthorityRequestDto authorityRequestDto);

    AuthorityEntity getAuthority(Long authorityId);

    List<AuthorityEntity> readAll();

    void assignRoleAuthorities(RoleAuthorityRequestDto roleAuthorityRequestDto);

    List<AuthorityEntity> getUserAuthorities(UserEntity userEntity);

    List<AuthorityEntity> getAnonymousUserAuthorities();

    List<AuthorityEntity> readAllAuthority();

    Page<AuthorityPageDto> getAll(PageableSearchFilterDto filterDto);

    List<AuthorityResponse> findAuthoritiesByRoleId(Long roleId);
}