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
        AuthorityEntity authorityEntity = new AuthorityEntity();
        authorityEntity.setParentAuthority(authorityRepository.getOne(authorityRequestDto.getParentId()));
        authorityEntity.setCreatedDate(CalendarHelper.getCurrentInstant());
        authorityEntity.setMenu(authorityRequestDto.getMenu());
        authorityEntity.setAuthorityCode(authorityRequestDto.getAuthorizeCode());
        authorityEntity.setTitle(authorityRequestDto.getTitle());
        authorityEntity.setUrl(authorityRequestDto.getUrl());
        authorityEntity.setVisible(authorityRequestDto.getVisible());
        authorityEntity.setIcon(authorityRequestDto.getIcon());
        authorityRepository.save(authorityEntity);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.UPDATE_AUTHORITY + "')")
    public void update(Long authorityId, AuthorityRequestDto authorityRequestDto) {
        AuthorityEntity authorityEntity = authorityRepository.getOne(authorityId);

        if (Objects.equals(authorityEntity, null)) {
            throw new NullObjectException(ExceptionMessages.AUTHORITY_NULL);
        }
        //this.cancelRoleAuthorityRelation(authorityId);
        authorityEntity.setTitle(authorityRequestDto.getTitle());
        authorityEntity.setIcon(authorityRequestDto.getIcon());
        authorityRepository.save(authorityEntity);
    }

    private void cancelRoleAuthorityRelation(Long authorityId) {
        List<RoleAuthorityEntity> roleAuthorities = roleAuthorityRepository.findAllByAuthority(authorityRepository.getOne(authorityId));
        for (RoleAuthorityEntity roleAuthorityEntity : roleAuthorities) {
            roleAuthorityRepository.delete(roleAuthorityEntity);
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_AUTHORITY_MANAGEMENT + "')")
    public AuthorityEntity getAuthority(Long authorityId) {
        AuthorityEntity role = authorityRepository.getOne(authorityId);

        if (role == null) {
            throw new NullObjectException(ExceptionMessages.ROLE_NULL);
        }
        return role;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_AUTHORITY_MANAGEMENT + "')")
    public List<AuthorityEntity> readAll() {
        return authorityRepository.findByParentAuthorityOrderById(null);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.UPDATE_ROLE_AUTHORITIES + "')")
    public void assignRoleAuthorities(RoleAuthorityRequestDto roleAuthorityRequestDto) {
        RoleEntity role = roleRepository.getOne(roleAuthorityRequestDto.getRoleId());
        roleAuthorityRepository.deleteAllByRole(role);
        for (Long authorityId : roleAuthorityRequestDto.getAuthorityIds()) {
            RoleAuthorityEntity roleAuthorityEntity = new RoleAuthorityEntity();
            roleAuthorityEntity.setRole(role);
            roleAuthorityEntity.setAuthority(authorityRepository.getOne(authorityId));
            roleAuthorityRepository.save(roleAuthorityEntity);
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_AUTHORITY_MANAGEMENT + "')")
    public List<AuthorityEntity> getUserAuthorities(UserEntity userEntity) {
        Collection<UserRoleEntity> userRolesByUsersIdEntity = userEntity.getUserRoleEntities();
        List<Long> roleIds = new ArrayList<>();
        for (UserRoleEntity userRoleEntity : userRolesByUsersIdEntity) {
            roleIds.add(userRoleEntity.getRole().getId());
        }
        Set<Long> authorityIds = new HashSet<>();
        for (Long roleId : roleIds) {
            List<RoleAuthorityEntity> roleAuthorities = roleAuthorityRepository.findAllByRole(roleRepository.getOne(roleId));
            for (RoleAuthorityEntity roleAuth : roleAuthorities) {
                authorityIds.add(roleAuth.getAuthority().getId());
            }
        }

        List<AuthorityEntity> allAuthorityEntity = this.authorityRepository.findByParentAuthorityOrderById(null);
        return getAuthorities(allAuthorityEntity, authorityIds);
    }

    @Override
    public List<AuthorityEntity> getAnonymousUserAuthorities() {
        RoleEntity role = roleRepository.findByName("AnonymousUser");
        Set<Long> authorityIds = new HashSet<>();
        List<RoleAuthorityEntity> roleAuthorities = roleAuthorityRepository.findAllByRole(roleRepository.getOne(role.getId()));
        for (RoleAuthorityEntity roleAuth : roleAuthorities) {
            authorityIds.add(roleAuth.getAuthority().getId());
        }

        List<AuthorityEntity> allAuthorityEntity = this.authorityRepository.findByParentAuthorityOrderById(null);
        return getAuthorities(allAuthorityEntity, authorityIds);
    }

    @Override
    public List<AuthorityEntity> readAllAuthority() {
        return IteratorUtils.toList(this.authorityRepository.findAll().iterator());
    }

    @Override
    public Page<AuthorityPageDto> getAll(PageableSearchFilterDto filterDto) {

        SearchSpecificationBuilder<AuthorityEntity> specificationBuilder =
                SearchSpecificationBuilder.filterAllowedKeysInstance(AuthorityEntity.class,
                        "title");

        Specification<AuthorityEntity> builtSpecification = specificationBuilder.build(filterDto.getCriteriaList());

        PageRequestDto pageRequest = filterDto.getPageRequest();
        addSortRequestForCreatedOnDesc(pageRequest);
        PageRequest pageable = pageRequest.getSpringPageRequest();

        Page<AuthorityEntity> authorityPageResult = authorityRepository.findAll(builtSpecification, pageable);

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

    private void buildResultList(Page<AuthorityEntity> authorityPageResult, List<AuthorityPageDto> authorityPageDtos) {
        for (AuthorityEntity authorityEntity : authorityPageResult) {
            AuthorityPageDto authorityPageDto = new AuthorityPageDto();

            authorityPageDto.setAuthorityId(authorityEntity.getId());
            authorityPageDto.setUrl(authorityEntity.getUrl());
            authorityPageDto.setParentId(Objects.nonNull(authorityEntity.getParentAuthority()) ? authorityEntity.getParentAuthority().getId() : null);
            authorityPageDto.setMenu(authorityEntity.getMenu());
            authorityPageDto.setTitle(authorityEntity.getTitle());

            authorityPageDtos.add(authorityPageDto);
        }
    }

    @Override
    public List<AuthorityResponse> findAuthoritiesByRoleId(Long roleId) {
        List<RoleAuthorityEntity> roleAuthorities = this.roleAuthorityRepository.findAllByRole(roleRepository.getOne(roleId));
        Set<Long> authorityIds = new HashSet<>();

        for (RoleAuthorityEntity roleAuth : roleAuthorities) {
            authorityIds.add(roleAuth.getAuthority().getId());
        }

        List<AuthorityEntity> allAuthorityEntity = this.authorityRepository.findByParentAuthorityOrderById(null);
        List<AuthorityEntity> authorities = getAuthorities(allAuthorityEntity, authorityIds);

        return AuthorityMapper.INSTANCE.entityListToDtoList(authorities);
    }

    private List<AuthorityEntity> getAuthorities(List<AuthorityEntity> authorityEntityListByDb, Set<Long> authorityIds) {
        List<AuthorityEntity> authorityEntityList = new ArrayList<>();
        for (AuthorityEntity authorityEntity : authorityEntityListByDb) {
            if (authorityIds.contains(authorityEntity.getId())) {
                authorityEntityList.add(authorityEntity);
            } else {
                if (authorityEntity.getAuthorities().size() > 0) {
                    List<AuthorityEntity> newAuthorities = getAuthorities(new ArrayList<>(authorityEntity.getAuthorities()), authorityIds);
                    if (newAuthorities.size() > 0) {
                        authorityEntity.setAuthorities(newAuthorities);
                        authorityEntityList.add(authorityEntity);
                    }
                }
            }
        }
        return authorityEntityList;
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