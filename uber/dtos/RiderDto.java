package com.ups.uber.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RiderDto {

    private Long id;
    private UserDto user;
    private Double rating;
}
