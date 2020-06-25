package com.appsdeveloperblog.app.ws.mobileappws.ui.controller;

import com.appsdeveloperblog.app.ws.mobileappws.exceptions.UserServiceException;
import com.appsdeveloperblog.app.ws.mobileappws.ui.model.request.UpdateUserDetailsRequestModel;
import com.appsdeveloperblog.app.ws.mobileappws.ui.model.request.UserDetailsRequestModel;
import com.appsdeveloperblog.app.ws.mobileappws.ui.model.response.UserRest;
import com.appsdeveloperblog.app.ws.mobileappws.userservice.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("users") // http:localhost:8080/users
public class UserController {

    Map<String, UserRest> users = new HashMap<>();
    private final UserService userService;

    @GetMapping
    public String getUsers(@RequestParam(value="page", defaultValue = "1") int page,
                           @RequestParam(value="limit", defaultValue = "50") int limit,
                           @RequestParam(value="sort", defaultValue = "desc", required = false) String sort) {
        return  "get users was called with page = " + page + " and limit = " + limit + " and sort = " + sort;
    }

    @GetMapping(value = "/{userId}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,})
    public ResponseEntity<UserRest> getUser(@PathVariable String userId) {

        if (users.containsKey(userId)) {
            return new ResponseEntity<>(users.get(userId), HttpStatus.OK);
        }

        if (true) throw new UserServiceException("A user service exception is thrown");

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,})
    public ResponseEntity<UserRest> createUser(@Valid @RequestBody UserDetailsRequestModel request) {

        UserRest returnValue = userService.createUser(request);

        return new ResponseEntity<>(returnValue, HttpStatus.OK);
    }

    @PutMapping(value = "/{userId}",
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE,})
    public ResponseEntity<UserRest> updateUser(@Valid @RequestBody UpdateUserDetailsRequestModel requestModel, @PathVariable String userId) {

        if (users.containsKey(userId)) {
            UserRest storedUserDetails = users.get(userId);
            storedUserDetails.setFirstName(requestModel.getFirstName());
            storedUserDetails.setLastName(requestModel.getLastName());

            users.put(userId, storedUserDetails);

            return new ResponseEntity<>(storedUserDetails, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {

        if (users.containsKey(userId)) {
            users.remove(userId);
            return ResponseEntity.ok().build();

        }
        return ResponseEntity.badRequest().build();
    }


}
