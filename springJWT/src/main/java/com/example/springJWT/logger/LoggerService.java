package com.example.springJWT.logger;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.parser.Authorization;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Service
@Slf4j
public class LoggerService {




    void logRequest(HttpServletRequest httpServletRequest, Object body){

        log.info("REQUEST: ");
        log.info("URI" + httpServletRequest.getRequestURI());
        log.info("Method: " + httpServletRequest.getMethod());
        log.info("Header: " + httpServletRequest.getHeader("Authorization"));
        log.info("Parameters: " );
        httpServletRequest.getParameterMap().keySet().forEach(i -> log.info(httpServletRequest.getParameter(i)));
        log.info("Request Body: " + body.toString());
    }

    void logResponse(HttpServletRequest httpServletRequest,
                     HttpServletResponse httpServletResponse,
                     Object body) throws IOException {
        log.info("REQUEST:");
        log.info(String.valueOf(httpServletRequest.getRequestURI()));
        log.info(String.valueOf(httpServletRequest.getMethod()));
        httpServletRequest.getParameterMap().keySet().forEach(i -> log.info(httpServletRequest.getParameter(i)));
        log.info(String.valueOf(httpServletRequest.getHeader("Authorization")));

        log.info("RESPONSE");
        log.info("Header: " + httpServletResponse.getHeader("username"));
        log.info("body: " + body.toString());
    }

    void logError(String errorMessage){
        log.error(errorMessage);
    }
}
