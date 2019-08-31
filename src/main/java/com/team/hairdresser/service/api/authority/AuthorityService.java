package com.team.hairdresser.service.api.authority;

import com.team.hairdresser.domain.Authority;
import com.team.hairdresser.domain.Users;
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

    Authority getAuthority(Long authorityId);

    List<Authority> readAll();

    void assignRoleAuthorities(RoleAuthorityRequestDto roleAuthorityRequestDto);

    List<Authority> getUserAuthorities(Users users);

    List<Authority> getAnonymousUserAuthorities();

    List<Authority> readAllAuthority();

    Page<AuthorityPageDto> getAll(PageableSearchFilterDto filterDto);

    List<AuthorityResponse> findAuthoritiesByRoleId(Long roleId);
}