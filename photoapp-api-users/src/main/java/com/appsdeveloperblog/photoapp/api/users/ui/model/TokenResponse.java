package com.appsdeveloperblog.photoapp.api.users.ui.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TokenResponse {

    @JsonProperty("access_token")
    private String accessToken;
}
