package com.team.hairdresser.dto.lookuptype;

public class LookupValueDto {

    private Integer lookupValueId;
    private Integer lookupTypeId;
    private String value;
    private Boolean isActive = Boolean.TRUE;

    public Integer getLookupValueId() {
        return lookupValueId;
    }

    public void setLookupValueId(Integer lookupValueId) {
        this.lookupValueId = lookupValueId;
    }

    public Integer getLookupTypeId() {
        return lookupTypeId;
    }

    public void setLookupTypeId(Integer lookupTypeId) {
        this.lookupTypeId = lookupTypeId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
