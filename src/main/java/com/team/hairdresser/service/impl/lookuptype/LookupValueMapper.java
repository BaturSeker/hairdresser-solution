package com.team.hairdresser.service.impl.lookuptype;

import com.team.hairdresser.config.mapper.GeneralMapStructConfig;
import com.team.hairdresser.domain.lookuptype.LookupValueEntity;
import com.team.hairdresser.dto.lookuptype.LookupValueDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(config = GeneralMapStructConfig.class)
public interface LookupValueMapper {

    LookupValueMapper INSTANCE = Mappers.getMapper(LookupValueMapper.class);

    LookupValueEntity dtoToEntity(LookupValueDto lookupValueDto);

    @Mapping(source = "lookupType.lookupTypeId", target = "lookupTypeId")
    LookupValueDto entityToDto(LookupValueEntity lookupValueEntity);

    List<LookupValueEntity> dtoListToEntityList(List<LookupValueDto> lookupValueDtos);

    List<LookupValueDto> entityListToDtoList(List<LookupValueEntity> lookupValueEntities);

    default Page<LookupValueDto> entityPageToDtoPage(Page<LookupValueEntity> lookupValues) {
        return lookupValues.map(this::entityToDto);
    }
}
