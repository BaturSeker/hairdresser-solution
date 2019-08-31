package com.team.hairdresser.service.impl.authority;


import com.team.hairdresser.constant.ValidationMessages;
import com.team.hairdresser.domain.AuthorityEntity;
import com.team.hairdresser.domain.Users;
import com.team.hairdresser.dto.authority.AuthorityRequestDto;
import com.team.hairdresser.dto.authority.AuthorityResponse;
import com.team.hairdresser.dto.authority.RoleAuthorityRequestDto;
import com.team.hairdresser.service.api.authority.AuthorityRules;
import com.team.hairdresser.service.api.authority.AuthorityService;
import com.team.hairdresser.utils.util.ValidationHelper;
import com.team.hairdresser.utils.util.exception.NullObjectException;
import com.team.hairdresser.utils.util.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(rollbackFor = Exception.class)
public class AuthorityRulesImpl implements AuthorityRules {

    private AuthorityService authorityService;

    @Override
    public void save(AuthorityRequestDto authorityRequestDto) {
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

        authorityService.save(authorityRequestDto);
    }

    @Override
    public void update(Long authorityId, AuthorityRequestDto authorityRequestDto) {
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

        authorityService.update(authorityId, authorityRequestDto);
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
        return authorityService.getAuthority(authorityId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<AuthorityEntity> readAll() throws NullObjectException {
        return authorityService.readAll();
    }

    @Override
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

        authorityService.assignRoleAuthorities(roleAuthorityRequestDto);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<AuthorityEntity> getUserAuthorities(Users users) {
        return this.authorityService.getUserAuthorities(users);
    }


    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<AuthorityEntity> getAnonymousUserAuthorities() {
        return this.authorityService.getAnonymousUserAuthorities();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public AuthorityEntity findByAuthorityCode(String authorityCode) {
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<AuthorityEntity> readAllAuthority() {
        return this.authorityService.readAllAuthority();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<AuthorityResponse> findAuthoritiesByRoleId(Long roleId) {
        return this.authorityService.findAuthoritiesByRoleId(roleId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public boolean shouldIconExist(Long authorityId) {
        AuthorityEntity authorityEntity = read(authorityId);
        if (authorityEntity.getParentAuthority() != null) {
            AuthorityEntity parent = authorityEntity.getParentAuthority();
            if (parent.getParentAuthority() == null) {
                return true;
            }
        }
        return false;
    }

    @Autowired
    public void setAuthorityService(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }
}

