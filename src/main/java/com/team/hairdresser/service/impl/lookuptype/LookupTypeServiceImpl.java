package com.team.hairdresser.service.impl.lookuptype;


import com.team.hairdresser.constant.LookupTypeEnum;
import com.team.hairdresser.dao.LookupTypeRepository;
import com.team.hairdresser.dao.LookupValueRepository;
import com.team.hairdresser.domain.lookuptype.LookupTypeEntity;
import com.team.hairdresser.domain.lookuptype.LookupValueEntity;
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
    public LookupTypeEntity save(LookupTypeDto lookupTypeDto) {
        LookupTypeEntity lookupTypeEntity = new LookupTypeEntity();
        lookupTypeEntity.setName(lookupTypeDto.getName());
        lookupTypeEntity.setTypeEnum(LookupTypeEnum.fromLookupTypeEnumId(lookupTypeDto.getLookupTypeEnumId()));

        for (LookupValueDto valueDto : lookupTypeDto.getLookupValues()) {
            LookupValueEntity lookupValueEntity = LookupValueMapper.INSTANCE.dtoToEntity(valueDto);
            lookupValueEntity.setLookupTypeEntity(lookupTypeEntity);
            lookupTypeEntity.getLookupValueEntities().add(lookupValueEntity);
        }

        return lookupTypeRepository.saveAndFlush(lookupTypeEntity);
    }

    @Override
    public LookupTypeEntity getLookupType(Integer lookupTypeId) {
        return this.lookupTypeRepository.getOne(lookupTypeId);
    }

    @Override
    public List<LookupTypeEntity> readAll() {
        return this.lookupTypeRepository.findAll();
    }

    @Override
    public Page<LookupTypeDto> getAll(PageableSearchFilterDto filterDto) {

        SearchSpecificationBuilder<LookupTypeEntity> specificationBuilder =
                SearchSpecificationBuilder.filterAllowedKeysInstance(LookupTypeEntity.class,
                        "name");

        Specification<LookupTypeEntity> builtSpecification = specificationBuilder.build(filterDto.getCriteriaList());

        PageRequestDto pageRequest = filterDto.getPageRequest();

        PageRequest pageable = pageRequest.getSpringPageRequest();

        Page<LookupTypeEntity> lookupTypePageResult = lookupTypeRepository.findAll(builtSpecification, pageable);

        List<LookupTypeDto> lookupTypePageDtos = new ArrayList<>();

        long totalCount = lookupTypePageResult.getTotalElements();
        buildResultList(lookupTypePageResult, lookupTypePageDtos);

        return new PageImpl<>(lookupTypePageDtos, pageable, totalCount);
    }

    private void buildResultList(Page<LookupTypeEntity> lookupTypePageResult, List<LookupTypeDto> lookupTypePageDtos) {
        for (LookupTypeEntity lookupTypeEntity : lookupTypePageResult) {
            LookupTypeDto lookupTypeDto = new LookupTypeDto();

            lookupTypeDto.setLookupTypeId(lookupTypeEntity.getLookupTypeId());
            lookupTypeDto.setName(lookupTypeEntity.getName());
            lookupTypeDto.setLookupTypeEnumId(Objects.nonNull(lookupTypeEntity.getTypeEnum()) ? lookupTypeEntity.getTypeEnum().getLookupTypeEnumId() : null);

            List<LookupValueDto> lookupValueDtos = new ArrayList<>();

            for (LookupValueEntity lookupValueEntity : lookupTypeEntity.getLookupValueEntities()) {

                LookupValueDto lookupValueDto = new LookupValueDto();
                lookupValueDto.setLookupValueId(lookupValueEntity.getLookupValueId());
                lookupValueDto.setLookupTypeId(Objects.nonNull(lookupValueEntity.getLookupTypeEntity()) ? lookupValueEntity.getLookupTypeEntity().getLookupTypeId() : null);
                lookupValueDto.setValue(lookupValueEntity.getValue());
                lookupValueDto.setActive(lookupValueEntity.getActive());

                lookupValueDtos.add(lookupValueDto);

            }

            lookupTypePageDtos.add(lookupTypeDto);
        }
    }

    @Override
    public LookupTypeEntity update(Integer lookupTypeId, LookupTypeDto lookupTypeDto) {
        LookupTypeEntity lookupTypeEntity = this.lookupTypeRepository.getOne(lookupTypeId);
        lookupTypeEntity.setName(lookupTypeDto.getName());
        return this.lookupTypeRepository.saveAndFlush(lookupTypeEntity);
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


