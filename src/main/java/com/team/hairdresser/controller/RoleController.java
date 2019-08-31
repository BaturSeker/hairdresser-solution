package com.team.hairdresser.controller;


import com.team.hairdresser.constant.SuccessMessages;
import com.team.hairdresser.domain.RoleEntity;
import com.team.hairdresser.dto.SuccessResponseDto;
import com.team.hairdresser.dto.role.RoleDto;
import com.team.hairdresser.dto.role.RoleRequestDto;
import com.team.hairdresser.dto.user.UserRoleRequestDto;
import com.team.hairdresser.service.api.authority.AuthorityService;
import com.team.hairdresser.service.api.role.RoleRules;
import com.team.hairdresser.service.impl.role.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/rest/roles/")
public class RoleController {
    private RoleRules roleRules;
    private AuthorityService authorityService;

    @GetMapping(value = "{roleId}")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity getRole(@PathVariable Long roleId) {
        RoleEntity role = roleRules.read(roleId);
        RoleDto roleDto = RoleMapper.INSTANCE.entityToDto(role);
        roleDto.setRoleAuthorities(authorityService.findAuthoritiesByRoleId(roleId));
        return new ResponseEntity<>(roleDto, HttpStatus.OK);
    }

    @GetMapping(value = "getAll")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity getAll() {
        List<RoleDto> roleDtos = RoleMapper.INSTANCE.entityListToDtoList(roleRules.readAll());
        return new ResponseEntity<>(roleDtos, HttpStatus.OK);
    }

    @DeleteMapping(value = "{roleId}")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity deleteRole(@PathVariable Long roleId) {
        roleRules.delete(roleId);
        return new ResponseEntity<>(new SuccessResponseDto(SuccessMessages.ROLE_DELETE_TITLE, SuccessMessages.ROLE_DELETE_MESSAGE), HttpStatus.OK);
    }

    @PutMapping(value = "{roleId}")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity updateRole(@PathVariable Long roleId, @Valid @RequestBody RoleRequestDto roleRequestDto) {
        roleRules.update(roleId, roleRequestDto);
        return new ResponseEntity<>(new SuccessResponseDto(SuccessMessages.ROLE_UPDATE_TITLE, SuccessMessages.ROLE_UPDATE_MESSAGE), HttpStatus.OK);
    }

    @PostMapping(value = "save")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity saveRole(@Valid @RequestBody RoleRequestDto roleRequestDto) {
        roleRules.save(roleRequestDto);
        return new ResponseEntity<>(new SuccessResponseDto(SuccessMessages.ROLE_CREATE_TITLE, SuccessMessages.ROLE_CREATE_MESSAGE), HttpStatus.OK);
    }

    @PostMapping(value = "assignUserRoles")
    @PreAuthorize("@CheckPermission.hasPermission(authentication)")
    public ResponseEntity assignUserRoles(@Valid @RequestBody UserRoleRequestDto userRoleRequestDto) {
        roleRules.assignUserRoles(userRoleRequestDto);
        return new ResponseEntity<>(new SuccessResponseDto(SuccessMessages.ROLE_ASSIGN_TITLE, SuccessMessages.ROLE_ASSIGN_MESSAGE), HttpStatus.OK);
    }

    @GetMapping("getComboRoles")
    public ResponseEntity getComboRoles() {
        List roleList = roleRules.getComboRoles();
        return new ResponseEntity<>(roleList, HttpStatus.OK);
    }

    @Autowired
    public void setRoleRules(RoleRules roleRules) {
        this.roleRules = roleRules;
    }

    @Autowired
    public void setAuthorityService(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }
}

