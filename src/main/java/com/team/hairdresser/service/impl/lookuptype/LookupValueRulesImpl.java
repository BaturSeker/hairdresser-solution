package com.team.hairdresser.service.impl.lookuptype;

import com.team.hairdresser.constant.ValidationMessages;
import com.team.hairdresser.domain.lookuptype.LookupValueEntity;
import com.team.hairdresser.dto.lookuptype.LookupValueDto;
import com.team.hairdresser.service.api.lookuptype.LookupValueRules;
import com.team.hairdresser.service.api.lookuptype.LookupValueService;
import com.team.hairdresser.utils.util.ValidationHelper;
import com.team.hairdresser.utils.util.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class LookupValueRulesImpl implements LookupValueRules {
    private LookupValueService lookupValueService;

    @Override
    public void save(LookupValueDto lookupValueDto) {
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

        lookupValueService.save(lookupValueDto);
    }

    @Override
    public void update(Integer genericTypeValueId, String value) {
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

        lookupValueService.update(genericTypeValueId, value);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public LookupValueEntity read(Integer genericTypeValueId) {
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
        return lookupValueService.read(genericTypeValueId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List getComboLookupValues(Integer lookupTypeValueId) {
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
        return lookupValueService.getComboLookupValues(lookupTypeValueId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<LookupValueEntity> readAll() {
        return lookupValueService.readAll();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<LookupValueEntity> readAllByLookupTypeId(Integer genericTypeId) {
        return lookupValueService.readAllByLookupTypeId(genericTypeId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<LookupValueEntity> readAllIcons() {
        return lookupValueService.readAllIcon();
    }

    @Override
    public void changeActive(Integer genericTypeValueId) {
        lookupValueService.changeActive(genericTypeValueId);
    }

    @Autowired
    public void setLookupValueService(LookupValueService lookupValueService) {
        this.lookupValueService = lookupValueService;
    }
}

