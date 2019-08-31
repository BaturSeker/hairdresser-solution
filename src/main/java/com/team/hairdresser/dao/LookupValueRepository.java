package com.team.hairdresser.dao;


import com.team.hairdresser.constant.LookupTypeEnum;
import com.team.hairdresser.domain.lookuptype.LookupTypeEntity;
import com.team.hairdresser.domain.lookuptype.LookupValueEntity;
import com.team.hairdresser.utils.abstracts.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LookupValueRepository extends BaseRepository<LookupValueEntity, Integer> {
    List<LookupValueEntity> findAllByLookupTypeEntity(LookupTypeEntity lookupTypeEntity);

    List<LookupValueEntity> findAllByLookupTypeEntityTypeEnum(LookupTypeEnum lookupTypeEnum);

    Page<LookupValueEntity> findAllByLookupTypeEntityTypeEnum(LookupTypeEnum lookupTypeEnum, Pageable pageRequest);

    LookupValueEntity findByLookupTypeEntityAndValue(LookupTypeEntity lookupTypeEntity, String value);

    @Query(nativeQuery = true, value = "select LookupValueId as VALUE, Value as TEXT from LookupValue WHERE LookupTypeId=?1")
    List<Object[]> getComboLookupValues(Integer lookupTypeId);

    List<LookupValueEntity> findByValue(String value);

    LookupValueEntity getByValue(String value);
}

