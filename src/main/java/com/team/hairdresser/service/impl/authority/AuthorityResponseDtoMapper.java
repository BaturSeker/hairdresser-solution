package com.team.hairdresser.service.impl.authority;


import com.team.hairdresser.config.mapper.GeneralMapStructConfig;
import com.team.hairdresser.domain.AuthorityEntity;
import com.team.hairdresser.dto.authority.AuthoritySimpleResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(config = GeneralMapStructConfig.class)
public interface AuthorityResponseDtoMapper {

    AuthorityResponseDtoMapper INSTANCE = Mappers.getMapper(AuthorityResponseDtoMapper.class);

    AuthorityEntity dtoToEntity(AuthoritySimpleResponseDto authoritySimpleResponseDto);

    @Mappings({
            @Mapping(source = "id", target = "authorityId"),
            @Mapping(source = "parentAuthority.id", target = "parentId")
    })
    AuthoritySimpleResponseDto entityToDto(AuthorityEntity authorityEntity);

    List<AuthorityEntity> dtoListToEntityList(List<AuthoritySimpleResponseDto> authoritySimpleResponseDtos);

    List<AuthoritySimpleResponseDto> entityListToDtoList(List<AuthorityEntity> authorities);
}