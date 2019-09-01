package com.team.hairdresser.service.impl.authority;


import com.team.hairdresser.constant.AuthorityCodes;
import com.team.hairdresser.constant.ExceptionMessages;
import com.team.hairdresser.constant.ValidationMessages;
import com.team.hairdresser.dao.AuthorityRepository;
import com.team.hairdresser.dao.RoleAuthorityRepository;
import com.team.hairdresser.dao.RoleRepository;
import com.team.hairdresser.domain.*;
import com.team.hairdresser.dto.authority.AuthorityPageDto;
import com.team.hairdresser.dto.authority.AuthorityRequestDto;
import com.team.hairdresser.dto.authority.AuthorityResponseDto;
import com.team.hairdresser.dto.authority.RoleAuthorityRequestDto;
import com.team.hairdresser.service.api.authority.AuthorityService;
import com.team.hairdresser.utils.pageablesearch.model.BaseSortRequestDto;
import com.team.hairdresser.utils.pageablesearch.model.PageRequestDto;
import com.team.hairdresser.utils.pageablesearch.model.PageableSearchFilterDto;
import com.team.hairdresser.utils.pageablesearch.specification.SearchSpecificationBuilder;
import com.team.hairdresser.utils.util.CalendarHelper;
import com.team.hairdresser.utils.util.ValidationHelper;
import com.team.hairdresser.utils.util.exception.NullObjectException;
import com.team.hairdresser.utils.util.exception.ValidationException;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class AuthorityServiceImpl implements AuthorityService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorityServiceImpl.class);

    private AuthorityRepository authorityRepository;
    private RoleAuthorityRepository roleAuthorityRepository;
    private RoleRepository roleRepository;

    @Autowired
    public AuthorityServiceImpl(AuthorityRepository authorityRepository,
                                RoleAuthorityRepository roleAuthorityRepository, RoleRepository roleRepository) {
        this.authorityRepository = authorityRepository;
        this.roleAuthorityRepository = roleAuthorityRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.UPDATE_AUTHORITY + "')")
    public void save(AuthorityRequestDto authorityRequestDto) {
        controlSave(authorityRequestDto);

        AuthorityEntity authorityEntity = new AuthorityEntity();
        authorityEntity.setParentAuthorityEntity(authorityRepository.getOne(authorityRequestDto.getParentId()));
        authorityEntity.setCreatedDate(CalendarHelper.getCurrentInstant());
        authorityEntity.setMenu(authorityRequestDto.getMenu());
        authorityEntity.setAuthorityCode(authorityRequestDto.getAuthorizeCode());
        authorityEntity.setTitle(authorityRequestDto.getTitle());
        authorityEntity.setUrl(authorityRequestDto.getUrl());
        authorityEntity.setVisible(authorityRequestDto.getVisible());
        authorityEntity.setIcon(authorityRequestDto.getIcon());
        authorityRepository.save(authorityEntity);
    }

    private void controlSave(AuthorityRequestDto authorityRequestDto) {
        StringBuilder messages = new StringBuilder();
        boolean isValid = true;
        if (!Objects.nonNull(authorityRequestDto.getTitle())) {
            isValid = false;
            messages.append(ValidationMessages.AUTHORITY_TITLE_NOT_NULL);
            messages.append(System.lineSeparator());
        }
        if (!Objects.nonNull(authorityRequestDto.getIcon())) {
            isValid = false;
            messages.append(ValidationMessages.AUTHORITY_ICON_NOT_NULL);
            messages.append(System.lineSeparator());
        }
        if (!Objects.nonNull(authorityRequestDto.getUrl())) {
            isValid = false;
            messages.append(ValidationMessages.AUTHORITY_URL_NOT_NULL);
            messages.append(System.lineSeparator());
        }
        if (!Objects.nonNull(authorityRequestDto.getAuthorizeCode())) {
            isValid = false;
            messages.append(ValidationMessages.AUTHORITY_AUTHORIZE_CODE_NOT_NULL);
            messages.append(System.lineSeparator());
        }
        if (!Objects.nonNull(authorityRequestDto.getMenu())) {
            isValid = false;
            messages.append(ValidationMessages.AUTHORITY_IS_MENU_NOT_NULL);
            messages.append(System.lineSeparator());
        }
        if (!Objects.nonNull(authorityRequestDto.getVisible())) {
            isValid = false;
            messages.append(ValidationMessages.AUTHORITY_IS_VISIBLE_NOT_NULL);
            messages.append(System.lineSeparator());
        }

        if (!isValid) {
            throw new ValidationException(messages.toString());
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.UPDATE_AUTHORITY + "')")
    public void update(Long authorityId, AuthorityRequestDto authorityRequestDto) {
        controlUpdate(authorityId, authorityRequestDto);

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
        List<RoleAuthorityEntity> roleAuthorities = roleAuthorityRepository.findAllByAuthorityEntity(authorityRepository.getOne(authorityId));
        for (RoleAuthorityEntity roleAuthorityEntity : roleAuthorities) {
            roleAuthorityRepository.delete(roleAuthorityEntity);
        }
    }

    private void controlUpdate(Long authorityId, AuthorityRequestDto authorityRequestDto) {
        StringBuilder messages = new StringBuilder();
        boolean isValid = true;
        if (!Objects.nonNull(authorityRequestDto)) {
            messages.append(ValidationMessages.AUTHORITY_NOT_NULL);
            messages.append(System.lineSeparator());
            throw new ValidationException(messages.toString());
        }
        if (shouldIconExist(authorityId)) {
            if (!ValidationHelper.isValid(authorityRequestDto.getIcon())) {
                isValid = false;
                messages.append(ValidationMessages.AUTHORITY_ICON_NOT_NULL);
                messages.append(System.lineSeparator());
            }
        }
        if (!ValidationHelper.isValid(authorityId)) {
            isValid = false;
            messages.append(ValidationMessages.AUTHORITY_ID_NOT_NULL);
            messages.append(System.lineSeparator());
        }
        if (!isValid) {
            throw new ValidationException(messages.toString());
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_AUTHORITY_MANAGEMENT + "')")
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public AuthorityEntity getAuthority(Long authorityId) {
        AuthorityEntity role = authorityRepository.getOne(authorityId);

        if (role == null) {
            throw new NullObjectException(ExceptionMessages.ROLE_NULL);
        }
        return role;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_AUTHORITY_MANAGEMENT + "')")
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<AuthorityEntity> readAll() {
        return authorityRepository.findByParentAuthorityEntityOrderById(null);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.UPDATE_ROLE_AUTHORITIES + "')")
    public void assignRoleAuthorities(RoleAuthorityRequestDto roleAuthorityRequestDto) {
        StringBuilder messages = new StringBuilder();
        boolean isValid = true;
        if (!ValidationHelper.isValid(roleAuthorityRequestDto.getRoleId())) {
            isValid = false;
            messages.append(ValidationMessages.ROLE_ID_NOT_NULL);
            messages.append(System.lineSeparator());
        }
        if (!ValidationHelper.isValid(roleAuthorityRequestDto.getAuthorityIds())) {
            isValid = false;
            messages.append(ValidationMessages.AUTHORITY_ID_LIST_NOT_NULL);
            messages.append(System.lineSeparator());
        }

        if (!isValid) {
            throw new ValidationException(messages.toString());
        }

        RoleEntity role = roleRepository.getOne(roleAuthorityRequestDto.getRoleId());
        roleAuthorityRepository.deleteAllByRoleEntity(role);
        for (Long authorityId : roleAuthorityRequestDto.getAuthorityIds()) {
            RoleAuthorityEntity roleAuthorityEntity = new RoleAuthorityEntity();
            roleAuthorityEntity.setRoleEntity(role);
            roleAuthorityEntity.setAuthorityEntity(authorityRepository.getOne(authorityId));
            roleAuthorityRepository.save(roleAuthorityEntity);
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_AUTHORITY_MANAGEMENT + "')")
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<AuthorityEntity> getUserAuthorities(UserEntity userEntity) {
        Collection<UserRoleEntity> userRolesByUsersIdEntity = userEntity.getUserRoleEntities();
        List<Long> roleIds = new ArrayList<>();
        for (UserRoleEntity userRoleEntity : userRolesByUsersIdEntity) {
            roleIds.add(userRoleEntity.getRole().getId());
        }
        Set<Long> authorityIds = new HashSet<>();
        for (Long roleId : roleIds) {
            List<RoleAuthorityEntity> roleAuthorities = roleAuthorityRepository.findAllByRoleEntity(roleRepository.getOne(roleId));
            for (RoleAuthorityEntity roleAuth : roleAuthorities) {
                authorityIds.add(roleAuth.getAuthorityEntity().getId());
            }
        }

        List<AuthorityEntity> allAuthorityEntity = this.authorityRepository.findByParentAuthorityEntityOrderById(null);
        return getAuthorities(allAuthorityEntity, authorityIds);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<AuthorityEntity> getAnonymousUserAuthorities() {
        RoleEntity role = roleRepository.findByName("AnonymousUser");
        Set<Long> authorityIds = new HashSet<>();
        List<RoleAuthorityEntity> roleAuthorities = roleAuthorityRepository.findAllByRoleEntity(roleRepository.getOne(role.getId()));
        for (RoleAuthorityEntity roleAuth : roleAuthorities) {
            authorityIds.add(roleAuth.getAuthorityEntity().getId());
        }

        List<AuthorityEntity> allAuthorityEntity = this.authorityRepository.findByParentAuthorityEntityOrderById(null);
        return getAuthorities(allAuthorityEntity, authorityIds);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<AuthorityEntity> readAllAuthority() {
        return IteratorUtils.toList(this.authorityRepository.findAll().iterator());
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
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
            authorityPageDto.setParentId(Objects.nonNull(authorityEntity.getParentAuthorityEntity()) ? authorityEntity.getParentAuthorityEntity().getId() : null);
            authorityPageDto.setMenu(authorityEntity.getMenu());
            authorityPageDto.setTitle(authorityEntity.getTitle());

            authorityPageDtos.add(authorityPageDto);
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<AuthorityResponseDto> findAuthoritiesByRoleId(Long roleId) {
        List<RoleAuthorityEntity> roleAuthorities = this.roleAuthorityRepository.findAllByRoleEntity(roleRepository.getOne(roleId));
        Set<Long> authorityIds = new HashSet<>();

        for (RoleAuthorityEntity roleAuth : roleAuthorities) {
            authorityIds.add(roleAuth.getAuthorityEntity().getId());
        }

        List<AuthorityEntity> allAuthorityEntity = this.authorityRepository.findByParentAuthorityEntityOrderById(null);
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

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public boolean shouldIconExist(Long authorityId) {
        AuthorityEntity authorityEntity = read(authorityId);
        if (authorityEntity.getParentAuthorityEntity() != null) {
            AuthorityEntity parent = authorityEntity.getParentAuthorityEntity();
            if (parent.getParentAuthorityEntity() == null) {
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public AuthorityEntity read(Long authorityId) {
        StringBuilder message = new StringBuilder();
        boolean isValid = true;
        if (!ValidationHelper.isValid(authorityId)) {
            isValid = false;
            message.append(ValidationMessages.AUTHORITY_ID_NOT_NULL);
            message.append(System.lineSeparator());
        }
        if (!isValid) {
            throw new ValidationException(message.toString());
        }
        return getAuthority(authorityId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public AuthorityEntity findByAuthorityCode(String authorityCode) {
        return null;
    }
}