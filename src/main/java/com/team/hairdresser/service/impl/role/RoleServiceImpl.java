package com.team.hairdresser.service.impl.role;


import com.team.hairdresser.constant.ExceptionMessages;
import com.team.hairdresser.dao.RoleRepository;
import com.team.hairdresser.dao.UserRoleRepository;
import com.team.hairdresser.dao.UsersRepository;
import com.team.hairdresser.domain.RoleEntity;
import com.team.hairdresser.domain.UserRoleEntity;
import com.team.hairdresser.domain.UserEntity;
import com.team.hairdresser.dto.role.RoleRequestDto;
import com.team.hairdresser.dto.user.UserRoleRequestDto;
import com.team.hairdresser.service.api.role.RoleService;
import com.team.hairdresser.service.api.user.UserRules;
import com.team.hairdresser.utils.util.ComboResponseBuilder;
import com.team.hairdresser.utils.util.exception.NullObjectException;
import com.team.hairdresser.utils.util.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleServiceImpl.class);

    private RoleRepository roleRepository;
    private UserRoleRepository userRoleRepository;
    private UsersRepository usersRepository;
    private UserRules userRules;

    @Override
    public void save(RoleRequestDto roleRequestDto) {
        RoleEntity role = new RoleEntity();
        role.setDeleted(false);
        role.setDescription(roleRequestDto.getDescription());
        role.setName(roleRequestDto.getName());
        roleRepository.saveAndFlush(role);
    }

    @Override
    public void update(Long roleId, RoleRequestDto roleRequestDto) throws NullObjectException {
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

    @Override
    public void delete(Long roleId) throws NullObjectException {
        RoleEntity role = roleRepository.getOne(roleId);
        List<UserEntity> users = userRules.findByRole(role);
        if (!users.isEmpty()) {
            throw new ValidationException("Bu role atanmış kullanıcılar olduğundan Bu Rol Silinemez.");
        }
        role.setDeleted(true);
        roleRepository.saveAndFlush(role);
    }

    public RoleEntity getRole(Long roleId) throws NullObjectException {
        return roleRepository.getOne(roleId);
    }

    @Override
    public List<RoleEntity> getAllRoles() throws NullObjectException {
        return roleRepository.findAllByIsDeleted(false);
    }

    @Override
    public void assignUserRoles(UserRoleRequestDto userRoleRequestDto) {
        for (Long roleId : userRoleRequestDto.getRoleIds()) {
            UserRoleEntity userRoleEntity = new UserRoleEntity();
            userRoleEntity.setRole(roleRepository.getOne(roleId));
            userRoleEntity.setUser(usersRepository.getOne(userRoleRequestDto.getUserId()));
            userRoleRepository.saveAndFlush(userRoleEntity);
        }
    }

    @Override
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
    public void setUserRules(UserRules userRules) {
        this.userRules = userRules;
    }
}
