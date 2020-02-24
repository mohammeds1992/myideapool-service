package com.backend.services.myideapool.exceptions;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (BadJWTTokenException e) {
            setErrorResponse(HttpStatus.FORBIDDEN, response, e.getMessage());
        } catch (RuntimeException e) {
            e.printStackTrace();
            setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, response, "Server error, please retry in some time");
        }
    }

    public void setErrorResponse(HttpStatus status, HttpServletResponse response, String message) {
        response.setStatus(status.value());
        response.setContentType("application/json");
        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setTimestamp(LocalDateTime.now());
        errors.setError(message);
        errors.setStatus(status.value());
        try {
            String json = errors.convertToJson();
            response.getWriter()
                    .write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}