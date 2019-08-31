package com.team.hairdresser.dao;


import com.team.hairdresser.domain.RoleEntity;
import com.team.hairdresser.domain.UserEntity;
import com.team.hairdresser.utils.abstracts.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends BaseRepository<UserEntity, Long> {

    @Query(nativeQuery = true, value = "select Id as VALUE, firstname + ' ' + surname as TEXT from Users")
    List<Object[]> findUsersAsComboValues();

    UserEntity findUsersByUsername(String username);

    Page<UserEntity> findUsersBy(Pageable pageRequest);

    List<UserEntity> findAllByUserRoleEntitiesContaining(RoleEntity role);

    UserEntity findByEmail(String email);

    UserEntity findByMobilePhone(String phone);
}
