package com.team.hairdresser.dao;

import com.team.hairdresser.domain.AuthorityEntity;
import com.team.hairdresser.domain.RoleAuthority;
import com.team.hairdresser.domain.Roles;
import com.team.hairdresser.utils.abstracts.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleAuthorityRepository extends BaseRepository<RoleAuthority, Long> {

    List<RoleAuthority> findAllByAuthority(AuthorityEntity authorityEntity);

    List<RoleAuthority> findAllByRole(Roles role);

    void deleteAllByRole(Roles role);
}
