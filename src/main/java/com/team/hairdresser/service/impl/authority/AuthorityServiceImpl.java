package com.team.hairdresser.service.impl.authority;


import com.team.hairdresser.constant.AuthorityCodes;
import com.team.hairdresser.constant.ExceptionMessages;
import com.team.hairdresser.dao.AuthorityRepository;
import com.team.hairdresser.dao.RoleAuthorityRepository;
import com.team.hairdresser.dao.RoleRepository;
import com.team.hairdresser.domain.*;
import com.team.hairdresser.dto.authority.AuthorityPageDto;
import com.team.hairdresser.dto.authority.AuthorityRequestDto;
import com.team.hairdresser.dto.authority.AuthorityResponse;
import com.team.hairdresser.dto.authority.RoleAuthorityRequestDto;
import com.team.hairdresser.service.api.authority.AuthorityService;
import com.team.hairdresser.utils.pageablesearch.model.BaseSortRequestDto;
import com.team.hairdresser.utils.pageablesearch.model.PageRequestDto;
import com.team.hairdresser.utils.pageablesearch.model.PageableSearchFilterDto;
import com.team.hairdresser.utils.pageablesearch.specification.SearchSpecificationBuilder;
import com.team.hairdresser.utils.util.CalendarHelper;
import com.team.hairdresser.utils.util.exception.NullObjectException;
import org.apache.commons.collections4.IteratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthorityServiceImpl implements AuthorityService {

    private AuthorityRepository authorityRepository;
    private RoleAuthorityRepository roleAuthorityRepository;
    private RoleRepository roleRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorityServiceImpl.class);

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.UPDATE_AUTHORITY + "')")
    public void save(AuthorityRequestDto authorityRequestDto) {
        Authority authority = new Authority();
        authority.setParentAuthority(authorityRepository.getOne(authorityRequestDto.getParentId()));
        authority.setCreatedDate(CalendarHelper.getCurrentInstant());
        authority.setMenu(authorityRequestDto.getMenu());
        authority.setAuthorityCode(authorityRequestDto.getAuthorizeCode());
        authority.setTitle(authorityRequestDto.getTitle());
        authority.setUrl(authorityRequestDto.getUrl());
        authority.setVisible(authorityRequestDto.getVisible());
        authority.setIcon(authorityRequestDto.getIcon());
        authorityRepository.save(authority);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.UPDATE_AUTHORITY + "')")
    public void update(Long authorityId, AuthorityRequestDto authorityRequestDto) {
        Authority authority = authorityRepository.getOne(authorityId);

        if (Objects.equals(authority, null)) {
            throw new NullObjectException(ExceptionMessages.AUTHORITY_NULL);
        }
        //this.cancelRoleAuthorityRelation(authorityId);
        authority.setTitle(authorityRequestDto.getTitle());
        authority.setIcon(authorityRequestDto.getIcon());
        authorityRepository.save(authority);
    }

    private void cancelRoleAuthorityRelation(Long authorityId) {
        List<RoleAuthority> roleAuthorities = roleAuthorityRepository.findAllByAuthority(authorityRepository.getOne(authorityId));
        for (RoleAuthority roleAuthority : roleAuthorities) {
            roleAuthorityRepository.delete(roleAuthority);
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_AUTHORITY_MANAGEMENT + "')")
    public Authority getAuthority(Long authorityId) {
        Authority role = authorityRepository.getOne(authorityId);

        if (role == null) {
            throw new NullObjectException(ExceptionMessages.ROLE_NULL);
        }
        return role;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_AUTHORITY_MANAGEMENT + "')")
    public List<Authority> readAll() {
        return authorityRepository.findByParentAuthorityOrderById(null);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.UPDATE_ROLE_AUTHORITIES + "')")
    public void assignRoleAuthorities(RoleAuthorityRequestDto roleAuthorityRequestDto) {
        Roles role = roleRepository.getOne(roleAuthorityRequestDto.getRoleId());
        roleAuthorityRepository.deleteAllByRole(role);
        for (Long authorityId : roleAuthorityRequestDto.getAuthorityIds()) {
            RoleAuthority roleAuthority = new RoleAuthority();
            roleAuthority.setRole(role);
            roleAuthority.setAuthority(authorityRepository.getOne(authorityId));
            roleAuthorityRepository.save(roleAuthority);
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_AUTHORITY_MANAGEMENT + "')")
    public List<Authority> getUserAuthorities(Users users) {
        Collection<UserRole> userRolesByUsersId = users.getUserRoles();
        List<Long> roleIds = new ArrayList<>();
        for (UserRole userRole : userRolesByUsersId) {
            roleIds.add(userRole.getRole().getId());
        }
        Set<Long> authorityIds = new HashSet<>();
        for (Long roleId : roleIds) {
            List<RoleAuthority> roleAuthorities = roleAuthorityRepository.findAllByRole(roleRepository.getOne(roleId));
            for (RoleAuthority roleAuth : roleAuthorities) {
                authorityIds.add(roleAuth.getAuthority().getId());
            }
        }

        List<Authority> allAuthority = this.authorityRepository.findByParentAuthorityOrderById(null);
        return getAuthorities(allAuthority, authorityIds);
    }

    @Override
    public List<Authority> getAnonymousUserAuthorities() {
        Roles role = roleRepository.findByName("AnonymousUser");
        Set<Long> authorityIds = new HashSet<>();
        List<RoleAuthority> roleAuthorities = roleAuthorityRepository.findAllByRole(roleRepository.getOne(role.getId()));
        for (RoleAuthority roleAuth : roleAuthorities) {
            authorityIds.add(roleAuth.getAuthority().getId());
        }

        List<Authority> allAuthority = this.authorityRepository.findByParentAuthorityOrderById(null);
        return getAuthorities(allAuthority, authorityIds);
    }

    @Override
    public List<Authority> readAllAuthority() {
        return IteratorUtils.toList(this.authorityRepository.findAll().iterator());
    }

    @Override
    public Page<AuthorityPageDto> getAll(PageableSearchFilterDto filterDto) {

        SearchSpecificationBuilder<Authority> specificationBuilder =
                SearchSpecificationBuilder.filterAllowedKeysInstance(Authority.class,
                        "title");

        Specification<Authority> builtSpecification = specificationBuilder.build(filterDto.getCriteriaList());

        PageRequestDto pageRequest = filterDto.getPageRequest();
        addSortRequestForCreatedOnDesc(pageRequest);
        PageRequest pageable = pageRequest.getSpringPageRequest();

        Page<Authority> authorityPageResult = authorityRepository.findAll(builtSpecification, pageable);

        List<AuthorityPageDto> authorityPageDtos = new ArrayList<>();

        long totalCount = authorityPageResult.getTotalElements();
        buildResultList(authorityPageResult, authorityPageDtos);

        return new PageImpl<>(authorityPageDtos, pageable, totalCount);
    }

    private void addSortRequestForCreatedOnDesc(PageRequestDto pageRequest) {
        BaseSortRequestDto sortRequest = new BaseSortRequestDto();
        sortRequest.setDir("desc");
        sortRequest.setField("updateOn");
        pageRequest.addSort(sortRequest);
    }

    private void buildResultList(Page<Authority> authorityPageResult, List<AuthorityPageDto> authorityPageDtos) {
        for (Authority authority : authorityPageResult) {
            AuthorityPageDto authorityPageDto = new AuthorityPageDto();

            authorityPageDto.setAuthorityId(authority.getId());
            authorityPageDto.setUrl(authority.getUrl());
            authorityPageDto.setParentId(Objects.nonNull(authority.getParentAuthority()) ? authority.getParentAuthority().getId() : null);
            authorityPageDto.setMenu(authority.getMenu());
            authorityPageDto.setTitle(authority.getTitle());

            authorityPageDtos.add(authorityPageDto);
        }
    }

    @Override
    public List<AuthorityResponse> findAuthoritiesByRoleId(Long roleId) {
        List<RoleAuthority> roleAuthorities = this.roleAuthorityRepository.findAllByRole(roleRepository.getOne(roleId));
        Set<Long> authorityIds = new HashSet<>();

        for (RoleAuthority roleAuth : roleAuthorities) {
            authorityIds.add(roleAuth.getAuthority().getId());
        }

        List<Authority> allAuthority = this.authorityRepository.findByParentAuthorityOrderById(null);
        List<Authority> authorities = getAuthorities(allAuthority, authorityIds);

        return AuthorityMapper.INSTANCE.entityListToDtoList(authorities);
    }

    private List<Authority> getAuthorities(List<Authority> authorityListByDb, Set<Long> authorityIds) {
        List<Authority> authorityList = new ArrayList<>();
        for (Authority authority : authorityListByDb) {
            if (authorityIds.contains(authority.getId())) {
                authorityList.add(authority);
            } else {
                if (authority.getAuthorities().size() > 0) {
                    List<Authority> newAuthorities = getAuthorities(new ArrayList<>(authority.getAuthorities()), authorityIds);
                    if (newAuthorities.size() > 0) {
                        authority.setAuthorities(newAuthorities);
                        authorityList.add(authority);
                    }
                }
            }
        }
        return authorityList;
    }

    @Autowired
    public void setRoleAuthorityRepository(RoleAuthorityRepository roleAuthorityRepository) {
        this.roleAuthorityRepository = roleAuthorityRepository;
    }

    @Autowired
    public void setAuthorityRepository(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
}