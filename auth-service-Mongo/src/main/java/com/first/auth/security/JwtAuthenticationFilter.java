package com.first.auth.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Authenticates requests carrying {@code Authorization: Bearer <access token>}.
 * <p>
 * The user is re-loaded on every request (see {@link AccessTokenVerifier}) so disabled / locked
 * accounts and revoked tokens are rejected immediately rather than at token expiry.
 * On failure the request continues unauthenticated; protected endpoints then answer 401 through
 * {@link RestAuthenticationEntryPoint}, which reports the reason stored in {@link #ERROR_ATTRIBUTE}.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String ERROR_ATTRIBUTE = "auth.jwt.error";
    private static final String BEARER = "Bearer ";

    private final AccessTokenVerifier verifier;

    public JwtAuthenticationFilter(AccessTokenVerifier verifier) {
        this.verifier = verifier;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith(BEARER)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            AccessTokenVerifier.VerifiedToken verified = verifier.verify(header.substring(BEARER.length()).trim());
            UserPrincipal principal = UserPrincipal.from(verified.user());
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(principal, null, principal.authorities());
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (ExpiredJwtException e) {
            SecurityContextHolder.clearContext();
            request.setAttribute(ERROR_ATTRIBUTE, "Access token has expired");
        } catch (JwtException | IllegalArgumentException e) {
            SecurityContextHolder.clearContext();
            request.setAttribute(ERROR_ATTRIBUTE, "Invalid access token: " + e.getMessage());
        }

        chain.doFilter(request, response);
    }
}
