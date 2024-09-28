package com.library.application.dto.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

public record BorrowedBooksResponse(
        @JsonProperty("_id_borrowed")
        Long idBorrowed,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
        @JsonProperty("ds_borrowed_date")
        ZonedDateTime dsBorrowedDate,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
        @JsonProperty("ds_expected_delivery_date")
        ZonedDateTime dsExpectedDeliveryDate,

        @JsonProperty("data_book")
        BookResponse bookResponse,

        @JsonProperty("data_user")
        UserInLibraryResponse userInLibraryResponse
) {
}
