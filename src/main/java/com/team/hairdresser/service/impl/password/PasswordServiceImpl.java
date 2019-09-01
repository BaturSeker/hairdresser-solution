package com.team.hairdresser.service.impl.password;


import com.team.hairdresser.constant.ExceptionMessages;
import com.team.hairdresser.domain.UserEntity;
import com.team.hairdresser.dto.password.SetNewPasswordRequestDto;
import com.team.hairdresser.dto.user.UserRequestDto;
import com.team.hairdresser.service.api.password.PasswordService;
import com.team.hairdresser.service.api.user.UserService;
import com.team.hairdresser.utils.util.CalendarHelper;
import com.team.hairdresser.utils.util.HashUtils;
import com.team.hairdresser.utils.util.PasswordGenerator;
import com.team.hairdresser.utils.util.ValidationHelper;
import com.team.hairdresser.utils.util.exception.PasswordNotGenerateException;
import com.team.hairdresser.utils.util.exception.ValidationException;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
@Transactional(rollbackFor = Exception.class)
public class PasswordServiceImpl implements PasswordService, ResourceLoaderAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordServiceImpl.class);

    private UserService userService;
    private ResourceLoader resourceLoader;

    @Autowired
    public PasswordServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void passwordValidation(UserRequestDto userRequest) throws ValidationException {
        boolean isValidPass = true;
        StringBuilder messages = new StringBuilder();

        basePasswordRules(userRequest.getPassword());

        checkSlangWord(userRequest.getPassword());

        if (userRequest.getPassword().contains(userRequest.getName()) || userRequest.getPassword().contains(userRequest.getSurname())
                || userRequest.getPassword().contains(userRequest.getUserName())) {
            messages.append(ExceptionMessages.CONSTRAINT_PERSONAL_INFO);
            messages.append(System.lineSeparator());
            isValidPass = false;
        }
        if (!isValidPass) {
            throw new ValidationException(messages.toString());
        }

    }

    @Override
    public boolean isPasswordValid(String password) {
        basePasswordRules(password);
        return true;
    }

    @Override
    public boolean isUsernamePasswordValid(String username, String password) {
        StringBuilder messages = new StringBuilder();
        boolean isValidPass = true;
        basePasswordRules(password);
        if (password.contains(username)) {
            isValidPass = false;
            messages.append(ExceptionMessages.CONSTRAINT_PERSONAL_INFO);
        }
        if (!isValidPass) {
            throw new ValidationException(messages.toString());
        }
        return true;
    }

    @Override
    public void setIsTemproraryPassword(UserEntity user) throws NoSuchAlgorithmException {
        PasswordGenerator passwordGenerator = new PasswordGenerator();
        String password = passwordGenerator.generate(10);

        int generateCount = 0;
        while ((!this.isUsernamePasswordValid(user.getUsername(), password)) && generateCount < 5) {
            generateCount++;
            password = passwordGenerator.generate(10);
        }
        if (generateCount > 5 && (!this.isUsernamePasswordValid(user.getUsername(), password))) {
            throw new PasswordNotGenerateException(ExceptionMessages.PASSWORD_NOT_GENERATE);
        }
        String hashedPassword = HashUtils.sha256(password);
        user.setPassword(hashedPassword);
        user.setPasswordCreatedDate(CalendarHelper.getCurrentInstant());
        user.setInvalidPswEntryCount(0);
        user.setPasswordTemporary(true);
        userService.save(user);
    }

    @Override
    public void setIsTemproraryPassword(List<UserEntity> userEntityList) throws NoSuchAlgorithmException {
        for (UserEntity userEntity : userEntityList) {
            this.setIsTemproraryPassword(userEntity);
        }
    }

    @Override
    public void setNewPassword(SetNewPasswordRequestDto setNewPasswordRequest) throws NoSuchAlgorithmException {
        StringBuilder messages = new StringBuilder();
        boolean isValidPass = true;
        String hashedOldPass, hashedNewPass;
        UserEntity user = userService.getUser(setNewPasswordRequest.getUserId());
        hashedOldPass = HashUtils.sha256(setNewPasswordRequest.getOldPassword());
        hashedNewPass = HashUtils.sha256(setNewPasswordRequest.getNewPassword());
        if (!setNewPasswordRequest.getNewPassword().equals(setNewPasswordRequest.getNewPasswordRe())) {
            isValidPass = false;
            messages.append(ExceptionMessages.PASSWORDS_NOT_MATCH);
            messages.append(System.lineSeparator());
        }
        if (!user.getPassword().equalsIgnoreCase(hashedOldPass)) {
            isValidPass = false;
            messages.append(ExceptionMessages.OLD_PASSWORDS_NOT_MATCH);
            messages.append(System.lineSeparator());
        }
        if (user.getPassword().equalsIgnoreCase(hashedNewPass)) {
            isValidPass = false;
            messages.append(ExceptionMessages.OLD_AND_NEW_PASSWORDS_CAN_NOT_BE_SAME);
            messages.append(System.lineSeparator());
        }
        if (!isValidPass) {
            throw new ValidationException(messages.toString());
        }

        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setName(user.getFirstname());
        userRequestDto.setSurname(user.getSurname());
        userRequestDto.setUserName(user.getUsername());
        userRequestDto.setPassword(setNewPasswordRequest.getNewPassword());
        passwordValidation(userRequestDto);

        String hashedPassword = HashUtils.sha256(setNewPasswordRequest.getNewPassword());
        user.setPassword(hashedPassword);
        user.setPasswordCreatedDate(CalendarHelper.getCurrentInstant());
        user.setPasswordTemporary(false);
        userService.save(user);
    }

    @Override
    public void resetPassword(Long userId) throws NoSuchAlgorithmException {
        StringBuilder messages = new StringBuilder();
        boolean isValidPass = true;
        if (!ValidationHelper.notNull(this.userService.getUser(userId))) {
            isValidPass = false;
            messages.append(ExceptionMessages.RESET_PASSWORD_USER_NOT_NULL);
            messages.append(System.lineSeparator());
        }
        if (!isValidPass) {
            throw new ValidationException(messages.toString());
        }
        UserEntity user = this.userService.getUser(userId);
        setIsTemproraryPassword(user);
    }

    @Override
    public void basePasswordRules(String password) {
        boolean isValidPass = true;
        StringBuilder messages = new StringBuilder();
        if (!ValidationHelper.notNull(password)) {
            messages.append(ExceptionMessages.CONSTRAINT_EMPTY);
            messages.append(System.lineSeparator());
            throw new ValidationException(messages.toString());
        } else {
            if (password.length() < 8 || password.length() > 14) {
                messages.append(ExceptionMessages.CONSTRAINT_SIZE);
                messages.append(System.lineSeparator());
                isValidPass = false;
            }
            if (!password.matches(".*[a-z].*")) {
                messages.append(ExceptionMessages.CONSTRAINT_LOWERCASE);
                messages.append(System.lineSeparator());
                isValidPass = false;
            }
            if (!password.matches(".*[A-Z].*")) {
                messages.append(ExceptionMessages.CONSTRAINT_UPPERCASE);
                messages.append(System.lineSeparator());
                isValidPass = false;
            }
            if (!password.matches(".*\\d.*")) {
                messages.append(ExceptionMessages.CONSTRAINT_NUMBER);
                messages.append(System.lineSeparator());
                isValidPass = false;
            }
            if (password.contains(" ")) {
                messages.append(ExceptionMessages.CONSTRAINT_WHITESPACE);
                messages.append(System.lineSeparator());
                isValidPass = false;
            }
        }

        //TODO: Rule lar eklenecek.
//        PasswordValidator validator = new PasswordValidator(Arrays.asList(
//                new NumericalSequenceRule(4, false),
//                new AlphabeticalSequenceRule(4, false),
//                new QwertySequenceRule(4, false)));

        PasswordValidator validator = new PasswordValidator();

        RuleResult result = validator.validate(new PasswordData(password));
        if (!result.isValid()) {
            isValidPass = false;
            messages.append(ExceptionMessages.CONSTRAINT_SEQUENCE);
            messages.append(System.lineSeparator());
        }
        if (!isValidPass) {
            throw new ValidationException(messages.toString());
        }
    }

    @Override
    public void checkSlangWord(String password) {
        boolean isValidPass = true;
        StringBuilder messages = new StringBuilder();
        Resource resource = resourceLoader.getResource("classpath:argo.txt");
        try (Scanner scanner = new Scanner(resource.getInputStream());) {
            ArrayList<String> list = new ArrayList<>();
            while (scanner.hasNext()) {
                list.add(scanner.next());
            }

            for (String word : list) {
                if (password.toLowerCase().contains(word.toLowerCase())) {
                    isValidPass = false;
                }
            }

        } catch (IOException e) {
            LOGGER.error("error occured", e);
        }

        if (!isValidPass) {
            messages.append(ExceptionMessages.CONSTRAINT_DICTIONARY_WORD);
            throw new ValidationException(messages.toString());
        }
    }
}
