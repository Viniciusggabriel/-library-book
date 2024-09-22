package com.library.application.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "TB_USER_IN_LIBRARY", schema = "LIBRARY_BOOKS")
public class UserInLibrary {
    @Id
    @GeneratedValue
    @Column(name = "ID_USER", unique = true)
    private UUID idUser;

    @Column(name = "DS_USERNAME", nullable = false, unique = true, length = 30)
    private String dsUserName;

    // TODO: Implementar criptografia usando Bcrypt
    @Column(name = "DS_PASSWORD", nullable = false, length = 100)
    private String dsPassword;
}