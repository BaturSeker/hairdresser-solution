package com.team.hairdresser.dto.authority;


import java.util.List;

public class AuthorityResponseDto {
    private Long authorityId;
    private String url;
    private Boolean isMenu;
    private String title;
    private String icon;
    private Boolean hasIcon;
    List<AuthorityResponseDto> authorities;
    private String authorityCode;

    public List<AuthorityResponseDto> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<AuthorityResponseDto> authorities) {
        this.authorities = authorities;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Long authorityId) {
        this.authorityId = authorityId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Boolean getHasIcon() {
        return hasIcon;
    }

    public void setHasIcon(Boolean hasIcon) {
        this.hasIcon = hasIcon;
    }

    public void setAuthorityCode(String authorityCode) {
        this.authorityCode = authorityCode;
    }

    public String getAuthorityCode() {
        return authorityCode;
    }
}
