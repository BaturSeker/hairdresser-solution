package com.team.hairdresser.service.impl.lookuptype;


import com.team.hairdresser.constant.ValidationMessages;
import com.team.hairdresser.domain.lookuptype.LookupType;
import com.team.hairdresser.dto.lookuptype.LookupTypeDto;
import com.team.hairdresser.service.api.lookuptype.LookupTypeRules;
import com.team.hairdresser.service.api.lookuptype.LookupTypeService;
import com.team.hairdresser.utils.pageablesearch.model.PageableSearchFilterDto;
import com.team.hairdresser.utils.util.ValidationHelper;
import com.team.hairdresser.utils.util.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class LookupTypeRulesImpl implements LookupTypeRules {

    private LookupTypeService lookupTypeService;

    @Override
    public void save(LookupTypeDto lookupTypeDto) {
        StringBuilder messages = new StringBuilder();
        boolean isValid = true;
        if (!ValidationHelper.notEmpty(lookupTypeDto.getName())) {
            isValid = false;
            messages.append(ValidationMessages.LOOKUP_TYPE_TYPE);
            messages.append(System.lineSeparator());
        }

        if (!isValid) {
            throw new ValidationException(messages.toString());
        }

        lookupTypeService.save(lookupTypeDto);
    }

    @Override
    public void update(Integer genericTypeId, LookupTypeDto lookupTypeDto) {
        StringBuilder messages = new StringBuilder();
        boolean isValid = true;
        if (!ValidationHelper.notEmpty(lookupTypeDto.getName())) {
            isValid = false;
            messages.append(ValidationMessages.LOOKUP_TYPE_TYPE);
            messages.append(System.lineSeparator());
        }
        if (!ValidationHelper.notEmpty(genericTypeId)) {
            isValid = false;
            messages.append(ValidationMessages.LOOKUP_TYPE_ID);
            messages.append(System.lineSeparator());
        }
        if (!isValid) {
            throw new ValidationException(messages.toString());
        }
        lookupTypeService.update(genericTypeId, lookupTypeDto);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public LookupType read(Integer genericTypeId) {
        StringBuilder messages = new StringBuilder();
        boolean isValid = true;
        if (!ValidationHelper.notEmpty(genericTypeId)) {
            isValid = false;
            messages.append(ValidationMessages.LOOKUP_TYPE_ID);
            messages.append(System.lineSeparator());
        }
        if (!isValid) {
            throw new ValidationException(messages.toString());
        }
        return lookupTypeService.getLookupType(genericTypeId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<LookupType> readAll() {
        return lookupTypeService.readAll();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Page<LookupTypeDto> getAll(PageableSearchFilterDto pageRequest) {
        return lookupTypeService.getAll(pageRequest);
    }

    @Autowired
    public void setLookupTypeService(LookupTypeService lookupTypeService) {
        this.lookupTypeService = lookupTypeService;
    }
}

