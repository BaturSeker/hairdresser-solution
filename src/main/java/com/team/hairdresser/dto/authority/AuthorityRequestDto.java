package com.team.hairdresser.dto.authority;


public class AuthorityRequestDto {
    private Long parentId;
    private String title;
    private String authorizeCode;
    private Boolean IsMenu;
    private String url;
    private Boolean isVisible;
    private String icon;

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorizeCode() {
        return authorizeCode;
    }

    public void setAuthorizeCode(String authorizeCode) {
        this.authorizeCode = authorizeCode;
    }

    public Boolean getMenu() {
        return IsMenu;
    }

    public void setMenu(Boolean menu) {
        IsMenu = menu;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getVisible() {
        return isVisible;
    }

    public void setVisible(Boolean visible) {
        isVisible = visible;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}

