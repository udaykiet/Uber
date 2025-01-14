package com.ups.uber.controllers;

import com.ups.uber.dtos.*;
import com.ups.uber.services.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth APIs" , description = "")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignupDto signupDto){
        return new ResponseEntity<>(authService.signup(signupDto), HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/onBoardNewDriver/{userId}")
    public ResponseEntity<DriverDto> onBoardNewDriver(@PathVariable Long userId,
                                                      @RequestBody OnBoardNewDriverDto onBoardNewDriverDto){
        return new ResponseEntity<>(authService.onBoardNewDriver(userId ,
                onBoardNewDriverDto.getVehicleId()), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto,
                                                  HttpServletRequest httpServletRequest,
                                                  HttpServletResponse httpServletResponse){

       String[] tokens =  authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
        Cookie cookie = new Cookie("token" ,  tokens[1]);
        cookie.setHttpOnly(true);
        httpServletResponse.addCookie(cookie);

       String accessToken = tokens[0];
       LoginResponseDto loginResponseDto = new LoginResponseDto(accessToken);
       return ResponseEntity.ok(loginResponseDto);
    }




    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(HttpServletRequest request){

        String refreshToken = Arrays.stream(request.getCookies())
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(cookie -> cookie.getValue())
                .orElseThrow(()-> new AuthenticationServiceException("refresh token not found in the cookie"));


        String accessToken =  authService.refreshToken(refreshToken);
        return ResponseEntity.ok(new LoginResponseDto(accessToken));
    }

}
