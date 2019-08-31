package com.team.hairdresser.service.api.lookuptype;

import com.team.hairdresser.domain.lookuptype.LookupTypeEntity;
import com.team.hairdresser.dto.lookuptype.LookupTypeDto;
import com.team.hairdresser.utils.pageablesearch.model.PageableSearchFilterDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LookupTypeRules {

    void save(LookupTypeDto lookupTypeDto);

    void update(Integer lookupTypeId, LookupTypeDto lookupTypeDto);

    LookupTypeEntity read(Integer lookupTypeId);

    List<LookupTypeEntity> readAll();

    Page<LookupTypeDto> getAll(PageableSearchFilterDto pageRequest);
}

