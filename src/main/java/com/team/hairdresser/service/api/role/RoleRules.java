package com.team.hairdresser.service.api.role;

import com.team.hairdresser.domain.Roles;
import com.team.hairdresser.dto.role.RoleRequest;
import com.team.hairdresser.dto.user.UserRoleRequest;

import java.util.List;

public interface RoleRules {
    void save(RoleRequest roleRequest);

    void update(Long roleId, RoleRequest roleRequest);

    void delete(Long roleId);

    Roles read(Long roleId);

    List<Roles> readAll();

    void assignUserRoles(UserRoleRequest userRoleRequest);

    List getComboRoles();
}
