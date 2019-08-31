package com.team.hairdresser.dao;


import com.team.hairdresser.domain.lookuptype.LookupType;
import com.team.hairdresser.utils.abstracts.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LookupTypeRepository extends BaseRepository<LookupType, Integer> {
    LookupType findByName(String name);
}

