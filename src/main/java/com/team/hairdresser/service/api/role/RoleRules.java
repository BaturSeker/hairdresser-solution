package com.team.hairdresser.service.api.role;

import com.team.hairdresser.domain.RoleEntity;
import com.team.hairdresser.dto.role.RoleRequestDto;
import com.team.hairdresser.dto.user.UserRoleRequestDto;

import java.util.List;

public interface RoleRules {
    void save(RoleRequestDto roleRequestDto);

    void update(Long roleId, RoleRequestDto roleRequestDto);

    void delete(Long roleId);

    RoleEntity read(Long roleId);

    List<RoleEntity> readAll();

    void assignUserRoles(UserRoleRequestDto userRoleRequestDto);

    List getComboRoles();
}
