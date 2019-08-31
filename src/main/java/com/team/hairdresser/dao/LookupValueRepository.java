package com.team.hairdresser.dao;


import com.team.hairdresser.constant.LookupTypeEnum;
import com.team.hairdresser.domain.lookuptype.LookupType;
import com.team.hairdresser.domain.lookuptype.LookupValue;
import com.team.hairdresser.utils.abstracts.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LookupValueRepository extends BaseRepository<LookupValue, Integer> {
    List<LookupValue> findAllByLookupType(LookupType lookupType);

    List<LookupValue> findAllByLookupTypeTypeEnum(LookupTypeEnum lookupTypeEnum);

    Page<LookupValue> findAllByLookupTypeTypeEnum(LookupTypeEnum lookupTypeEnum, Pageable pageRequest);

    LookupValue findByLookupTypeAndValue(LookupType lookupType, String value);

    @Query(nativeQuery = true, value = "select LookupValueId as VALUE, Value as TEXT from LookupValue WHERE LookupTypeId=?1")
    List<Object[]> getComboLookupValues(Integer lookupTypeId);

    List<LookupValue> findByValue(String value);

    LookupValue getByValue(String Values);
}

