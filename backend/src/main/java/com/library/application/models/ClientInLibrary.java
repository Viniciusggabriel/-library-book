package com.library.application.models;

import com.library.util.validations.anotations.ValidEmail;
import com.library.util.validations.anotations.ValidPhoneNumber;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "TB_CLIENT_IN_LIBRARY", schema = "LIBRARY_BOOKS")
public class ClientInLibrary {
    @Id
    @GeneratedValue
    @Column(name = "ID_CLIENT", unique = true)
    private UUID idClient;

    @Column(name = "DS_CLIENT_NAME", nullable = false, unique = true, length = 30)
    private String dsClientName;

    @ValidPhoneNumber
    @Column(name = "DS_PHONE_NUMBER", nullable = false)
    private String dsPhoneNumber;

    @ValidEmail
    @Column(name = "DS_EMAIL", nullable = false, length = 200)
    private String dsEmail;

    @Column(name = "DS_DATE_OF_BIRTH")
    @Convert(converter = ZonedDateTime.class)
    private ZonedDateTime dsDateOfBirth;

    @ManyToOne
    @JoinColumn(name = "FK_USER_IN_LIBRARY")
    private UserInLibrary fkIdUserInLibrary;
}
