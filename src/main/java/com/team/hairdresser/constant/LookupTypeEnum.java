package com.team.hairdresser.constant;


import com.fasterxml.jackson.annotation.JsonCreator;

public enum LookupTypeEnum {
    SIGNBOARD(1, "Tabela"),
    ICON(2, "Icon"),
    TARIFF_VEHICLE_TYPE(3, "Tarife Araç Tipi");

    private Integer lookupTypeEnumId;
    private String name;

    LookupTypeEnum(Integer lookupTypeEnumId, String name) {
        this.lookupTypeEnumId = lookupTypeEnumId;
        this.name = name;
    }

    @JsonCreator
    public static LookupTypeEnum fromLookupTypeEnumId(Integer id) {
        for (LookupTypeEnum type : values()) {
            if (type.getLookupTypeEnumId().equals(id)) {
                return type;
            }
        }
        return null;
    }

    public Integer getLookupTypeEnumId() {
        return lookupTypeEnumId;
    }

    public String getName() {
        return name;
    }

}
