package com.first.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

/**
 * Caller metadata recorded alongside issued refresh tokens.
 * Behind a reverse proxy set {@code server.forward-headers-strategy=native} so getRemoteAddr()
 * reflects the real client.
 */
public record ClientInfo(String ipAddress, String userAgent) {

    public static ClientInfo from(HttpServletRequest request) {
        return new ClientInfo(request.getRemoteAddr(), request.getHeader(HttpHeaders.USER_AGENT));
    }
}
