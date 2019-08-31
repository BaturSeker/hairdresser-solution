package com.team.hairdresser.service.impl.authority;


import com.team.hairdresser.domain.*;
import com.team.hairdresser.service.api.authority.AuthorityListRules;
import com.team.hairdresser.service.api.authority.AuthorizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class AuthorityListRulesImpl implements AuthorityListRules {

    private AuthorizationService authorizationService;

    private static Logger LOGGER = LoggerFactory.getLogger(AuthorityListRulesImpl.class);

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<UserRoleEntity> getAuthorizeList(Long userId) {

        if (userId != null) {
            List<UserRoleEntity> userRoleEntityElem = authorizationService.getAuthorizeList(userId);
            if (userRoleEntityElem != null) {
                return userRoleEntityElem;
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

    @Autowired
    public void setAuthorizationService(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }
}
