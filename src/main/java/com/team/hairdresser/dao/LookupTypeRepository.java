package com.team.hairdresser.dao;


import com.team.hairdresser.domain.lookuptype.LookupTypeEntity;
import com.team.hairdresser.utils.abstracts.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LookupTypeRepository extends BaseRepository<LookupTypeEntity, Integer> {
    LookupTypeEntity findByName(String name);
}

