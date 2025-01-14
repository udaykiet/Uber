package com.ups.uber.services;

import com.ups.uber.dtos.DriverDto;
import com.ups.uber.dtos.LoginResponseDto;
import com.ups.uber.dtos.SignupDto;
import com.ups.uber.dtos.UserDto;

public interface AuthService {

    String[] login(String email , String password);

    UserDto signup(SignupDto signupDto);

    DriverDto onBoardNewDriver(Long userId , String vehicleId);

    String refreshToken(String refreshToken);

}
