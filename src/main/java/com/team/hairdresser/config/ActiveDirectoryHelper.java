package com.team.hairdresser.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Component;

import javax.naming.directory.DirContext;

@Component
public class ActiveDirectoryHelper {

    private LDAPConfig ldapConfig;

    private LdapContextSource contextSource;

    public boolean authenticate(String userDn, String credentials) {
        DirContext ctx = null;
        try {
            if (userDn.contains("@" + ldapConfig.getDomain())) {
                ctx = contextSource.getContext(userDn, credentials);
            } else {
                ctx = contextSource.getContext(userDn + "@" + ldapConfig.getDomain(), credentials);
            }
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            LdapUtils.closeContext(ctx);
        }
    }

    public LDAPConfig getLdapConfig() {
        return ldapConfig;
    }

    @Autowired
    public void setContextSource(@Qualifier("getContextSource") LdapContextSource contextSource) {
        this.contextSource = contextSource;
    }

    @Autowired
    public void setLdapConfig(LDAPConfig ldapConfig) {
        this.ldapConfig = ldapConfig;
    }
}
