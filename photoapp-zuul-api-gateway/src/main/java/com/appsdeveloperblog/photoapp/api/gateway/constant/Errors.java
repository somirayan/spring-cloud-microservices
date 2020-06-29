package com.appsdeveloperblog.photoapp.api.gateway.constant;

public enum Errors {

        AUTH_UNAUTHORIZED("ERR001" , "Full authentication is required to access this resource"),
        AUTH_ACCESS_DENIED("ERR002" , "Access Denied");

        private final String code;
        private final String message;

        Errors(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() { return code; }
        public String getMessage() { return message; }
}

