package com.team.hairdresser.service.impl.lookuptype;


import com.team.hairdresser.constant.AuthorityCodes;
import com.team.hairdresser.constant.LookupTypeEnum;
import com.team.hairdresser.constant.ValidationMessages;
import com.team.hairdresser.dao.LookupTypeRepository;
import com.team.hairdresser.dao.LookupValueRepository;
import com.team.hairdresser.domain.lookuptype.LookupTypeEntity;
import com.team.hairdresser.domain.lookuptype.LookupValueEntity;
import com.team.hairdresser.dto.lookuptype.LookupValueDto;
import com.team.hairdresser.service.api.lookuptype.LookupValueService;
import com.team.hairdresser.utils.util.ComboResponseBuilder;
import com.team.hairdresser.utils.util.ValidationHelper;
import com.team.hairdresser.utils.util.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class LookupValueServiceImpl implements LookupValueService {

    private LookupValueRepository lookupValueRepository;
    private LookupTypeRepository lookupTypeRepository;

    @Autowired
    public LookupValueServiceImpl(LookupValueRepository lookupValueRepository, LookupTypeRepository lookupTypeRepository) {
        this.lookupValueRepository = lookupValueRepository;
        this.lookupTypeRepository = lookupTypeRepository;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_LOOKUP_VALUE_MANAGEMENT + "')")
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<LookupValueDto> getAllValuesByLookupTypeEnum(LookupTypeEnum typeEnum) {
        List<LookupValueEntity> values = lookupValueRepository.findAllByLookupTypeEntityTypeEnum(typeEnum);
        return LookupValueMapper.INSTANCE.entityListToDtoList(values);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_LOOKUP_VALUE_MANAGEMENT + "')")
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Page<LookupValueDto> getValuesByLookupTypeEnum(LookupTypeEnum typeEnum, Pageable pageRequest) {
        Page<LookupValueEntity> values = lookupValueRepository.findAllByLookupTypeEntityTypeEnum(typeEnum, pageRequest);
        return LookupValueMapper.INSTANCE.entityPageToDtoPage(values);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.CREATE_LOOKUP_VALUE + "')")
    public LookupValueEntity save(LookupValueDto lookupValueDto) {
        controlSave(lookupValueDto);

        LookupValueEntity lookupValueEntity = new LookupValueEntity();
        lookupValueEntity.setValue(lookupValueDto.getValue());
        LookupTypeEntity lookupTypeEntity = lookupTypeRepository.getOne(lookupValueDto.getLookupTypeId());
        lookupValueEntity.setLookupTypeEntity(lookupTypeEntity);
        return lookupValueRepository.saveAndFlush(lookupValueEntity);
    }

    private void controlSave(LookupValueDto lookupValueDto) {
        StringBuilder messages = new StringBuilder();
        boolean isValid = true;
        if (!ValidationHelper.notNull(lookupValueDto)) {
            isValid = false;
            messages.append(ValidationMessages.LOOKUP_TYPE_VALUE);
            messages.append(System.lineSeparator());
        }
        if (!ValidationHelper.notEmpty(lookupValueDto.getValue())) {
            isValid = false;
            messages.append(ValidationMessages.LOOKUP_TYPE_VALUE_DESCRIPTION);
            messages.append(System.lineSeparator());
        }
        if (!ValidationHelper.notEmpty(lookupValueDto.getLookupTypeId())) {
            isValid = false;
            messages.append(ValidationMessages.LOOKUP_TYPE_VALUE_LOOKUP_TYPE_ID_NOT_NULL);
            messages.append(System.lineSeparator());
        }
        if (!isValid) {
            throw new ValidationException(messages.toString());
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.UPDATE_LOOKUP_VALUE + "')")
    public LookupValueEntity update(Integer genericTypeValueId, String value) {
        controlUpdate(genericTypeValueId, value);

        LookupValueEntity lookupValueEntity = this.lookupValueRepository.getOne(genericTypeValueId);
        lookupValueEntity.setValue(value);
        return this.lookupValueRepository.saveAndFlush(lookupValueEntity);
    }

    private void controlUpdate(Integer genericTypeValueId, String value) {
        StringBuilder messages = new StringBuilder();
        boolean isValid = true;
        if (!ValidationHelper.notEmpty(value)) {
            isValid = false;
            messages.append(ValidationMessages.LOOKUP_TYPE_VALUE_NAME);
            messages.append(System.lineSeparator());
        }
        if (!ValidationHelper.notEmpty(genericTypeValueId)) {
            isValid = false;
            messages.append(ValidationMessages.LOOKUP_TYPE_ID);
            messages.append(System.lineSeparator());
        }
        if (!isValid) {
            throw new ValidationException(messages.toString());
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_LOOKUP_VALUE_MANAGEMENT + "')")
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public LookupValueEntity read(Integer genericTypeValueId) {
        controlRead(genericTypeValueId);
        return this.lookupValueRepository.getOne(genericTypeValueId);
    }

    private void controlRead(Integer genericTypeValueId) {
        StringBuilder messages = new StringBuilder();
        boolean isValid = true;
        if (!ValidationHelper.notEmpty(genericTypeValueId)) {
            isValid = false;
            messages.append(ValidationMessages.LOOKUP_TYPE_VALUE_ID);
            messages.append(System.lineSeparator());
        }
        if (!isValid) {
            throw new ValidationException(messages.toString());
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_LOOKUP_VALUE_MANAGEMENT + "')")
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<LookupValueEntity> readAll() {
        return this.lookupValueRepository.findAll();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_LOOKUP_VALUE_MANAGEMENT + "')")
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<LookupValueEntity> readAllByLookupTypeId(Integer lookupTypeId) {
        LookupTypeEntity lookupTypeEntity = lookupTypeRepository.getOne(lookupTypeId);
        return this.lookupValueRepository.findAllByLookupTypeEntity(lookupTypeEntity);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_LOOKUP_VALUE_MANAGEMENT + "')")
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<LookupValueEntity> readAllIcon() {
        LookupTypeEntity lookupTypeEntity = lookupTypeRepository.findByName("Icon");
        return lookupValueRepository.findAllByLookupTypeEntity(lookupTypeEntity);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_LOOKUP_VALUE_MANAGEMENT + "')")
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List getComboLookupValues(Integer lookupTypeId) {
        controlComboLookupValues(lookupTypeId);
        List<Object[]> resultList = this.lookupValueRepository.getComboLookupValues(lookupTypeId);
        return ComboResponseBuilder.buildComboResponseList(resultList);
    }

    private void controlComboLookupValues(Integer lookupTypeValueId) {
        StringBuilder messages = new StringBuilder();
        boolean isValid = true;
        if (!ValidationHelper.notEmpty(lookupTypeValueId)) {
            isValid = false;
            messages.append(ValidationMessages.LOOKUP_TYPE_VALUE_ID);
            messages.append(System.lineSeparator());
        }
        if (!isValid) {
            throw new ValidationException(messages.toString());
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.UPDATE_LOOKUP_VALUE + "')")
    public void changeActive(Integer lookupValueId) {
        LookupValueEntity lookupValueEntity = this.lookupValueRepository.getOne(lookupValueId);
        Boolean active = lookupValueEntity.getActive();
        lookupValueEntity.setActive(!active);
        this.lookupValueRepository.save(lookupValueEntity);
    }

}
