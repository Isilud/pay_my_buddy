package com.paymybuddy.application.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class UserLoginDTO {

    private int id;
    private String jwtToken;

}
