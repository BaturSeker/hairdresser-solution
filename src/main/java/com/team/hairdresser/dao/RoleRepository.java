package com.team.hairdresser.dao;


import com.team.hairdresser.domain.RoleEntity;
import com.team.hairdresser.utils.abstracts.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends BaseRepository<RoleEntity, Long> {

    @Query(nativeQuery = true, value = "select RoleId as VALUE, Name as TEXT from Role Where IsDeleted = 0")
    List<Object[]> findRolesAsComboValues();

    RoleEntity findByName(String name);

    List<RoleEntity> findAllByNameContains(String name);

    List<RoleEntity> findAllByIsDeleted(Boolean isDeleted);
}

