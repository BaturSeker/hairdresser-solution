package com.team.hairdresser.controller;


import com.team.hairdresser.constant.SuccessMessages;
import com.team.hairdresser.domain.lookuptype.LookupTypeEntity;
import com.team.hairdresser.domain.lookuptype.LookupValueEntity;
import com.team.hairdresser.dto.SuccessResponseDto;
import com.team.hairdresser.dto.lookuptype.LookupTypeDto;
import com.team.hairdresser.dto.lookuptype.LookupValueDto;
import com.team.hairdresser.service.api.lookuptype.LookupTypeService;
import com.team.hairdresser.service.api.lookuptype.LookupValueService;
import com.team.hairdresser.service.impl.lookuptype.LookupTypeMapper;
import com.team.hairdresser.service.impl.lookuptype.LookupValueMapper;
import com.team.hairdresser.utils.pageablesearch.model.PageableSearchFilterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("rest/lookupType")
public class LookupTypeController {

    private LookupTypeService lookupTypeService;
    private LookupValueService lookupValueService;

    @Autowired
    public LookupTypeController(LookupTypeService lookupTypeService, LookupValueService lookupValueService) {
        this.lookupTypeService = lookupTypeService;
        this.lookupValueService = lookupValueService;
    }

    @PostMapping()
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity saveType(@Valid @RequestBody LookupTypeDto lookupTypeDto) {
        lookupTypeService.save(lookupTypeDto);
        return new ResponseEntity<>(new SuccessResponseDto(SuccessMessages.GENERIC_TYPE_CREATE_TITLE, SuccessMessages.GENERIC_TYPE_CREATE_MESSAGE), HttpStatus.OK);
    }

    @PostMapping("value")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity saveValue(@Valid @RequestBody LookupValueDto lookupValueDto) {
        lookupValueService.save(lookupValueDto);
        return new ResponseEntity<>(new SuccessResponseDto(SuccessMessages.GENERIC_TYPE_VALUE_CREATE_TITLE, SuccessMessages.GENERIC_TYPE_VALUE_CREATE_MESSAGE), HttpStatus.OK);
    }

    @PutMapping(value = "{lookupTypeId}")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity updateType(@PathVariable Integer lookupTypeId, @Valid @RequestBody LookupTypeDto lookupTypeDto) {
        lookupTypeService.update(lookupTypeId, lookupTypeDto);
        return new ResponseEntity<>(new SuccessResponseDto(SuccessMessages.GENERIC_TYPE_UPDATE_TITLE, SuccessMessages.GENERIC_TYPE_UPDATE_MESSAGE), HttpStatus.OK);
    }

    @PutMapping(value = "value/{lookupValueId}")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity updateValue(@PathVariable Integer lookupValueId, @Valid @RequestBody String value) {
        lookupValueService.update(lookupValueId, value);
        return new ResponseEntity<>(new SuccessResponseDto(SuccessMessages.GENERIC_TYPE_VALUE_UPDATE_TITLE, SuccessMessages.GENERIC_TYPE_VALUE_UPDATE_MESSAGE), HttpStatus.OK);
    }

    @GetMapping(value = "value/{lookupValueId}")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity readValue(@PathVariable Integer lookupValueId) {
        LookupValueEntity lookupValueEntity = lookupValueService.read(lookupValueId);
        LookupValueDto lookupValueDto = LookupValueMapper.INSTANCE.entityToDto(lookupValueEntity);
        return new ResponseEntity<>(lookupValueDto, HttpStatus.OK);
    }

    @GetMapping(value = "{lookupTypeId}")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity readType(@PathVariable Integer lookupTypeId) {
        LookupTypeEntity lookupTypeEntity = lookupTypeService.read(lookupTypeId);
        LookupTypeDto lookupTypeDto = LookupTypeMapper.INSTANCE.entityToDto(lookupTypeEntity);
        return new ResponseEntity<>(lookupTypeDto, HttpStatus.OK);
    }

    @GetMapping("getAll")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity readType() {
        List<LookupTypeEntity> lookupTypeEntities = lookupTypeService.readAll();
        List<LookupTypeDto> lookupTypeDtos = LookupTypeMapper.INSTANCE.entityListToDtoList(lookupTypeEntities);
        return new ResponseEntity<>(lookupTypeDtos, HttpStatus.OK);
    }

    @PostMapping("list")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity<Page<LookupTypeDto>> getLookupTypePage(@RequestBody PageableSearchFilterDto pageRequest) {
        Page<LookupTypeDto> response = lookupTypeService.getAll(pageRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("values/{lookupTypeId}/getAll")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity readTypeValues(@PathVariable Integer lookupTypeId) {
        List<LookupValueDto> lookupValueDtos = LookupValueMapper.INSTANCE.entityListToDtoList(lookupValueService.readAllByLookupTypeId(lookupTypeId));
        return new ResponseEntity<>(lookupValueDtos, HttpStatus.OK);
    }

    @GetMapping("values/getIcons")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity getIcons() {
        List<LookupValueDto> lookupValueDtos = LookupValueMapper.INSTANCE.entityListToDtoList(lookupValueService.readAllIcon());
        return new ResponseEntity<>(lookupValueDtos, HttpStatus.OK);
    }

    @DeleteMapping("value/{lookupValueId}")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity changeActiveOfLookupValue(@PathVariable Integer lookupValueId) {
        lookupValueService.changeActive(lookupValueId);
        return new ResponseEntity<>(new SuccessResponseDto(SuccessMessages.GENERIC_TYPE_VALUE_UPDATE_TITLE, SuccessMessages.GENERIC_TYPE_VALUE_UPDATE_MESSAGE), HttpStatus.OK);
    }
}


