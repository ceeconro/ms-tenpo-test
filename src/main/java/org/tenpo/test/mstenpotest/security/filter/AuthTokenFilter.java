package org.tenpo.test.mstenpotest.security.filter;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.tenpo.test.mstenpotest.exceptions.NotLoggedInException;
import org.tenpo.test.mstenpotest.security.user.User;
import org.tenpo.test.mstenpotest.security.user.UserService;
import org.tenpo.test.mstenpotest.security.util.JwtTokenUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {

    public static final String _BEARER = "Bearer ";

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authToken = null;

        String headerAuth = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(_BEARER)) {
            authToken = headerAuth.substring(7);
        }
        String username = null;

        try {
            if (authToken != null) {
                username = jwtUtil.parseToken(authToken).getSubject();
            }
        } catch (IllegalArgumentException e) {
            logger.error("an error occured during getting username from token", e);
        } catch (ExpiredJwtException e) {
            logger.warn("the token is expired and not valid anymore", e);
        }


        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            logger.info("checking authentication for user " + username);

            User userDetails = this.userService.loadUserByUsername(username);
            log.info("User logged in {}", userDetails);
            if (userDetails.isLogged()) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        ((User) userDetails).getAuthorities()
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                log.info("authenticated user {}, setting security context", username);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                throw new NotLoggedInException("User is not logged");
            }
        }

        filterChain.doFilter(request, response);
    }
}
