package com.team.hairdresser.service.impl.role;


import com.team.hairdresser.constant.AuthorityCodes;
import com.team.hairdresser.constant.ExceptionMessages;
import com.team.hairdresser.constant.ValidationMessages;
import com.team.hairdresser.dao.RoleRepository;
import com.team.hairdresser.dao.UserRoleRepository;
import com.team.hairdresser.dao.UsersRepository;
import com.team.hairdresser.domain.RoleEntity;
import com.team.hairdresser.domain.UserEntity;
import com.team.hairdresser.domain.UserRoleEntity;
import com.team.hairdresser.dto.role.RoleRequestDto;
import com.team.hairdresser.dto.user.UserRoleRequestDto;
import com.team.hairdresser.service.api.role.RoleService;
import com.team.hairdresser.service.api.user.UserService;
import com.team.hairdresser.utils.util.ComboResponseBuilder;
import com.team.hairdresser.utils.util.ValidationHelper;
import com.team.hairdresser.utils.util.exception.NullObjectException;
import com.team.hairdresser.utils.util.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleServiceImpl.class);

    private RoleRepository roleRepository;
    private UserRoleRepository userRoleRepository;
    private UsersRepository usersRepository;
    private UserService userService;

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.CREATE_ROLE + "')")
    public void save(RoleRequestDto roleRequestDto) {
        controlSave(roleRequestDto);

        RoleEntity role = new RoleEntity();
        role.setDeleted(false);
        role.setDescription(roleRequestDto.getDescription());
        role.setName(roleRequestDto.getName());
        roleRepository.saveAndFlush(role);
    }

    private void controlSave(RoleRequestDto roleRequestDto) {
        StringBuilder messages = new StringBuilder();
        boolean isValid = true;
        if (!ValidationHelper.notEmpty(roleRequestDto.getName())) {
            isValid = false;
            messages.append(ValidationMessages.ROLE_NAME_NOT_NULL);
            messages.append(System.lineSeparator());
        }
        if (!ValidationHelper.notEmpty(roleRequestDto.getDescription())) {
            isValid = false;
            messages.append(ValidationMessages.ROLE_DESCRIPTION_NOT_NULL);
            messages.append(System.lineSeparator());
        }

        if (!isValid) {
            throw new ValidationException(messages.toString());
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.UPDATE_ROLE + "')")
    public void update(Long roleId, RoleRequestDto roleRequestDto) throws NullObjectException {
        controlUpdate(roleId, roleRequestDto);

        RoleEntity role = null;
        try {
            role = roleRepository.getOne(roleId);
        } catch (Exception e) {
            LOGGER.error("Role update failed!", e);
        }
        if (role == null) {
            throw new NullObjectException(ExceptionMessages.ROLE_NULL);
        }
        role.setDescription(roleRequestDto.getDescription());
        role.setName(roleRequestDto.getName());
        roleRepository.saveAndFlush(role);
    }

    private void controlUpdate(Long roleId, RoleRequestDto roleRequestDto) {
        StringBuilder messages = new StringBuilder();
        boolean isValid = true;
        if (!ValidationHelper.notEmpty(roleId)) {
            isValid = false;
            messages.append(ValidationMessages.ROLE_ID_NOT_NULL);
            messages.append(System.lineSeparator());
        }
        if (!ValidationHelper.notEmpty(roleRequestDto.getName())) {
            isValid = false;
            messages.append(ValidationMessages.ROLE_NAME_NOT_NULL);
            messages.append(System.lineSeparator());
        }
        if (!ValidationHelper.notEmpty(roleRequestDto.getDescription())) {
            isValid = false;
            messages.append(ValidationMessages.ROLE_DESCRIPTION_NOT_NULL);
            messages.append(System.lineSeparator());
        }

        if (!isValid) {
            throw new ValidationException(messages.toString());
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.DELETE_ROLE + "')")
    public void delete(Long roleId) throws NullObjectException {
        deleteControl(roleId);

        RoleEntity role = roleRepository.getOne(roleId);
        List<UserEntity> users = userService.findByRole(role);
        if (!users.isEmpty()) {
            throw new ValidationException("Bu role atanmış kullanıcılar olduğundan Bu Rol Silinemez.");
        }
        role.setDeleted(true);
        roleRepository.saveAndFlush(role);
    }

    private void deleteControl(Long roleId) {
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
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_ROLE_MANAGEMENT + "')")
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public RoleEntity read(Long roleId) throws NullObjectException {
        controlRead(roleId);

        return roleRepository.getOne(roleId);
    }

    private void controlRead(Long roleId) {
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
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_ROLE_MANAGEMENT + "')")
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<RoleEntity> readAll() throws NullObjectException {
        return roleRepository.findAllByIsDeleted(false);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_ROLE_MANAGEMENT + "')")
    public void assignUserRoles(UserRoleRequestDto userRoleRequestDto) {
        controlUserRoleRequestDto(userRoleRequestDto);

        for (Long roleId : userRoleRequestDto.getRoleIds()) {
            UserRoleEntity userRoleEntity = new UserRoleEntity();
            userRoleEntity.setRole(roleRepository.getOne(roleId));
            userRoleEntity.setUser(usersRepository.getOne(userRoleRequestDto.getUserId()));
            userRoleRepository.saveAndFlush(userRoleEntity);
        }
    }

    private void controlUserRoleRequestDto(UserRoleRequestDto userRoleRequestDto) {
        StringBuilder message = new StringBuilder();
        boolean isValid = true;
        if (!ValidationHelper.notEmpty(userRoleRequestDto.getUserId())) {
            isValid = false;
            message.append(ValidationMessages.ASSIGN_USER_ID_NOT_NULL);
            message.append(System.lineSeparator());
        }
        if (!ValidationHelper.notNull(userRoleRequestDto.getRoleIds())) {
            isValid = false;
            message.append(ValidationMessages.ASSIGN_ROLE_IDS_LIST_NOT_NULL);
            message.append(System.lineSeparator());
        }

        if (!isValid) {
            throw new ValidationException(message.toString());
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_ROLE_MANAGEMENT + "')")
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List getComboRoles() {
        List<Object[]> resultList = roleRepository.findRolesAsComboValues();
        return ComboResponseBuilder.buildComboResponseList(resultList);
    }

    @Autowired
    public void setUsersRepository(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setUserRoleRepository(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
