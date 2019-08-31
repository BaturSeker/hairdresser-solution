package com.team.hairdresser.service.api.lookuptype;

import com.team.hairdresser.constant.LookupTypeEnum;
import com.team.hairdresser.domain.lookuptype.LookupValue;
import com.team.hairdresser.dto.lookuptype.LookupValueDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LookupValueService {

    List<LookupValueDto> getAllValuesByLookupTypeEnum(LookupTypeEnum typeEnum);

    Page<LookupValueDto> getValuesByLookupTypeEnum(LookupTypeEnum typeEnum, Pageable pageRequest);

    LookupValue save(LookupValueDto lookupValueDto);

    LookupValue update(Integer lookupValueId, String value);

    LookupValue read(Integer lookupValueId);

    List<LookupValue> readAll();

    List<LookupValue> readAllByLookupTypeId(Integer lookupTypeId);

    List<LookupValue> readAllIcon();

    List getComboLookupValues(Integer lookupTypeId);

    void changeActive(Integer lookupValueId);

}
