package com.team.hairdresser.service.impl.authority;


import com.team.hairdresser.config.mapper.GeneralMapStructConfig;
import com.team.hairdresser.domain.Authority;
import com.team.hairdresser.dto.authority.AuthorityResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(config = GeneralMapStructConfig.class)
public interface AuthorityResponseDtoMapper {

    AuthorityResponseDtoMapper INSTANCE = Mappers.getMapper(AuthorityResponseDtoMapper.class);

    Authority dtoToEntity(AuthorityResponseDto authorityResponseDto);

    @Mappings({
            @Mapping(source = "id", target = "authorityId"),
            @Mapping(source = "parentAuthority.id", target = "parentId")
    })
    AuthorityResponseDto entityToDto(Authority authority);

    List<Authority> dtoListToEntityList(List<AuthorityResponseDto> authorityResponseDtos);

    List<AuthorityResponseDto> entityListToDtoList(List<Authority> authorities);
}