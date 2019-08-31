package com.team.hairdresser.controller;


import com.team.hairdresser.constant.SuccessMessages;
import com.team.hairdresser.domain.Authority;
import com.team.hairdresser.dto.SuccessResponseDto;
import com.team.hairdresser.dto.authority.*;
import com.team.hairdresser.service.api.authority.AuthorityRules;
import com.team.hairdresser.service.api.authority.AuthorityService;
import com.team.hairdresser.service.impl.authority.AuthorityMapper;
import com.team.hairdresser.service.impl.authority.AuthorityResponseDtoMapper;
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
@RequestMapping("/rest/authority/")
public class AuthorityController {
    private AuthorityRules authorityRules;
    private AuthorityService authorityService;

    @GetMapping(value = "{authorityId}")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity getAuthority(@PathVariable Long authorityId) {

        Authority authority = authorityRules.read(authorityId);
        AuthorityResponse authorityResponse = new AuthorityResponse();
        authorityResponse.setAuthorityId(authorityId);
        authorityResponse.setTitle(authority.getTitle());
        authorityResponse.setIcon(authority.getIcon());
        authorityResponse.setHasIcon(authorityRules.shouldIconExist(authorityId));
        return new ResponseEntity<>(authorityResponse, HttpStatus.OK);
    }

    @PutMapping(value = "{authorityId}")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity updateAuthority(@PathVariable Long authorityId, @Valid @RequestBody AuthorityRequestDto authorityRequestDto) {

        authorityRules.update(authorityId, authorityRequestDto);
        return new ResponseEntity<>(new SuccessResponseDto(SuccessMessages.AUTHORITY_UPDATE_TITLE, SuccessMessages.AUTHORITY_UPDATE_MESSAGE), HttpStatus.OK);
    }

    @PostMapping(value = "save")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity saveAuthority(@Valid @RequestBody AuthorityRequestDto authorityRequestDto) {
        authorityRules.save(authorityRequestDto);
        return new ResponseEntity<>(new SuccessResponseDto(SuccessMessages.AUTHORITY_CREATE_TITLE, SuccessMessages.AUTHORITY_CREATE_MESSAGE), HttpStatus.OK);
    }

    @PostMapping(value = "assignRoleAuthorities")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity assignRoleAuthorities(@Valid @RequestBody RoleAuthorityRequestDto roleAuthorityRequestDto) {
        authorityRules.assignRoleAuthorities(roleAuthorityRequestDto);
        return new ResponseEntity<>(new SuccessResponseDto(SuccessMessages.AUTHORITY_ASSIGN_TITLE, SuccessMessages.AUTHORITY_ASSIGN_MESSAGE), HttpStatus.OK);
    }

    @GetMapping(value = "getAllAuthorities")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity getAllAuthorities() {
        List<AuthorityResponse> authorityResponses = AuthorityMapper.INSTANCE.entityListToDtoList(authorityRules.readAll());
        return new ResponseEntity<>(authorityResponses, HttpStatus.OK);
    }

    @GetMapping(value = "getAll")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity getAll() {
        List<AuthorityResponseDto> authorityResponseDtos = AuthorityResponseDtoMapper.INSTANCE.entityListToDtoList(authorityRules.readAllAuthority());
        return new ResponseEntity<>(authorityResponseDtos, HttpStatus.OK);
    }

    @PostMapping("list")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity<Page<AuthorityPageDto>> getAuthorityPage(@RequestBody PageableSearchFilterDto pageRequest) {
        Page<AuthorityPageDto> response = authorityService.getAll(pageRequest);
        return ResponseEntity.ok(response);
    }

    @Autowired
    public void setAuthorityRules(AuthorityRules authorityRules) {
        this.authorityRules = authorityRules;
    }

    @Autowired
    public void setAuthorityService(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }
}


