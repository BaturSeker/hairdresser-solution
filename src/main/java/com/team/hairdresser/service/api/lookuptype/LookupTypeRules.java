package com.team.hairdresser.service.api.lookuptype;

import com.team.hairdresser.domain.lookuptype.LookupType;
import com.team.hairdresser.dto.lookuptype.LookupTypeDto;
import com.team.hairdresser.utils.pageablesearch.model.PageableSearchFilterDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LookupTypeRules {

    void save(LookupTypeDto lookupTypeDto);

    void update(Integer lookupTypeId, LookupTypeDto lookupTypeDto);

    LookupType read(Integer lookupTypeId);

    List<LookupType> readAll();

    Page<LookupTypeDto> getAll(PageableSearchFilterDto pageRequest);
}

