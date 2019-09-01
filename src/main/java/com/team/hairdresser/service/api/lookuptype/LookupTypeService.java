package com.team.hairdresser.service.api.lookuptype;

import com.team.hairdresser.domain.lookuptype.LookupTypeEntity;
import com.team.hairdresser.dto.lookuptype.LookupTypeDto;
import com.team.hairdresser.utils.pageablesearch.model.PageableSearchFilterDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LookupTypeService {

    LookupTypeEntity save(LookupTypeDto lookupTypeDto);

    LookupTypeEntity getLookupType(Integer lookupTypeId);

    LookupTypeEntity read(Integer genericTypeId);

    List<LookupTypeEntity> readAll();

    Page<LookupTypeDto> getAll(PageableSearchFilterDto filterDto);

    LookupTypeEntity update(Integer lookupTypeId, LookupTypeDto lookupTypeDto);

}

