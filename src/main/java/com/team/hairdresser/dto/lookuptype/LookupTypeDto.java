package com.team.hairdresser.dto.lookuptype;


import java.util.ArrayList;
import java.util.List;

public class LookupTypeDto {

    private Integer lookupTypeId;
    private String name;
    private Integer lookupTypeEnumId;
    private List<LookupValueDto> lookupValues = new ArrayList<>();

    public Integer getLookupTypeId() {
        return lookupTypeId;
    }

    public void setLookupTypeId(Integer lookupTypeId) {
        this.lookupTypeId = lookupTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLookupTypeEnumId() {
        return lookupTypeEnumId;
    }

    public void setLookupTypeEnumId(Integer lookupTypeEnumId) {
        this.lookupTypeEnumId = lookupTypeEnumId;
    }

    public List<LookupValueDto> getLookupValues() {
        return lookupValues;
    }

    public void setLookupValues(List<LookupValueDto> lookupValues) {
        this.lookupValues = lookupValues;
    }
}
