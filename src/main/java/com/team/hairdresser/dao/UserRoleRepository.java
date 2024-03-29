package com.team.hairdresser.dao;


import com.team.hairdresser.domain.RoleEntity;
import com.team.hairdresser.domain.UserRoleEntity;
import com.team.hairdresser.domain.UserEntity;
import com.team.hairdresser.utils.abstracts.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends BaseRepository<UserRoleEntity, Long> {

    List<UserRoleEntity> findByUser(UserEntity user);

    List<UserRoleEntity> findAllByRole(RoleEntity role);
}
