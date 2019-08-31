package com.team.hairdresser.utils.util;


import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

public class ClearSession {
    public HttpStatus clear(ServletRequestAttributes attr) {

        HttpStatus responseCode = HttpStatus.PROCESSING;
        try {
            if (attr != null) {
                HttpSession httpSession = attr.getRequest().getSession();
                if (httpSession != null) {
                    httpSession.invalidate();
                    responseCode = HttpStatus.OK;
                }
            }
        } catch (Exception e) {
            responseCode = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return responseCode;
    }
}