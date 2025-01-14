package com.ups.uber.services.impl;

import com.ups.uber.dtos.DriverDto;
import com.ups.uber.dtos.LoginResponseDto;
import com.ups.uber.dtos.SignupDto;
import com.ups.uber.dtos.UserDto;
import com.ups.uber.entities.Driver;
import com.ups.uber.entities.User;
import com.ups.uber.entities.enums.Role;
import com.ups.uber.exceptions.ResourceNotFoundException;
import com.ups.uber.exceptions.RuntimeConflictException;
import com.ups.uber.repositories.UserRepository;
import com.ups.uber.security.JWTService;
import com.ups.uber.services.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RiderService riderService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private DriverService driverService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;



    @Override
    public String[] login(String email, String password) {


        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        User user = (User) authenticate.getPrincipal();
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return new String[]{accessToken , refreshToken};
    }

    @Override
    @Transactional
    public UserDto signup(SignupDto signupDto) {
        User user = userRepository.findByEmail(signupDto.getEmail()).orElse(null);
        if(user != null){
            throw new RuntimeConflictException("user with email " + signupDto.getEmail() + " is already exist");
        }


        User mappedUser = modelMapper.map(signupDto , User.class);
        mappedUser.setRoles(Set.of(Role.RIDER));
        //encode the password of the user
        mappedUser.setPassword(passwordEncoder.encode(mappedUser.getPassword()));
        User savedUser = userRepository.save(mappedUser);

//        create user related entities

         riderService.createNewRider(savedUser);

         walletService.createNewWallet(savedUser);

        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public DriverDto onBoardNewDriver(Long userId , String vehicleId ) {

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("user with id: " + userId + " is not present"));
        if(user.getRoles().contains(Role.DRIVER))
            throw  new RuntimeException("user with id: "+ userId + " is already s driver");

        user.getRoles().add(Role.DRIVER);
        User savedUser = userRepository.save(user);

        Driver createDriver = Driver.builder()
                .available(true)
                .rating(0.0)
                .vehicleId(vehicleId)
                .user(savedUser)
                .build();

       Driver savedDriver =  driverService.createNewDriver(createDriver);
       return modelMapper.map(savedDriver , DriverDto.class);
    }

    @Override
    public String refreshToken(String refreshToken) {
        Long userId = jwtService.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("user not found with id: "+userId));
        return jwtService.generateAccessToken(user);

    }
}
