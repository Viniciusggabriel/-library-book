package com.library.application.services;

import com.library.application.dto.requests.UserInLibraryRequest;
import com.library.application.models.UserInLibrary;
import com.library.infra.database.repositories.finders.EntityFinder;
import com.library.util.errors.exceptions.InvalidRequestPayloadException;
import com.library.util.errors.exceptions.ValueNotFoundException;
import lombok.RequiredArgsConstructor;
import org.eclipse.jetty.http.HttpStatus;

import java.util.Optional;
import java.util.UUID;

import static com.library.infra.security.PasswordEncryption.setupBcrypt;
import static com.library.infra.security.PasswordEncryption.verifyBcrypt;

@RequiredArgsConstructor
public class AuthenticateUserInLibraryService {
    private static final EntityFinder<UUID, UserInLibrary> finder = new EntityFinder<>(UserInLibrary.class);

    public boolean authenticateUserInLibrary(UserInLibraryRequest userInLibraryRequest) throws InvalidRequestPayloadException {
        UserInLibrary user = Optional.ofNullable(finder.findByName("dsUserName", userInLibraryRequest.dsUserName()))
                .orElseThrow(() -> new ValueNotFoundException("O usuário não foi encontrado!", HttpStatus.BAD_REQUEST_400));

        return verifyBcrypt(userInLibraryRequest.dsPassword(), user.getDsPassword());
    }
}
