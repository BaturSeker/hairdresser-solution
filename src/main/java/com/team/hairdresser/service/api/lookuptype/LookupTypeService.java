package com.team.hairdresser.service.api.lookuptype;

import com.team.hairdresser.domain.lookuptype.LookupType;
import com.team.hairdresser.dto.lookuptype.LookupTypeDto;
import com.team.hairdresser.utils.pageablesearch.model.PageableSearchFilterDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LookupTypeService {

    LookupType save(LookupTypeDto lookupTypeDto);

    LookupType getLookupType(Integer lookupTypeId);

    List<LookupType> readAll();

    Page<LookupTypeDto> getAll(PageableSearchFilterDto filterDto);

    LookupType update(Integer lookupTypeId, LookupTypeDto lookupTypeDto);

}

