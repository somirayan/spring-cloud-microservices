package com.appsdeveloperblog.app.ws.mobileappws.userservice.impl;

import com.appsdeveloperblog.app.ws.mobileappws.shared.Utils;
import com.appsdeveloperblog.app.ws.mobileappws.ui.model.request.UserDetailsRequestModel;
import com.appsdeveloperblog.app.ws.mobileappws.ui.model.response.UserRest;
import com.appsdeveloperblog.app.ws.mobileappws.userservice.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    Map<String, UserRest> users = new HashMap<>();
    private final Utils utils;

    @Override
    public UserRest createUser(UserDetailsRequestModel request) {
        UserRest returnValue = new UserRest();
        returnValue.setEmail(request.getEmail());
        returnValue.setFirstName(request.getFirstName());
        returnValue.setLastName(request.getLastName());
        returnValue.setUserId(utils.generateUserId());

        if (users.isEmpty()) {
            users.put(returnValue.getUserId(), returnValue);
        }

        return returnValue;

    }
}
