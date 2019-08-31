package com.team.hairdresser.service.api.role;

import com.team.hairdresser.domain.RoleEntity;
import com.team.hairdresser.dto.role.RoleRequest;
import com.team.hairdresser.dto.user.UserRoleRequest;

import java.util.List;

public interface RoleService {
    void save(RoleRequest roleRequest);

    void update(Long roleId, RoleRequest roleRequest);

    void delete(Long roleId);

    RoleEntity getRole(Long roleId);

    List<RoleEntity> getAllRoles();

    void assignUserRoles(UserRoleRequest userRoleRequest);

    List getComboRoles();
}
