package com.example.hrtool.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final SystemUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

//       System.out.println("JwtAuthenticationFilter");

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        String userEmail = "";

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            System.out.println("JwtAuthenticationFilter - no Header");
            filterChain.doFilter(request, response);
            return;
        }

//        System.out.println("JwtAuthenticationFilter - with Header");

        jwt = authHeader.substring(7);

        try{
            userEmail = jwtService.extractUsername(jwt);
        } catch (Exception e) {
            if(e.getMessage().contains("JWT expired")){
                //wichtig, dass es diese Nachricht ist (401), damit der Fehler im Frontend richtig erkannt wird
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("text/plain");
                response.getWriter().write("accessToken Expired");
                return;
            }
            e.printStackTrace();
        }


        if (userEmail != null && !userEmail.equals("") && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            if(!jwtService.isAccessTokenValid(jwt, userDetails)) {
            }

            if (jwtService.isAccessTokenValid(jwt, userDetails)) {

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}

