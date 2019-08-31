package com.team.hairdresser.dto.role;


import com.team.hairdresser.dto.authority.AuthorityResponseDto;

import java.util.List;

public class RoleDto {

    private Long roleId;
    private String name;
    private String description;
    private Boolean isDeleted;
    private List<AuthorityResponseDto> roleAuthorities;

    public List<AuthorityResponseDto> getRoleAuthorities() {
        return roleAuthorities;
    }

    public void setRoleAuthorities(List<AuthorityResponseDto> roleAuthorities) {
        this.roleAuthorities = roleAuthorities;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}