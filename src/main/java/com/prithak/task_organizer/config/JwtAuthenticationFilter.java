package com.prithak.task_organizer.config;

import com.prithak.task_organizer.util.JwtUtil;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtUtil jwtUtil;
  private final UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
    try {
      String authenticationHeader = request.getHeader("Authorization");
      String jwtToken;
      String username;
      if (authenticationHeader == null || !authenticationHeader.startsWith("Bearer ")) {
        filterChain.doFilter(request, response);
        return;
      }
      jwtToken = authenticationHeader.substring(7);
      username = jwtUtil.extractUsername(jwtToken);
      if (null != username && null == SecurityContextHolder.getContext().getAuthentication()) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
        Collection<? extends GrantedAuthority> authorities = jwtUtil.getAuthoritiesFromJwt(jwtToken);
        if (jwtUtil.isTokenValid(jwtToken, userDetails)) {
          UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
          authenticationToken.setDetails(
                  new WebAuthenticationDetailsSource().buildDetails(request)
          );
          SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
      }
    } catch (SignatureException e) {
      log.error("Exception Type : {}, Message : {}", e.getClass().getName(), e.getMessage());
    }
    filterChain.doFilter(request, response);
  }
}
