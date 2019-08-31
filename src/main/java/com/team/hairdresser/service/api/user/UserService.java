package com.team.hairdresser.service.api.user;

import com.team.hairdresser.domain.RoleEntity;
import com.team.hairdresser.domain.Users;
import com.team.hairdresser.dto.password.ResetPasswordDto;
import com.team.hairdresser.dto.user.UserInfoResponseDto;
import com.team.hairdresser.utils.pageablesearch.model.PageableSearchFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface UserService {
    void save(Users user);

    Users getUser(Long userId);

    List<Users> getAllUsers();

    List getComboUsers();

    Page<UserInfoResponseDto> getUserInfoPage(PageRequest pageRequest);

    Page<UserInfoResponseDto> getUsersFiltered(PageableSearchFilterDto filterDto);

    List<Users> findByRole(RoleEntity role);

    //TODO:
//    Integer saveUserImage(UserImageDto userImageDto);

    String resetPassword(ResetPasswordDto resetPasswordDto) throws NoSuchAlgorithmException;

    List<Users> getAllUser(Integer locationId);

    String getUserInfo(Users user);

    Users getCurrentUser();
}
