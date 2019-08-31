package com.team.hairdresser.service.impl.role;

import com.team.hairdresser.config.mapper.GeneralMapStructConfig;
import com.team.hairdresser.domain.Roles;
import com.team.hairdresser.dto.role.RoleDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(config = GeneralMapStructConfig.class)
public interface RoleMapper {

    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    Roles dtoToEntity(RoleDto dto);

    RoleDto entityToDto(Roles entity);

    List<Roles> dtoListToEntityList(List<RoleDto> dtoList);

    List<RoleDto> entityListToDtoList(List<Roles> entityList);
}
