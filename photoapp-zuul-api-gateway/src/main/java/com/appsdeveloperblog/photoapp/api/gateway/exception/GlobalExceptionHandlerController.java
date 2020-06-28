package com.appsdeveloperblog.photoapp.api.gateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandlerController {

    @ResponseBody
    @ExceptionHandler({InsufficientAuthenticationException.class})
    public ResponseEntity<?> handleInsufficientAuthenticationException(HttpServletResponse res, InsufficientAuthenticationException e) throws IOException {
        //LOG.error("ERROR", e);
        System.out.println("Error: " + e.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON).body(e.getMessage());
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<?> handleAccessDeniedException(HttpServletResponse res, AccessDeniedException e) throws IOException {
        //LOG.error("ERROR", e);
        System.out.println("Error: " + e.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).contentType(MediaType.APPLICATION_JSON).body(e.getMessage());
    }

}
