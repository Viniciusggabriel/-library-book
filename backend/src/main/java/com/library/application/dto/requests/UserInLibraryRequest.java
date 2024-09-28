package com.library.application.dto.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserInLibraryRequest(
        @JsonProperty("username")
        String dsUserName,

        @JsonProperty("password")
        String dsPassword
) {
}
