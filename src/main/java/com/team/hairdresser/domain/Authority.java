package com.team.hairdresser.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.team.hairdresser.utils.abstracts.BaseEntity;

import javax.persistence.*;
import java.time.Instant;
import java.util.Collection;
import java.util.List;

@Entity
public class Authority extends BaseEntity<Long> {

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
    private Authority parentAuthority;

    @JsonIgnoreProperties("authorityByParentId")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentAuthority", cascade = CascadeType.ALL)
    private List<Authority> authorities;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "authority")
    private Collection<RoleAuthority> roleAuthorities;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "authority")
    private Collection<UserHistory> userHistories;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "authority")
    private Collection<UserLog> userLogs;

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

    public Authority getParentAuthority() {
        return parentAuthority;
    }

    public void setParentAuthority(Authority parentAuthority) {
        this.parentAuthority = parentAuthority;
    }

    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    public Collection<RoleAuthority> getRoleAuthorities() {
        return roleAuthorities;
    }

    public void setRoleAuthorities(Collection<RoleAuthority> roleAuthorities) {
        this.roleAuthorities = roleAuthorities;
    }

    public Collection<UserHistory> getUserHistories() {
        return userHistories;
    }

    public void setUserHistories(Collection<UserHistory> userHistories) {
        this.userHistories = userHistories;
    }

    public Collection<UserLog> getUserLogs() {
        return userLogs;
    }

    public void setUserLogs(Collection<UserLog> userLogs) {
        this.userLogs = userLogs;
    }
}
