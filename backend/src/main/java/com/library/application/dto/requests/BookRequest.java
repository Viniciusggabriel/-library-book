package com.library.application.dto.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

public record BookRequest(
        @JsonProperty("_id_book")
        Long idBook,

        @JsonProperty("ds_book_name")
        String dsBookName,

        @JsonProperty("ds_author_name")
        String dsAuthorName,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
        @JsonProperty("ds_release_date")
        ZonedDateTime dsReleaseDate,

        @JsonProperty("ds_summary")
        String dsSummary,

        @JsonProperty("ds_quantity_books")
        Integer dsQuantityBooks
) {
}
