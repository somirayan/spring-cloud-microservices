package com.appsdeveloperblog.photoapp.api.users.data;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Role {

    @Id
    @GeneratedValue
    private long id;
    private String role;
}
