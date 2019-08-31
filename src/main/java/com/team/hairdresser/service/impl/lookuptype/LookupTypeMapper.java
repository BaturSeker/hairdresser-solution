package com.team.hairdresser.service.impl.lookuptype;

import com.team.hairdresser.config.mapper.GeneralMapStructConfig;
import com.team.hairdresser.domain.lookuptype.LookupTypeEntity;
import com.team.hairdresser.dto.lookuptype.LookupTypeDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(config = GeneralMapStructConfig.class, uses = LookupValueMapper.class)
public interface LookupTypeMapper {

    LookupTypeMapper INSTANCE = Mappers.getMapper(LookupTypeMapper.class);

    LookupTypeEntity dtoToEntity(LookupTypeDto lookupTypeDto);

    @Mapping(source = "typeEnum.lookupTypeEnumId", target = "lookupTypeEnumId")
    LookupTypeDto entityToDto(LookupTypeEntity lookupTypeEntity);

    List<LookupTypeEntity> dtoListToEntityList(List<LookupTypeDto> lookupTypeDtos);

    List<LookupTypeDto> entityListToDtoList(List<LookupTypeEntity> lookupTypeEntities);
}