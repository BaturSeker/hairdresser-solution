package com.team.hairdresser.service.impl.authority;


import com.team.hairdresser.config.mapper.GeneralMapStructConfig;
import com.team.hairdresser.domain.AuthorityEntity;
import com.team.hairdresser.dto.authority.AuthorityResponseDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(config = GeneralMapStructConfig.class)
public interface AuthorityMapper {

    AuthorityMapper INSTANCE = Mappers.getMapper(AuthorityMapper.class);

    @InheritInverseConfiguration
    AuthorityEntity dtoToEntity(AuthorityResponseDto authorityResponseDto);

    @Mappings({
            @Mapping(source = "id", target = "authorityId"),
    })
    AuthorityResponseDto entityToDto(AuthorityEntity authorityEntity);

    List<AuthorityEntity> dtoListToEntityList(List<AuthorityResponseDto> authorityResponsDtos);

    List<AuthorityResponseDto> entityListToDtoList(List<AuthorityEntity> authorities);
}