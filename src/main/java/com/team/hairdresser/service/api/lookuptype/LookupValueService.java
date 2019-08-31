package com.team.hairdresser.service.api.lookuptype;

import com.team.hairdresser.constant.LookupTypeEnum;
import com.team.hairdresser.domain.lookuptype.LookupValueEntity;
import com.team.hairdresser.dto.lookuptype.LookupValueDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LookupValueService {

    List<LookupValueDto> getAllValuesByLookupTypeEnum(LookupTypeEnum typeEnum);

    Page<LookupValueDto> getValuesByLookupTypeEnum(LookupTypeEnum typeEnum, Pageable pageRequest);

    LookupValueEntity save(LookupValueDto lookupValueDto);

    LookupValueEntity update(Integer lookupValueId, String value);

    LookupValueEntity read(Integer lookupValueId);

    List<LookupValueEntity> readAll();

    List<LookupValueEntity> readAllByLookupTypeId(Integer lookupTypeId);

    List<LookupValueEntity> readAllIcon();

    List getComboLookupValues(Integer lookupTypeId);

    void changeActive(Integer lookupValueId);

}
