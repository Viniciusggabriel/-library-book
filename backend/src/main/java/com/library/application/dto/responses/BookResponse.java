package com.library.application.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.LocalDateTime;


public record BookResponse(
        @JsonProperty("_id_book")
        Long idBook,

        @JsonProperty("ds_quantity_books")
        Integer dsQuantityBooks,

        @JsonProperty("_data_book")
        DataBook dataBook
) {
        public static BookResponse of(Long idBook, Integer dsQuantityBooks, String dsBookName, String dsAuthorName, LocalDateTime dsReleaseDate, String dsSummary) {
                return new BookResponse(idBook, dsQuantityBooks, new DataBook(dsBookName, dsAuthorName, dsReleaseDate, dsSummary));
        }

        public record DataBook(
                @JsonProperty("ds_book_name")
                String dsBookName,

                @JsonProperty("ds_author_name")
                String dsAuthorName,

                @JsonProperty("ds_release_date")
                LocalDateTime dsReleaseDate,

                @JsonProperty("ds_summary")
                String dsSummary
        ) {
        }
}

