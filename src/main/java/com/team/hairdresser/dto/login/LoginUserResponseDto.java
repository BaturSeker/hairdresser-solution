package com.team.hairdresser.dto.login;


public class LoginUserResponseDto {

    private Long userId;
    private String name;
    private String surname;
    private String username;
    private Integer fileId;
    private Boolean isPasswordTemporary;

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getPasswordTemporary() {
        return isPasswordTemporary;
    }

    public void setPasswordTemporary(Boolean passwordTemporary) {
        isPasswordTemporary = passwordTemporary;
    }
}
