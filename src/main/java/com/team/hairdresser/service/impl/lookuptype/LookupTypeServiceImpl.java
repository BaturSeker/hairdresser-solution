package com.team.hairdresser.service.impl.lookuptype;


import com.team.hairdresser.constant.LookupTypeEnum;
import com.team.hairdresser.dao.LookupTypeRepository;
import com.team.hairdresser.dao.LookupValueRepository;
import com.team.hairdresser.domain.lookuptype.LookupType;
import com.team.hairdresser.domain.lookuptype.LookupValue;
import com.team.hairdresser.dto.lookuptype.LookupTypeDto;
import com.team.hairdresser.dto.lookuptype.LookupValueDto;
import com.team.hairdresser.service.api.lookuptype.LookupTypeService;
import com.team.hairdresser.utils.pageablesearch.model.PageRequestDto;
import com.team.hairdresser.utils.pageablesearch.model.PageableSearchFilterDto;
import com.team.hairdresser.utils.pageablesearch.specification.SearchSpecificationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
public class LookupTypeServiceImpl implements LookupTypeService {

    private LookupTypeRepository lookupTypeRepository;
    private LookupValueRepository lookupValueRepository;

    @Override
    public LookupType save(LookupTypeDto lookupTypeDto) {
        LookupType lookupType = new LookupType();
        lookupType.setName(lookupTypeDto.getName());
        lookupType.setTypeEnum(LookupTypeEnum.fromLookupTypeEnumId(lookupTypeDto.getLookupTypeEnumId()));

        for (LookupValueDto valueDto : lookupTypeDto.getLookupValues()) {
            LookupValue lookupValue = LookupValueMapper.INSTANCE.dtoToEntity(valueDto);
            lookupValue.setLookupType(lookupType);
            lookupType.getLookupValues().add(lookupValue);
        }

        return lookupTypeRepository.saveAndFlush(lookupType);
    }

    @Override
    public LookupType getLookupType(Integer lookupTypeId) {
        return this.lookupTypeRepository.getOne(lookupTypeId);
    }

    @Override
    public List<LookupType> readAll() {
        return this.lookupTypeRepository.findAll();
    }

    @Override
    public Page<LookupTypeDto> getAll(PageableSearchFilterDto filterDto) {

        SearchSpecificationBuilder<LookupType> specificationBuilder =
                SearchSpecificationBuilder.filterAllowedKeysInstance(LookupType.class,
                        "name");

        Specification<LookupType> builtSpecification = specificationBuilder.build(filterDto.getCriteriaList());

        PageRequestDto pageRequest = filterDto.getPageRequest();

        PageRequest pageable = pageRequest.getSpringPageRequest();

        Page<LookupType> lookupTypePageResult = lookupTypeRepository.findAll(builtSpecification, pageable);

        List<LookupTypeDto> lookupTypePageDtos = new ArrayList<>();

        long totalCount = lookupTypePageResult.getTotalElements();
        buildResultList(lookupTypePageResult, lookupTypePageDtos);

        return new PageImpl<>(lookupTypePageDtos, pageable, totalCount);
    }

    private void buildResultList(Page<LookupType> lookupTypePageResult, List<LookupTypeDto> lookupTypePageDtos) {
        for (LookupType lookupType : lookupTypePageResult) {
            LookupTypeDto lookupTypeDto = new LookupTypeDto();

            lookupTypeDto.setLookupTypeId(lookupType.getLookupTypeId());
            lookupTypeDto.setName(lookupType.getName());
            lookupTypeDto.setLookupTypeEnumId(Objects.nonNull(lookupType.getTypeEnum()) ? lookupType.getTypeEnum().getLookupTypeEnumId() : null);

            List<LookupValueDto> lookupValueDtos = new ArrayList<>();

            for (LookupValue lookupValue : lookupType.getLookupValues()) {

                LookupValueDto lookupValueDto = new LookupValueDto();
                lookupValueDto.setLookupValueId(lookupValue.getLookupValueId());
                lookupValueDto.setLookupTypeId(Objects.nonNull(lookupValue.getLookupType()) ? lookupValue.getLookupType().getLookupTypeId() : null);
                lookupValueDto.setValue(lookupValue.getValue());
                lookupValueDto.setActive(lookupValue.getActive());

                lookupValueDtos.add(lookupValueDto);

            }

            lookupTypePageDtos.add(lookupTypeDto);
        }
    }

    @Override
    public LookupType update(Integer lookupTypeId, LookupTypeDto lookupTypeDto) {
        LookupType lookupType = this.lookupTypeRepository.getOne(lookupTypeId);
        lookupType.setName(lookupTypeDto.getName());
        return this.lookupTypeRepository.saveAndFlush(lookupType);
    }

    @Autowired
    public void setLookupTypeRepository(LookupTypeRepository lookupTypeRepository) {
        this.lookupTypeRepository = lookupTypeRepository;
    }

    @Autowired
    public void setLookupValueRepository(LookupValueRepository lookupValueRepository) {
        this.lookupValueRepository = lookupValueRepository;
    }
}


