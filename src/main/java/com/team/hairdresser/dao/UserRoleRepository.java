package com.team.hairdresser.dao;


import com.team.hairdresser.domain.RoleEntity;
import com.team.hairdresser.domain.UserRole;
import com.team.hairdresser.domain.Users;
import com.team.hairdresser.utils.abstracts.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends BaseRepository<UserRole, Long> {

    List<UserRole> findByUser(Users user);

    List<UserRole> findAllByRole(RoleEntity role);
}
