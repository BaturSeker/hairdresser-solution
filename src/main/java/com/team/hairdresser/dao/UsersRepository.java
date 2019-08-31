package com.team.hairdresser.dao;


import com.team.hairdresser.domain.RoleEntity;
import com.team.hairdresser.domain.Users;
import com.team.hairdresser.utils.abstracts.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends BaseRepository<Users, Long> {

    @Query(nativeQuery = true, value = "select Id as VALUE, firstname + ' ' + surname as TEXT from Users")
    List<Object[]> findUsersAsComboValues();

    Users findUsersByUsername(String username);

    Page<Users> findUsersBy(Pageable pageRequest);

    List<Users> findAllByUserRolesContaining(RoleEntity role);

    Users findByEmail(String email);

    Users findByMobilePhone(String phone);
}
