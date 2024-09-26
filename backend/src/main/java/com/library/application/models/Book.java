package com.library.application.models;

import java.util.List;
import java.util.Objects;

import org.joda.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "TB_BOOK", schema = "LIBRARY_BOOKS")
public class Book {
    @Id
    @Column(name = "ID_BOOK")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBook;

    @Column(name = "DS_BOOK_NAME", nullable = false, length = 150)
    private String dsBookName;

    @Column(name = "DS_AUTHOR_NAME", nullable = false, length = 100)
    private String dsAuthorName;

    @Column(name = "DS_RELEASE_DATE")
    private LocalDateTime dsReleaseDate;

    @Column(name = "DS_SUMMARY", columnDefinition = "TEXT", length = 500)
    private String dsSummary;

    @Column(name = "DS_QUANTITY_BOOK", nullable = false)
    private Integer dsQuantityBooks;

    @ManyToMany(mappedBy = "fkIdBook", cascade = CascadeType.ALL)
    private List<BorrowedBooks> borrowedBooks;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Book book)) return false;
        return Objects.equals(idBook, book.idBook);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idBook);
    }
}
