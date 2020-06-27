package com.appsdeveloperblog.photoapp.api.users.data;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Data
@Entity
public class UserEntity implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false, length = 120, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String encryptedPassword;

    private int active=1;

    private boolean isLocked=false;

    private boolean isExpired=false;

    private boolean isEnabled=false;

    @OneToMany
    private Set<Role> role;
}
