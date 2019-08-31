package com.team.hairdresser.domain;


import com.team.hairdresser.utils.abstracts.BaseEntity;

import javax.persistence.*;

@Entity
public class UserLog extends BaseEntity<Long> {

    @Column(name = "IsSuccesful")
    private Boolean isSuccesful;

    @Column(name = "LogDetail")
    private String logDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserId", nullable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AuthorityId", nullable = false)
    private Authority authority;

    public Boolean getSuccesful() {
        return isSuccesful;
    }

    public void setSuccesful(Boolean succesful) {
        isSuccesful = succesful;
    }

    public String getLogDetail() {
        return logDetail;
    }

    public void setLogDetail(String logDetail) {
        this.logDetail = logDetail;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }
}
