package se.rydberg.handla.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Läser "Authorization: Bearer hk_..." och loggar in användaren som äger nyckeln.
 * Används bara i API-kedjan (/api/**), registreras inte som ett globalt servlet-filter.
 */
public class ApiTokenAuthenticationFilter extends OncePerRequestFilter {
    private static final String BEARER = "Bearer ";

    private final ApiTokenService apiTokenService;

    public ApiTokenAuthenticationFilter(ApiTokenService apiTokenService) {
        this.apiTokenService = apiTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith(BEARER)) {
            apiTokenService.authenticate(header.substring(BEARER.length()).trim()).ifPresent(user -> {
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(UsernamePasswordAuthenticationToken.authenticated(user, null, user.getAuthorities()));
                SecurityContextHolder.setContext(context);
            });
        }
        filterChain.doFilter(request, response);
    }
}
