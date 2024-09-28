package com.library.application.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record UserInLibraryResponse(
        @JsonProperty("_id_user")
        UUID idUser,

        @JsonProperty("username")
        String dsUserName

) {
}
