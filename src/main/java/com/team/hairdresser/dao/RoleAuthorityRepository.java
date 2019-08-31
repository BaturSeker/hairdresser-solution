package com.team.hairdresser.dao;

import com.team.hairdresser.domain.AuthorityEntity;
import com.team.hairdresser.domain.RoleAuthorityEntity;
import com.team.hairdresser.domain.RoleEntity;
import com.team.hairdresser.utils.abstracts.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleAuthorityRepository extends BaseRepository<RoleAuthorityEntity, Long> {

    List<RoleAuthorityEntity> findAllByAuthorityEntity(AuthorityEntity authorityEntity);

    List<RoleAuthorityEntity> findAllByRoleEntity(RoleEntity role);

    void deleteAllByRoleEntity(RoleEntity role);
}
