package com.team.hairdresser.domain.lookuptype;


import com.team.hairdresser.utils.abstracts.BaseEntity;

import javax.persistence.*;

@Entity
public class LookupValueEntity extends BaseEntity<Integer> {

    @Column(name = "Value")
    private String value;

    @Column(name = "IsActive")
    private Boolean isActive = Boolean.TRUE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LookupTypeId")
    private LookupTypeEntity lookupTypeEntity;

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String trValue) {
        this.value = trValue;
    }

    public Integer getLookupValueId() {
        return this.getId();
    }

    public void setLookupValueId(Integer genericTypeValueId) {
        this.setId(genericTypeValueId);
    }

    public LookupTypeEntity getLookupTypeEntity() {
        return lookupTypeEntity;
    }

    public void setLookupTypeEntity(LookupTypeEntity lookupTypeEntity) {
        this.lookupTypeEntity = lookupTypeEntity;
    }
}

