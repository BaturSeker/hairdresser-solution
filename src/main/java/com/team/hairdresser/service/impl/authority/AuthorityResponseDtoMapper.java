package com.team.hairdresser.service.impl.authority;


import com.team.hairdresser.config.mapper.GeneralMapStructConfig;
import com.team.hairdresser.domain.AuthorityEntity;
import com.team.hairdresser.dto.authority.AuthorityResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(config = GeneralMapStructConfig.class)
public interface AuthorityResponseDtoMapper {

    AuthorityResponseDtoMapper INSTANCE = Mappers.getMapper(AuthorityResponseDtoMapper.class);

    AuthorityEntity dtoToEntity(AuthorityResponseDto authorityResponseDto);

    @Mappings({
            @Mapping(source = "id", target = "authorityId"),
            @Mapping(source = "parentAuthority.id", target = "parentId")
    })
    AuthorityResponseDto entityToDto(AuthorityEntity authorityEntity);

    List<AuthorityEntity> dtoListToEntityList(List<AuthorityResponseDto> authorityResponseDtos);

    List<AuthorityResponseDto> entityListToDtoList(List<AuthorityEntity> authorities);
}