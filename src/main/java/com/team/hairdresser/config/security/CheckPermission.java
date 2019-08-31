package com.team.hairdresser.config.security;


import com.team.hairdresser.domain.AuthorityEntity;
import com.team.hairdresser.domain.UserEntity;
import com.team.hairdresser.service.api.authority.AuthorityRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by okan.ayvazoglu on 7/20/2017.
 */
@Component("CheckPermission")
public class CheckPermission {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckPermission.class);

    private AuthorityRules authorityRules;

    public boolean hasPermission(Authentication authentication, String... authorityCodes) throws Exception {
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof UserEntity) {
                if (authorityCodes.length == 0) {
                    return true;
                }
                SecurityContext context = SecurityContextHolder.getContext();
                if (context == null)
                    return false;

                List<String> newAuthorityList = new ArrayList<>();
                for (String authorityCode : authorityCodes) {
                    newAuthorityList.addAll(addPermission(authorityCode));
                }
                UserEntity user = (UserEntity) authentication.getPrincipal();
                List<AuthorityEntity> authorities = authorityRules.getUserAuthorities(user);
                for (String childAuthority : newAuthorityList) {
                    boolean state = checkPermission(authorities, childAuthority);
                    if (state) return true;
                }
            } else return false;
        }
        return false;
    }

    private boolean checkPermission(Collection<AuthorityEntity> list, String childAuthorizeCode) {
        for (AuthorityEntity authorize : list) {
            if (authorize.getAuthorityCode().equals(childAuthorizeCode))
                return true;
            if (!authorize.getAuthorities().isEmpty()) {
                boolean state = checkPermission(authorize.getAuthorities(), childAuthorizeCode);
                if (state) return true;
            }
        }
        return false;
    }

    private List<String> addPermission(String childAuthorityCode) {
        List<String> result = new ArrayList<>();
        result.add(childAuthorityCode);
        try {
            AuthorityEntity authorityEntity = authorityRules.findByAuthorityCode(childAuthorityCode);
            for (AuthorityEntity authorityEntityDb : authorityEntity.getAuthorities()) {
                result.addAll(addPermission(authorityEntityDb.getAuthorityCode()));
            }
        } catch (Exception e) {
            LOGGER.error("addPermission() failed!", e);
        }
        return result;
    }

    @Autowired
    public void setAuthorityRules(AuthorityRules authorityRules) {
        this.authorityRules = authorityRules;
    }
}

