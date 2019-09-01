package com.team.hairdresser.service.impl.authority;


import com.team.hairdresser.dao.UserRoleRepository;
import com.team.hairdresser.dao.UsersRepository;
import com.team.hairdresser.domain.*;
import com.team.hairdresser.service.api.authority.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class AuthorizationServiceImpl implements AuthorizationService {

    private UserRoleRepository userRoleRepository;
    private UsersRepository usersRepository;

    @Autowired
    public AuthorizationServiceImpl(UserRoleRepository userRoleRepository, UsersRepository usersRepository) {
        this.userRoleRepository = userRoleRepository;
        this.usersRepository = usersRepository;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<UserRoleEntity> getAuthorizeList(Long userId) {

        if (userId != null) {
            List<UserRoleEntity> authorizeList = userRoleRepository.findByUser(usersRepository.getOne(userId));

            if (authorizeList != null) {
                return authorizeList;
            }
        }
        return null;
    }

    @Override
    public void authorize(UserEntity user) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        List<UserRoleEntity> userRoleEntityList = getAuthorizeList(user.getId());

        for (UserRoleEntity userRoleEntity : userRoleEntityList) {
            RoleEntity role = userRoleEntity.getRole();
            List<RoleAuthorityEntity> roleAuthorities = role.getRoleAuthorityEntities();
            if (roleAuthorities != null) {
                for (RoleAuthorityEntity ra : roleAuthorities) {
                    AuthorityEntity authorityEntity = ra.getAuthorityEntity();
                    authorities.add(new SimpleGrantedAuthority(authorityEntity.getAuthorityCode()));
                }
            }
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, "N/A",
                authorities);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

    }
}
