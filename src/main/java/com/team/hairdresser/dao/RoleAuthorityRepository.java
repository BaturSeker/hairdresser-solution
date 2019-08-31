package com.team.hairdresser.dao;

import com.team.hairdresser.domain.Authority;
import com.team.hairdresser.domain.RoleAuthority;
import com.team.hairdresser.domain.Roles;
import com.team.hairdresser.utils.abstracts.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleAuthorityRepository extends BaseRepository<RoleAuthority, Long> {

    List<RoleAuthority> findAllByAuthority(Authority authority);

    List<RoleAuthority> findAllByRole(Roles role);

    void deleteAllByRole(Roles role);
}
