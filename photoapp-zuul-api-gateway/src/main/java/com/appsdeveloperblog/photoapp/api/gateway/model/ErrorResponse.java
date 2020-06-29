package com.appsdeveloperblog.photoapp.api.gateway.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorResponse {

    @JsonProperty("error_code")
    private String errorCode;

    @JsonProperty("error_response")
    private String errorResponse;

    public ErrorResponse() {
    }

    public ErrorResponse(String errorCode, String errorResponse) {
        this.errorCode = errorCode;
        this.errorResponse = errorResponse;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorResponse() {
        return errorResponse;
    }

    public void setErrorResponse(String errorResponse) {
        this.errorResponse = errorResponse;
    }
}
