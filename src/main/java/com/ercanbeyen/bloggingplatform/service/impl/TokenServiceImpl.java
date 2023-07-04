package com.ercanbeyen.bloggingplatform.service.impl;

import com.ercanbeyen.bloggingplatform.constant.messages.JwtMessage;
import com.ercanbeyen.bloggingplatform.exception.DataNotFound;
import com.ercanbeyen.bloggingplatform.security.jwt.JwtService;
import com.ercanbeyen.bloggingplatform.service.TokenService;
import com.ercanbeyen.bloggingplatform.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final JwtService jwtService;
    //private final AuthorService authorService;

    @Override
    public void refreshToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        String authorizationHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (!JwtUtils.doesTokenExist(authorizationHeader)) {
            throw new DataNotFound("Refresh token is missing");
        }

        try {
            Map<String, String> tokenMap = jwtService.refreshToken(authorizationHeader);
            httpServletResponse.setHeader(JwtMessage.TOKEN_MAP_KEY_ACCESS_TOKEN, tokenMap.get(JwtMessage.TOKEN_MAP_KEY_ACCESS_TOKEN));
            httpServletResponse.setHeader(JwtMessage.TOKEN_MAP_KEY_REFRESH_TOKEN, tokenMap.get(JwtMessage.TOKEN_MAP_KEY_REFRESH_TOKEN));
        } catch (Exception exception) {
            log.error("Error while refreshing token: {}", exception.getMessage());
            httpServletResponse.setHeader(JwtMessage.HEADER_KEY_ERROR, exception.getMessage());
            httpServletResponse.sendError(HttpStatus.FORBIDDEN.value());
        } finally {
            log.info("Refresh method is terminated");
        }

    }
}
