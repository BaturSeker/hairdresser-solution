package com.team.hairdresser.domain.lookuptype;


import com.team.hairdresser.constant.LookupTypeEnum;
import com.team.hairdresser.utils.abstracts.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class LookupTypeEntity extends BaseEntity<Integer> {

    @Column(name = "name")
    private String name;

    @Column(name = "TypeEnum")
    private LookupTypeEnum typeEnum;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "lookupTypeEntity", cascade = CascadeType.ALL)
    private List<LookupValueEntity> lookupValueEntities = new ArrayList<>();

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

    public List<LookupValueEntity> getLookupValueEntities() {
        return lookupValueEntities;
    }

    public void setLookupValueEntities(List<LookupValueEntity> lookupValueEntities) {
        this.lookupValueEntities = lookupValueEntities;
    }

    public LookupTypeEnum getTypeEnum() {
        return typeEnum;
    }

    public void setTypeEnum(LookupTypeEnum typeEnum) {
        this.typeEnum = typeEnum;
    }
}
