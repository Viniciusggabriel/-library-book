package com.library.application.dto.requests;

import com.library.application.models.Book;
import com.library.application.models.BorrowedBooks;
import org.joda.time.LocalDateTime;

import java.util.List;
import java.util.UUID;

public record BorrowedBooksRequest(
        LocalDateTime dsExpectedDeliveryDate,
        UUID fkIdUserInLibrary,
        List<Long> fkIdBook
) {
    public static BorrowedBooksRequest fromEntity(BorrowedBooks borrowedBooks) {
        List<Long> bookIds = borrowedBooks.getFkIdBook().stream()
                .map(Book::getIdBook)
                .toList();

        return new BorrowedBooksRequest(
                borrowedBooks.getDsExpectedDeliveryDate(),
                borrowedBooks.getFkIdUserInLibrary() != null ? borrowedBooks.getFkIdUserInLibrary().getIdUser() : null,
                bookIds
        );
    }
}
