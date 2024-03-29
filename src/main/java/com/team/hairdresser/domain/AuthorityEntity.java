package com.team.hairdresser.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.team.hairdresser.utils.abstracts.BaseEntity;

import javax.persistence.*;
import java.time.Instant;
import java.util.Collection;
import java.util.List;

@Entity
public class AuthorityEntity extends BaseEntity<Long> {

    @Column(name = "Title")
    private String title;

    @Column(name = "Url")
    private String url;

    @Column(name = "CreatedDate")
    private Instant createdDate;

    @Column(name = "IsVisible")
    private Boolean isVisible;

    @Column(name = "IsMenu")
    private Boolean isMenu;

    @Column(name = "Icon")
    private String icon;

    @Column(name = "AuthorityCode")
    private String authorityCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ParentId")
    private AuthorityEntity parentAuthorityEntity;

    @JsonIgnoreProperties("authorityByParentId")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentAuthorityEntity", cascade = CascadeType.ALL)
    private List<AuthorityEntity> authorities;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "authorityEntity")
    private Collection<RoleAuthorityEntity> roleAuthorities;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "authorityEntity")
    private Collection<UserHistoryEntity> userHistories;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "authorityEntity")
    private Collection<UserLogEntity> userLogEntitties;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getVisible() {
        return isVisible;
    }

    public void setVisible(Boolean visible) {
        isVisible = visible;
    }

    public Boolean getMenu() {
        return isMenu;
    }

    public void setMenu(Boolean menu) {
        isMenu = menu;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getAuthorityCode() {
        return authorityCode;
    }

    public void setAuthorityCode(String authorityCode) {
        this.authorityCode = authorityCode;
    }

    public AuthorityEntity getParentAuthorityEntity() {
        return parentAuthorityEntity;
    }

    public void setParentAuthorityEntity(AuthorityEntity parentAuthorityEntity) {
        this.parentAuthorityEntity = parentAuthorityEntity;
    }

    public Collection<UserLogEntity> getUserLogEntitties() {
        return userLogEntitties;
    }

    public void setUserLogEntitties(Collection<UserLogEntity> userLogEntitties) {
        this.userLogEntitties = userLogEntitties;
    }

    public List<AuthorityEntity> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<AuthorityEntity> authorities) {
        this.authorities = authorities;
    }

    public Collection<RoleAuthorityEntity> getRoleAuthorities() {
        return roleAuthorities;
    }

    public void setRoleAuthorities(Collection<RoleAuthorityEntity> roleAuthorities) {
        this.roleAuthorities = roleAuthorities;
    }

    public Collection<UserHistoryEntity> getUserHistories() {
        return userHistories;
    }

    public void setUserHistories(Collection<UserHistoryEntity> userHistories) {
        this.userHistories = userHistories;
    }

    public Collection<UserLogEntity> getUserLogs() {
        return userLogEntitties;
    }

    public void setUserLogs(Collection<UserLogEntity> userLogEntitties) {
        this.userLogEntitties = userLogEntitties;
    }
}
