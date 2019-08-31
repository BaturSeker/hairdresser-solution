package com.team.hairdresser.domain;


import com.team.hairdresser.utils.abstracts.BaseEntity;

import javax.persistence.*;

@Entity
public class UserLogEntity extends BaseEntity<Long> {

    @Column(name = "IsSuccesful")
    private Boolean isSuccesful;

    @Column(name = "LogDetail")
    private String logDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserId", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AuthorityId", nullable = false)
    private AuthorityEntity authorityEntity;

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

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public AuthorityEntity getAuthorityEntity() {
        return authorityEntity;
    }

    public void setAuthorityEntity(AuthorityEntity authorityEntity) {
        this.authorityEntity = authorityEntity;
    }
}
