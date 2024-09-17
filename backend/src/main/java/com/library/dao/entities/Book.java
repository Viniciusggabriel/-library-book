package com.library.dao.entities;

import jakarta.persistence.*;
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
    private Integer idBook;

    @Column(name = "DS_BOOK_NAME")
    private String dsBookName;
}
