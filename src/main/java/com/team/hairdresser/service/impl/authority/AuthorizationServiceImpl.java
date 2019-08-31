package com.team.hairdresser.service.impl.authority;


import com.team.hairdresser.dao.UserRoleRepository;
import com.team.hairdresser.dao.UsersRepository;
import com.team.hairdresser.domain.UserRoleEntity;
import com.team.hairdresser.service.api.authority.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class AuthorizationServiceImpl implements AuthorizationService {

    private UserRoleRepository userRoleRepository;

    private UsersRepository usersRepository;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<UserRoleEntity> getAuthorizeList(Long userId) {
        List<UserRoleEntity> authorizeList = userRoleRepository.findByUser(usersRepository.getOne(userId));
        return authorizeList;
    }

    @Autowired
    public void setUserRoleRepository(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Autowired
    public void setUsersRepository(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }
}
