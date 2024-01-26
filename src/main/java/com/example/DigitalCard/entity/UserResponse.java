package com.example.DigitalCard.entity;

import lombok.Data;

@Data
public class UserResponse {
    public UserResponse(String name, String lastname, String email) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
    }

    private String name ;
    private String lastname ;
    private String email ;
}
