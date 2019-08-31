package com.team.hairdresser.domain;


import com.team.hairdresser.utils.abstracts.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Users extends BaseEntity<Long> {

    @Size(max = 50)
    @Column(name = "Firstname", length = 50)
    private String firstname;

    @Size(max = 50)
    @Column(name = "Surname", length = 50)
    private String surname;

    @Size(min = 1, max = 50)
    @Column(name = "Username", length = 50, unique = true)
    private String username;

    @Column(name = "MailActive")
    private Boolean mailActive;

    @Column(name = "MobilePhone")
    private String mobilePhone;

    @SuppressWarnings("squid:S3437")
    @Column(name = "LastLoginDate")
    private ZonedDateTime lastLoginDate;

    @Email
    @Size(min = 5, max = 100)
    @Column(name = "Email", length = 100)
    private String email;

    @Column(name = "Password")
    private String password;

    @Column(name = "Address")
    private String address;

    @Column(name = "Telephone")
    private String telephone;

    @Column(name = "Fax")
    private String fax;

    @Column(name = "IsActive")
    private Boolean isActive;

    @Column(name = "IsPasswordTemporary")
    private Boolean isPasswordTemporary;

    @Column(name = "LoginLockDate")
    private Instant loginLockDate;

    @Column(name = "InvalidPswEntryCount")
    private Integer invalidPswEntryCount;

    @Column(name = "PasswordCreatedDate")
    private Instant passwordCreatedDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<UserRole> userRoles = new ArrayList<>();

    public ZonedDateTime getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(ZonedDateTime lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
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

    public Boolean getMailActive() {
        return mailActive;
    }

    public void setMailActive(Boolean mailActive) {
        this.mailActive = mailActive;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getPasswordTemporary() {
        return isPasswordTemporary;
    }

    public void setPasswordTemporary(Boolean passwordTemporary) {
        isPasswordTemporary = passwordTemporary;
    }

    public Integer getInvalidPswEntryCount() {
        return invalidPswEntryCount;
    }

    public void setInvalidPswEntryCount(Integer invalidPswEntryCount) {
        this.invalidPswEntryCount = invalidPswEntryCount;
    }

    public List<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    public Instant getLoginLockDate() {
        return loginLockDate;
    }

    public void setLoginLockDate(Instant loginLockDate) {
        this.loginLockDate = loginLockDate;
    }

    public Instant getPasswordCreatedDate() {
        return passwordCreatedDate;
    }

    public void setPasswordCreatedDate(Instant passwordCreatedDate) {
        this.passwordCreatedDate = passwordCreatedDate;
    }
}

