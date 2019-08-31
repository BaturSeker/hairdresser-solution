package com.team.hairdresser.service.api.user;

import com.team.hairdresser.domain.RoleEntity;
import com.team.hairdresser.domain.UserEntity;
import com.team.hairdresser.dto.password.ResetPasswordDto;
import com.team.hairdresser.dto.user.UserInfoResponseDto;
import com.team.hairdresser.utils.pageablesearch.model.PageableSearchFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface UserRules {

    void saveAll(List<UserEntity> userList);

    void save(UserEntity user);

    List<UserEntity> read();

    UserEntity getUser(Long userId);

    Page<UserInfoResponseDto> getUsersFiltered(PageableSearchFilterDto filterDto);

    List getComboUsers();

    Page<UserInfoResponseDto> getUserInfoPage(PageRequest pageRequest);

//    Page<UserInfoResponseDto> getUsersFiltered(PageableSearchFilterDto filterDto);

    List<UserEntity> findByRole(RoleEntity role);

    String resetPassword(ResetPasswordDto resetPasswordDto) throws NoSuchAlgorithmException;

    List<UserEntity> getAllUser(Integer locationId);

}
