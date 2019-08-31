package com.team.hairdresser.config.security;


import com.team.hairdresser.dao.UsersRepository;
import com.team.hairdresser.domain.UserEntity;
import com.team.hairdresser.service.api.authority.AuthorityListRules;
import com.team.hairdresser.service.impl.token.UserTokenHolderServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String AUTH_HEADER = "Authorization";

    private JwtUtil jwtUtil;
    private UsersRepository usersRepository;
    private AuthorityListRules authorityListRules;
    private UserTokenHolderServiceImpl userTokenHolderService;

    @Override
    public void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        final String requestHeader = httpServletRequest.getHeader(AUTH_HEADER);

        String username = null;
        String authToken = null;

        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            authToken = requestHeader.substring(7);
            userTokenHolderService.setToken(requestHeader);
            try {
                username = jwtUtil.getUsernameFromToken(authToken);
            } catch (IllegalArgumentException e) {
                log.error("an error occured during getting username from usertoken", e);
            } catch (ExpiredJwtException e) {
                log.warn("this usertoken is expired and not valid anymore", e);
            }
        } else {
            log.warn("could not find bearer string, will ignore the header");
        }
        log.debug("checking authentication for user '{}'", username);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            log.debug("security context was null, so authorizating user");

            UserEntity user = this.usersRepository.findUsersByUsername(username);
            if (jwtUtil.validateToken(authToken, user.getUsername())) {

                authorityListRules.authorize(user);
                log.info("authorizated user '{}', setting security context", username);

            }

        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    @Autowired
    public void setAuthorityListRules(AuthorityListRules authorityListRules) {
        this.authorityListRules = authorityListRules;
    }

    @Autowired
    public void setUserTokenHolderService(UserTokenHolderServiceImpl userTokenHolderService) {
        this.userTokenHolderService = userTokenHolderService;
    }

    @Autowired
    public void setJwtUtil(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Autowired
    public void setUsersRepository(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }
}
