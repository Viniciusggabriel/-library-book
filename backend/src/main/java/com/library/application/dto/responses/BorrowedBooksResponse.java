package com.library.application.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.LocalDateTime;

public record BorrowedBooksResponse(
        @JsonProperty("_id_borrowed")
        Long idBorrowed,

        @JsonProperty("ds_borrowed_date")
        LocalDateTime dsBorrowedDate,

        @JsonProperty("ds_expected_delivery_date")
        LocalDateTime dsExpectedDeliveryDate,

        @JsonProperty("data_book")
        BookResponse bookResponse,

        @JsonProperty("data_user")
        UserInLibraryResponse userInLibraryResponse
) {
}
