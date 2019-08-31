package com.team.hairdresser.dao;


import com.team.hairdresser.domain.Roles;
import com.team.hairdresser.utils.abstracts.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends BaseRepository<Roles, Long> {

    @Query(nativeQuery = true, value = "select RoleId as VALUE, Name as TEXT from Role Where IsDeleted = 0")
    List<Object[]> findRolesAsComboValues();

    Roles findByName(String name);

    List<Roles> findAllByNameContains(String name);

    List<Roles> findAllByIsDeleted(Boolean isDeleted);
}

