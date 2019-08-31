package com.team.hairdresser.service.api.role;

import com.team.hairdresser.domain.RoleEntity;
import com.team.hairdresser.dto.role.RoleRequest;
import com.team.hairdresser.dto.user.UserRoleRequest;

import java.util.List;

public interface RoleRules {
    void save(RoleRequest roleRequest);

    void update(Long roleId, RoleRequest roleRequest);

    void delete(Long roleId);

    RoleEntity read(Long roleId);

    List<RoleEntity> readAll();

    void assignUserRoles(UserRoleRequest userRoleRequest);

    List getComboRoles();
}
