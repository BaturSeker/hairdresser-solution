package com.team.hairdresser.dto.lookuptype;

import java.util.List;

public class LookupTypeWithValuesDto {

    private LookupTypeDto lookupTypeDto;
    private List<LookupValueDto> lookupValueDtos;

    public LookupTypeDto getLookupTypeDto() {
        return lookupTypeDto;
    }

    public void setLookupTypeDto(LookupTypeDto lookupTypeDto) {
        this.lookupTypeDto = lookupTypeDto;
    }

    public List<LookupValueDto> getLookupValueDtos() {
        return lookupValueDtos;
    }

    public void setLookupValueDtos(List<LookupValueDto> lookupValueDtos) {
        this.lookupValueDtos = lookupValueDtos;
    }
}