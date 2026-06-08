package com.mall.author;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.SignatureException;


@RestControllerAdvice
public class PermissionHandler {

	@ExceptionHandler(value = { SignatureException.class })
    public ResponseEntity authorizationException(SignatureException e){

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization失效"); //JsonResult.error(401, e.getMessage(), "Authorization失效");
    }
}
