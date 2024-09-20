package com.library.application.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.joda.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "TB_BOOK", schema = "LIBRARY_BOOKS")
public class Book {
    @Id
    @Column(name = "ID_BOOK")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idBook;

    @Column(name = "DS_BOOK_NAME", nullable = false, length = 150)
    private String dsBookName;

    @Column(name = "DS_AUTHOR_NAME", nullable = false, length = 100)
    private String dsAuthorName;

    @Column(name = "DS_RELEASE_DATE")
    private LocalDateTime dsReleaseDate;

    @Column(name = "DS_SUMMARY", columnDefinition = "TEXT", length = 500)
    private String dsSummary;
}