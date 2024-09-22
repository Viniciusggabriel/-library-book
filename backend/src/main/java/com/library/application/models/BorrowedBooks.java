package com.library.application.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.joda.time.LocalDateTime;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "TB_BORROWED_BOOK", schema = "LIBRARY_BOOKS")
public class BorrowedBooks {
    @Id
    @Column(name = "ID_BORROWED")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBorrowed;

    @Column(name = "DS_BORROWED_DATE", nullable = false)
    private LocalDateTime dsBorrowedDate = LocalDateTime.now();

    @Column(name = "DS_EXPECTED_DELIVERY_DATE", nullable = false)
    private LocalDateTime dsExpectedDeliveryDate;

    @ManyToOne
    @JoinColumn(name = "FK_USER_IN_LIBRARY")
    private UserInLibrary fkIdUserInLibrary;

    @ManyToMany
    @JoinTable(
            schema = "LIBRARY_BOOKS",
            name = "JT_BOOK_BORROWED_BOOKS",
            joinColumns = @JoinColumn(name = "FK_ID_BORROWED_BOOK", referencedColumnName = "ID_BORROWED"),
            inverseJoinColumns = @JoinColumn(name = "FK_ID_BOOK", referencedColumnName = "ID_BOOK")
    )
    private List<Book> fkIdBook;
}
