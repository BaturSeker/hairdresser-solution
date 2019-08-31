package com.team.hairdresser.service.api.lookuptype;


import com.team.hairdresser.domain.lookuptype.LookupValue;
import com.team.hairdresser.dto.lookuptype.LookupValueDto;

import java.util.List;

public interface LookupValueRules {

    void save(LookupValueDto lookupValueDto);

    void update(Integer lookupValueId, String value);

    LookupValue read(Integer lookupValueId);

    List getComboLookupValues(Integer lookupTypeValueId);

    List<LookupValue> readAll();

    List<LookupValue> readAllByLookupTypeId(Integer lookupTypeId);

    List<LookupValue> readAllIcons();

    void changeActive(Integer lookupValueId);
}
