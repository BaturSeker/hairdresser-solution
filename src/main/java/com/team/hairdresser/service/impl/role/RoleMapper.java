package com.team.hairdresser.service.impl.role;

import com.team.hairdresser.config.mapper.GeneralMapStructConfig;
import com.team.hairdresser.domain.RoleEntity;
import com.team.hairdresser.dto.role.RoleDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(config = GeneralMapStructConfig.class)
public interface RoleMapper {

    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    RoleEntity dtoToEntity(RoleDto dto);

    RoleDto entityToDto(RoleEntity entity);

    List<RoleEntity> dtoListToEntityList(List<RoleDto> dtoList);

    List<RoleDto> entityListToDtoList(List<RoleEntity> entityList);
}
