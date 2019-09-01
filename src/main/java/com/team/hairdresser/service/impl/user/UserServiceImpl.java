package com.team.hairdresser.service.impl.user;


import com.team.hairdresser.constant.AuthorityCodes;
import com.team.hairdresser.constant.ValidationMessages;
import com.team.hairdresser.dao.UserRoleRepository;
import com.team.hairdresser.dao.UsersRepository;
import com.team.hairdresser.domain.RoleEntity;
import com.team.hairdresser.domain.UserEntity;
import com.team.hairdresser.domain.UserRoleEntity;
import com.team.hairdresser.dto.password.ResetPasswordDto;
import com.team.hairdresser.dto.user.UserInfoResponseDto;
import com.team.hairdresser.service.api.user.UserService;
import com.team.hairdresser.utils.pageablesearch.model.PageableSearchFilterDto;
import com.team.hairdresser.utils.util.ComboResponseBuilder;
import com.team.hairdresser.utils.util.HashUtils;
import com.team.hairdresser.utils.util.PasswordGenerator;
import com.team.hairdresser.utils.util.ValidationHelper;
import com.team.hairdresser.utils.util.exception.ValidationException;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private UsersRepository usersRepository;
    private UserRoleRepository userRoleRepository;
//    private final MailService mailService;

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_USER_MANAGEMENT + "')")
    public void saveAll(List<UserEntity> userList) {
        boolean isValid = true;
        StringBuilder messages = new StringBuilder();
        if (!ValidationHelper.isValid(userList)) {
            isValid = false;
            messages.append(ValidationMessages.LIST_CAN_NOT_BE_NULL);
            messages.append(System.lineSeparator());
        }
        if (!isValid) {
            throw new ValidationException(messages.toString());
        }

        for (UserEntity user : userList) {
            this.save(user);
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_USER_MANAGEMENT + "')")
    public void save(UserEntity user) {
        controlSave(user);

        usersRepository.save(user);
    }

    private void controlSave(UserEntity user) {
        boolean isValid = true;
        StringBuilder messages = new StringBuilder();
        if (Objects.equals(user, null)) {
            isValid = false;
            messages.append(ValidationMessages.USER_CAN_NOT_BE_NULL);
            messages.append(System.lineSeparator());
        }
        if (!isValid) {
            throw new ValidationException(messages.toString());
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_USER_MANAGEMENT + "')")
    public UserEntity getUser(Long userId) {
        controlGetUser(userId);
        return usersRepository.getOne(userId);
    }

    private void controlGetUser(Long userId) {
        boolean isValid = true;
        StringBuilder message = new StringBuilder();
        if (!ValidationHelper.isValid(userId)) {
            isValid = false;
            message.append(ValidationMessages.USER_ID_CAN_NOT_BE_NULL);
            message.append(System.lineSeparator());
        }
        if (!isValid) {
            throw new ValidationException(message.toString());
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_USER_MANAGEMENT + "')")
    public List<UserEntity> getAllUsers() {
        return IteratorUtils.toList(usersRepository.findAll().iterator());
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_USER_MANAGEMENT + "')")
    public List getComboUsers() {
        List<Object[]> resultList = this.usersRepository.findUsersAsComboValues();
        return ComboResponseBuilder.buildComboResponseList(resultList);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_USER_MANAGEMENT + "')")
    public Page<UserInfoResponseDto> getUserInfoPage(PageRequest pageRequest) {
        Page<UserEntity> usersPageResult = usersRepository.findUsersBy(pageRequest);
        List<UserInfoResponseDto> userInfoResponseDtoList = new ArrayList<>();
        //TODO: deleted userlar sayilmamalidir
        buildResultList(usersPageResult, userInfoResponseDtoList);

        long totalUserCount = usersPageResult.getTotalElements();
        return new PageImpl(userInfoResponseDtoList, pageRequest, totalUserCount);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_USER_MANAGEMENT + "')")
    public Page<UserInfoResponseDto> getUsersFiltered(PageableSearchFilterDto filterDto) {
        //TODO: yazılacak
        return null;
    }

    private void buildResultList(Page<UserEntity> usersPageResult, List<UserInfoResponseDto> userInfoResponseDtoList) {
        for (UserEntity u : usersPageResult) {
            UserInfoResponseDto userInfoResponseDto = new UserInfoResponseDto();
            populateFromUser(u, userInfoResponseDto);


            List<UserRoleEntity> userRoleEntities = userRoleRepository.findByUser(u);
            List<Long> roleIdList = new ArrayList<>();
            for (UserRoleEntity userRoleEntity : userRoleEntities) {
                roleIdList.add(userRoleEntity.getRole().getId());
            }
            userInfoResponseDto.setRoleIds(roleIdList);
            userInfoResponseDtoList.add(userInfoResponseDto);
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_USER_MANAGEMENT + "')")
    public List<UserEntity> findByRole(RoleEntity role) {
        List<UserRoleEntity> userRoleEntities = userRoleRepository.findAllByRole(role);
        Set<Long> userIds = userRoleEntities.stream().map(t -> t.getUser().getId()).collect(Collectors.toSet());
        List<UserEntity> userListByRole = new ArrayList<>();
        for (Long userId : userIds) {
            UserEntity user = usersRepository.getOne(userId);
            userListByRole.add(user);
        }

        return userListByRole;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('" + AuthorityCodes.VIEW_USER_MANAGEMENT + "')")
    public String resetPassword(ResetPasswordDto resetPasswordDto) throws NoSuchAlgorithmException {
        PasswordGenerator passwordGenerator = new PasswordGenerator();
        UserEntity user = this.usersRepository.findByEmail(resetPasswordDto.getEmail());
        String tempPass = passwordGenerator.generate(10);
        if (Objects.nonNull(user.getId())) {
            user.setPassword(HashUtils.sha256(tempPass));
            user.setPasswordTemporary(true);
            this.usersRepository.save(user);

            String content = "Geçici şifreniz: " + tempPass;
            String subject = "Şifre sıfırlama";
            sendMail(subject, content, user.getEmail());
            return "Geçici şifreniz mailinize gönderildi.";
        }

        user = this.usersRepository.findByMobilePhone(resetPasswordDto.getPhone());
        if (Objects.nonNull(user.getId())) {
            user.setPassword(HashUtils.sha256(tempPass));
            user.setPasswordTemporary(true);
            this.usersRepository.save(user);

            return "Sisteme giriş yapabileceğiniz geçici şifreniz telefonunuza gönderild.";
        }

        return "Verdiğiniz bilgilerle sisteme kayıtlı bir kullanıcı bulunamadı.";
    }

    @Override
    public String getUserInfo(UserEntity user) {
        StringBuilder userName = new StringBuilder();
        userName.append(" ");
        userName.append(user.getFirstname());
        userName.append(" ");
        userName.append(user.getSurname());
        return userName.toString();
    }

    @Override
    public UserEntity getCurrentUser() {
        return (UserEntity) (SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    private void sendMail(String subject, String content, String email) {
        // TODO Mail düzeltilecek
        String[] recipients = {"asd@tambilisim.com.tr"};
//        Mail mail = MailBuilder.newMail().setContent(content).setHtml(true).setMultipart(false).setRecipent(recipients).setSubject(subject).build();
//        mailService.sendEmail(mail);
    }

    private void populateFromUser(UserEntity u, UserInfoResponseDto userInfoResponseDto) {
        userInfoResponseDto.setName(u.getFirstname());
        userInfoResponseDto.setSurname(u.getSurname());
        userInfoResponseDto.setEmail(u.getEmail());
        userInfoResponseDto.setMobile(u.getMobilePhone());
        userInfoResponseDto.setTelephone(u.getTelephone());
        userInfoResponseDto.setActive(u.getActive());
    }

    @Autowired
    public void setUsersRepository(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Autowired
    public void setUserRoleRepository(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }
}

