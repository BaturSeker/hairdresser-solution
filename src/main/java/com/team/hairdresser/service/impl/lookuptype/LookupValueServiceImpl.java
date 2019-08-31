package com.team.hairdresser.service.impl.lookuptype;


import com.team.hairdresser.constant.AuthorityCodes;
import com.team.hairdresser.constant.LookupTypeEnum;
import com.team.hairdresser.dao.LookupTypeRepository;
import com.team.hairdresser.dao.LookupValueRepository;
import com.team.hairdresser.domain.lookuptype.LookupTypeEntity;
import com.team.hairdresser.domain.lookuptype.LookupValueEntity;
import com.team.hairdresser.dto.lookuptype.LookupValueDto;
import com.team.hairdresser.service.api.lookuptype.LookupValueService;
import com.team.hairdresser.utils.util.ComboResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LookupValueServiceImpl implements LookupValueService {

    private LookupValueRepository lookupValueRepository;
    private LookupTypeRepository lookupTypeRepository;

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_LOOKUP_VALUE_MANAGEMENT + "')")
    public List<LookupValueDto> getAllValuesByLookupTypeEnum(LookupTypeEnum typeEnum) {
        List<LookupValueEntity> values = lookupValueRepository.findAllByLookupTypeEntityTypeEnum(typeEnum);
        return LookupValueMapper.INSTANCE.entityListToDtoList(values);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_LOOKUP_VALUE_MANAGEMENT + "')")
    public Page<LookupValueDto> getValuesByLookupTypeEnum(LookupTypeEnum typeEnum, Pageable pageRequest) {
        Page<LookupValueEntity> values = lookupValueRepository.findAllByLookupTypeEntityTypeEnum(typeEnum, pageRequest);
        return LookupValueMapper.INSTANCE.entityPageToDtoPage(values);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.CREATE_LOOKUP_VALUE + "')")
    public LookupValueEntity save(LookupValueDto lookupValueDto) {
        LookupValueEntity lookupValueEntity = new LookupValueEntity();
        lookupValueEntity.setValue(lookupValueDto.getValue());
        LookupTypeEntity lookupTypeEntity = lookupTypeRepository.getOne(lookupValueDto.getLookupTypeId());
        lookupValueEntity.setLookupTypeEntity(lookupTypeEntity);
        return lookupValueRepository.saveAndFlush(lookupValueEntity);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.UPDATE_LOOKUP_VALUE + "')")
    public LookupValueEntity update(Integer lookupValueId, String value) {
        LookupValueEntity lookupValueEntity = this.lookupValueRepository.getOne(lookupValueId);
        lookupValueEntity.setValue(value);
        return this.lookupValueRepository.saveAndFlush(lookupValueEntity);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_LOOKUP_VALUE_MANAGEMENT + "')")
    public LookupValueEntity read(Integer lookupValueId) {
        return this.lookupValueRepository.getOne(lookupValueId);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_LOOKUP_VALUE_MANAGEMENT + "')")
    public List<LookupValueEntity> readAll() {
        return this.lookupValueRepository.findAll();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_LOOKUP_VALUE_MANAGEMENT + "')")
    public List<LookupValueEntity> readAllByLookupTypeId(Integer lookupTypeId) {
        LookupTypeEntity lookupTypeEntity = lookupTypeRepository.getOne(lookupTypeId);
        return this.lookupValueRepository.findAllByLookupTypeEntity(lookupTypeEntity);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_LOOKUP_VALUE_MANAGEMENT + "')")
    public List<LookupValueEntity> readAllIcon() {
        LookupTypeEntity lookupTypeEntity = lookupTypeRepository.findByName("Icon");
        return lookupValueRepository.findAllByLookupTypeEntity(lookupTypeEntity);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_LOOKUP_VALUE_MANAGEMENT + "')")
    public List getComboLookupValues(Integer lookupTypeId) {
        List<Object[]> resultList = this.lookupValueRepository.getComboLookupValues(lookupTypeId);
        return ComboResponseBuilder.buildComboResponseList(resultList);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.UPDATE_LOOKUP_VALUE + "')")
    public void changeActive(Integer lookupValueId) {
        LookupValueEntity lookupValueEntity = this.lookupValueRepository.getOne(lookupValueId);
        Boolean active = lookupValueEntity.getActive();
        lookupValueEntity.setActive(!active);
        this.lookupValueRepository.save(lookupValueEntity);
    }

    @Autowired
    public void setLookupValueRepository(LookupValueRepository lookupValueRepository) {
        this.lookupValueRepository = lookupValueRepository;
    }

    @Autowired
    public void setLookupTypeRepository(LookupTypeRepository lookupTypeRepository) {
        this.lookupTypeRepository = lookupTypeRepository;
    }
}
