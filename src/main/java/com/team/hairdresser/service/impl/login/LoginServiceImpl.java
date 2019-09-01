package com.team.hairdresser.service.impl.login;


import com.team.hairdresser.constant.Constants;
import com.team.hairdresser.constant.ExceptionMessages;
import com.team.hairdresser.constant.ValidationMessages;
import com.team.hairdresser.dao.UsersRepository;
import com.team.hairdresser.domain.UserEntity;
import com.team.hairdresser.dto.login.LoginRequestDto;
import com.team.hairdresser.service.api.login.LoginService;
import com.team.hairdresser.utils.util.CalendarHelper;
import com.team.hairdresser.utils.util.HashUtils;
import com.team.hairdresser.utils.util.ValidationHelper;
import com.team.hairdresser.utils.util.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
@Transactional(rollbackFor = Exception.class)
public class LoginServiceImpl implements LoginService {
    private static Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class);

    private UsersRepository usersRepository;

    private HttpServletRequest httpServletRequest;

    @Override
    public UserEntity login(LoginRequestDto loginRequestDto) throws Exception {
        controlLoggedIn(loginRequestDto);

        UserEntity user = usersRepository.findUsersByUsername(loginRequestDto.getUsername());
        if (user == null) {
            throw new ValidationException(ExceptionMessages.NO_SUCH_USER);
        }
        if (user.getActive() != null && !user.getActive()) {
            throw new ValidationException(ExceptionMessages.USER_NOT_ACTIVE);
        }

        if (!(user.getPassword().equalsIgnoreCase(HashUtils.sha256(loginRequestDto.getPassword())))) {
            user.setInvalidPswEntryCount(user.getInvalidPswEntryCount() + 1);
            if (user.getInvalidPswEntryCount() >= Constants.INV_PASSWORD_COUNT && user.getLoginLockDate() == null) {
                user.setLoginLockDate(CalendarHelper.getCurrentInstant());
            }
            isInvalidLoginCountReached(user);
            usersRepository.saveAndFlush(user);
            throw new ValidationException(ExceptionMessages.USER_NAME_PASSWORD_NOT_MATCH);
        }
        user.setInvalidPswEntryCount(0);
        user.setLoginLockDate(null);

        return user;
    }

    private void controlLoggedIn(LoginRequestDto loginRequestDto) {

        StringBuilder message = new StringBuilder();
        boolean isValid = true;
        if (!ValidationHelper.notEmpty(loginRequestDto.getPassword())) {
            isValid = false;
            message.append(ValidationMessages.LOGIN_PASSWORD_NOT_NULL);
            message.append(System.lineSeparator());
        }
        if (!ValidationHelper.notEmpty(loginRequestDto.getUsername())) {
            isValid = false;
            message.append(ValidationMessages.LOGIN_USER_NAME_NOT_NULL);
            message.append(System.lineSeparator());
        }
        if (!isValid) {
            throw new ValidationException(message.toString());
        }
    }

    @Override
    public UserEntity loginLDAP(LoginRequestDto loginRequestDto) throws Exception {
        controlLoginLDAP(loginRequestDto);

        UserEntity user = usersRepository.findUsersByUsername(loginRequestDto.getUsername());
        if (user == null) {
            throw new ValidationException(ExceptionMessages.NO_SUCH_USER);
        }

        if (user.getActive() != null && !user.getActive()) {
            throw new ValidationException(ExceptionMessages.USER_NOT_ACTIVE);
        }

        user.setPassword(HashUtils.sha256(loginRequestDto.getPassword()));
        user.setInvalidPswEntryCount(0);
        user.setLoginLockDate(null);

        return user;
    }

    private void controlLoginLDAP(LoginRequestDto loginRequestDto) {
        StringBuilder message = new StringBuilder();
        boolean isValid = true;
        if (!ValidationHelper.notEmpty(loginRequestDto.getPassword())) {
            isValid = false;
            message.append(ValidationMessages.LOGIN_PASSWORD_NOT_NULL);
            message.append(System.lineSeparator());
        }
        if (!ValidationHelper.notEmpty(loginRequestDto.getUsername())) {
            isValid = false;
            message.append(ValidationMessages.LOGIN_USER_NAME_NOT_NULL);
            message.append(System.lineSeparator());
        }
        if (!isValid) {
            throw new ValidationException(message.toString());
        }
    }

    /**
     * kullanıcı 5 yanlış giriş yaptığında bekletecek fonksiyon
     *
     * @param user
     * @throws Exception
     */
    private void isInvalidLoginCountReached(UserEntity user) throws Exception {
        if (user.getLoginLockDate() != null) {
            if ((System.currentTimeMillis() - user.getLoginLockDate().getEpochSecond() <= Constants.TIMEOUT_DURATION_MS)) {
                long remaining = (user.getLoginLockDate().getEpochSecond() + Constants.TIMEOUT_DURATION_MS - System.currentTimeMillis()) / 1000;
                throw new ValidationException("Yeni giriş yapmak için kalan süre:\t" + remaining / 60 + " dakika " + remaining % 60 + " saniye");
            }
        }
    }

    private String getClientIp() {
        String remoteAddr = "";
        if (httpServletRequest != null) {
            remoteAddr = httpServletRequest.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = httpServletRequest.getRemoteAddr();
            }
        }
        return remoteAddr;
    }

    @Autowired
    public void setUsersRepository(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Autowired
    public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }
}

