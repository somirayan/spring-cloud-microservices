package com.appsdeveloperblog.photoapp.api.users.shared;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDto implements Serializable {

    private String userId;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String encryptedPassword;

}
