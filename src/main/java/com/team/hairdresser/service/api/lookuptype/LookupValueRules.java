package com.team.hairdresser.service.api.lookuptype;


import com.team.hairdresser.domain.lookuptype.LookupValueEntity;
import com.team.hairdresser.dto.lookuptype.LookupValueDto;

import java.util.List;

public interface LookupValueRules {

    void save(LookupValueDto lookupValueDto);

    void update(Integer lookupValueId, String value);

    LookupValueEntity read(Integer lookupValueId);

    List getComboLookupValues(Integer lookupTypeValueId);

    List<LookupValueEntity> readAll();

    List<LookupValueEntity> readAllByLookupTypeId(Integer lookupTypeId);

    List<LookupValueEntity> readAllIcons();

    void changeActive(Integer lookupValueId);
}
