package com.team.hairdresser.domain.lookuptype;


import com.team.hairdresser.constant.LookupTypeEnum;
import com.team.hairdresser.utils.abstracts.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class LookupType extends BaseEntity<Integer> {

    @Column(name = "name")
    private String name;

    @Column(name = "TypeEnum")
    private LookupTypeEnum typeEnum;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "lookupType", cascade = CascadeType.ALL)
    private List<LookupValue> lookupValues = new ArrayList<>();

    public Integer getLookupTypeId() {
        return this.getId();
    }

    public void setLookupTypeId(Integer lookupTypeId) {
        this.setId(lookupTypeId);
    }

    public String getName() {
        return name;
    }

    public void setName(String type) {
        this.name = type;
    }

    public List<LookupValue> getLookupValues() {
        return lookupValues;
    }

    public void setLookupValues(List<LookupValue> lookupValues) {
        this.lookupValues = lookupValues;
    }

    public LookupTypeEnum getTypeEnum() {
        return typeEnum;
    }

    public void setTypeEnum(LookupTypeEnum typeEnum) {
        this.typeEnum = typeEnum;
    }
}
