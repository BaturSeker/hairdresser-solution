package com.team.hairdresser.service.impl.role;

import com.team.hairdresser.constant.AuthorityCodes;
import com.team.hairdresser.constant.ValidationMessages;
import com.team.hairdresser.domain.Roles;
import com.team.hairdresser.dto.role.RoleRequest;
import com.team.hairdresser.dto.user.UserRoleRequest;
import com.team.hairdresser.service.api.role.RoleRules;
import com.team.hairdresser.service.api.role.RoleService;
import com.team.hairdresser.utils.util.ValidationHelper;
import com.team.hairdresser.utils.util.exception.NullObjectException;
import com.team.hairdresser.utils.util.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class RoleRulesImpl implements RoleRules {

    private RoleService roleService;

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.CREATE_ROLE + "')")
    public void save(RoleRequest roleRequest) {
        StringBuilder messages = new StringBuilder();
        boolean isValid = true;
        if (!ValidationHelper.notEmpty(roleRequest.getName())) {
            isValid = false;
            messages.append(ValidationMessages.ROLE_NAME_NOT_NULL);
            messages.append(System.lineSeparator());
        }
        if (!ValidationHelper.notEmpty(roleRequest.getDescription())) {
            isValid = false;
            messages.append(ValidationMessages.ROLE_DESCRIPTION_NOT_NULL);
            messages.append(System.lineSeparator());
        }

        if (!isValid) {
            throw new ValidationException(messages.toString());
        }

        roleService.save(roleRequest);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.UPDATE_ROLE + "')")
    public void update(Long roleId, RoleRequest roleRequest) {
        StringBuilder messages = new StringBuilder();
        boolean isValid = true;
        if (!ValidationHelper.notEmpty(roleId)) {
            isValid = false;
            messages.append(ValidationMessages.ROLE_ID_NOT_NULL);
            messages.append(System.lineSeparator());
        }
        if (!ValidationHelper.notEmpty(roleRequest.getName())) {
            isValid = false;
            messages.append(ValidationMessages.ROLE_NAME_NOT_NULL);
            messages.append(System.lineSeparator());
        }
        if (!ValidationHelper.notEmpty(roleRequest.getDescription())) {
            isValid = false;
            messages.append(ValidationMessages.ROLE_DESCRIPTION_NOT_NULL);
            messages.append(System.lineSeparator());
        }

        if (!isValid) {
            throw new ValidationException(messages.toString());
        }

        roleService.update(roleId, roleRequest);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.DELETE_ROLE + "')")
    public void delete(Long roleId) {
        StringBuilder message = new StringBuilder();
        boolean isValid = true;
        if (!ValidationHelper.notEmpty(roleId)) {
            isValid = false;
            message.append(ValidationMessages.ROLE_ID_NOT_NULL);
            message.append(System.lineSeparator());
        }
        if (!isValid) {
            throw new ValidationException(message.toString());
        }

        roleService.delete(roleId);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_ROLE_MANAGEMENT + "')")
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Roles read(Long roleId) {
        StringBuilder message = new StringBuilder();
        boolean isValid = true;
        if (!ValidationHelper.notEmpty(roleId)) {
            isValid = false;
            message.append(ValidationMessages.ROLE_ID_NOT_NULL);
            message.append(System.lineSeparator());
        }
        if (!isValid) {
            throw new ValidationException(message.toString());
        }

        return roleService.getRole(roleId);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_ROLE_MANAGEMENT + "')")
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Roles> readAll() throws NullObjectException {
        return roleService.getAllRoles();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_ROLE_MANAGEMENT + "')")
    public void assignUserRoles(UserRoleRequest userRoleRequest) {
        StringBuilder message = new StringBuilder();
        boolean isValid = true;
        if (!ValidationHelper.notEmpty(userRoleRequest.getUserId())) {
            isValid = false;
            message.append(ValidationMessages.ASSIGN_USER_ID_NOT_NULL);
            message.append(System.lineSeparator());
        }
        if (!ValidationHelper.notNull(userRoleRequest.getRoleIds())) {
            isValid = false;
            message.append(ValidationMessages.ASSIGN_ROLE_IDS_LIST_NOT_NULL);
            message.append(System.lineSeparator());
        }

        if (!isValid) {
            throw new ValidationException(message.toString());
        }

        roleService.assignUserRoles(userRoleRequest);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_ROLE_MANAGEMENT + "')")
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List getComboRoles() {
        return roleService.getComboRoles();
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }
}

