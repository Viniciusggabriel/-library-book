package com.library.application.dto.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;


public record BookResponse(
        @JsonProperty("_id_book")
        Long idBook,

        @JsonProperty("ds_quantity_books")
        Integer dsQuantityBooks,

        @JsonProperty("_data_book")
        DataBook dataBook
) {
    public static BookResponse of(Long idBook, Integer dsQuantityBooks, String dsBookName, String dsAuthorName, ZonedDateTime dsReleaseDate, String dsSummary) {
        return new BookResponse(idBook, dsQuantityBooks, new DataBook(dsBookName, dsAuthorName, dsReleaseDate, dsSummary));
    }

    public record DataBook(
            @JsonProperty("ds_book_name")
            String dsBookName,

            @JsonProperty("ds_author_name")
            String dsAuthorName,

            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
            @JsonProperty("ds_release_date")
            ZonedDateTime dsReleaseDate,

            @JsonProperty("ds_summary")
            String dsSummary
    ) {
    }
}

