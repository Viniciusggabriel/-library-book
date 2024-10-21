package com.library.application.services;

import com.library.application.dto.requests.UserInLibraryRequest;
import com.library.application.models.UserInLibrary;
import com.library.infra.database.configs.DataBaseSourceConfig;
import com.library.infra.database.repositories.contracts.UserInLibraryRepository;
import com.library.infra.database.repositories.finders.EntityFinder;
import com.library.infra.security.PasswordEncryption;
import com.library.util.errors.exceptions.EntityAttributeAccessException;
import com.library.util.errors.exceptions.InvalidRequestPayloadException;
import com.library.util.errors.exceptions.ValueAlreadyExistsException;
import com.library.util.utilitarian.UpdateObjectFields;
import lombok.RequiredArgsConstructor;
import org.eclipse.jetty.http.HttpStatus;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class UserInLibraryCrudService {
    private static final EntityFinder<UUID, UserInLibrary> finder = new EntityFinder<>(UserInLibrary.class);
    private final UserInLibraryRepository userInLibraryRepository;
    private UserInLibrary entity;

    public UserInLibraryCrudService() {
        this.userInLibraryRepository = new UserInLibraryRepository(DataBaseSourceConfig.getDatabase());
    }

    public void postUserInLibrary(UserInLibraryRequest userInLibraryRequest) throws InvalidRequestPayloadException {
        this.entity = new UserInLibrary();

        this.entity.setDsUserName(userInLibraryRequest.dsUserName());
        this.entity.setDsPassword(PasswordEncryption.setupBcrypt(userInLibraryRequest.dsPassword()));

        try {
            userInLibraryRepository.insertUser(this.entity);
        } catch (IllegalArgumentException exception) {
            throw new InvalidRequestPayloadException(exception.getMessage(), HttpStatus.BAD_REQUEST_400);
        }
    }

    public void putUserInLibrary(UserInLibraryRequest userInLibraryRequest, UUID idUser) throws IllegalAccessException {
        Optional<UserInLibrary> userIsPresent = Optional.ofNullable(finder.findByName("dsUserName", userInLibraryRequest.dsUserName()));
        userIsPresent.ifPresent(userPresent -> {
            throw new ValueAlreadyExistsException(String.format("O nome de usuário já existe dentro do banco de dados: %s", userPresent.getDsUserName()), HttpStatus.BAD_REQUEST_400);
        });

        UserInLibrary userInLibrary = userInLibraryRepository.selectUserByID(idUser);

        this.entity = new UserInLibrary();

        this.entity.setDsUserName(userInLibraryRequest.dsUserName());
        this.entity.setDsPassword(PasswordEncryption.setupBcrypt(userInLibraryRequest.dsPassword()));

        try {
            UpdateObjectFields.updatedObject(userInLibrary, this.entity);
            userInLibraryRepository.updateUser(userInLibrary, idUser);
        } catch (EntityAttributeAccessException exception) {
            throw new EntityAttributeAccessException(exception.getMessage(), exception.getStatusCode());
        }
    }
}
