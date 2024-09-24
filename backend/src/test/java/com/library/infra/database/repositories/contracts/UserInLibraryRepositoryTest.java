package com.library.infra.database.repositories.contracts;

import com.library.DataBaseSourceConfigTest;
import com.library.application.models.UserInLibrary;
import io.ebean.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserInLibraryRepositoryTest {
    private static final Logger logger = LoggerFactory.getLogger(UserInLibraryRepositoryTest.class);

    private UserInLibraryRepository userInLibraryRepository;

    @BeforeEach
    void setUp() {
        Database database = DataBaseSourceConfigTest.databaseTestSetup(List.of(UserInLibrary.class));
        userInLibraryRepository = new UserInLibraryRepository(database);
    }

    @Test
    public void insertFindUpdateDeleteUserInLibrary() {
        UUID USER_UUID = UUID.randomUUID();
        UserInLibrary userInLibrary = new UserInLibrary(USER_UUID, "Testador", "Senha em plain text");
        logger.info("O objeto de usuário foi criado!");

        userInLibraryRepository.insertUser(userInLibrary);
        logger.info("O usuário foi salvo com sucesso!");

        UserInLibrary userForUpdate = new UserInLibrary();
        userForUpdate.setDsUserName("Testador alterado");

        userInLibraryRepository.updateUser(userForUpdate, USER_UUID);

        Optional<UserInLibrary> user = Optional.ofNullable(userInLibraryRepository.selectUserByID(USER_UUID));
        user.ifPresent(userIsPresent -> {
            assertThat(userIsPresent.getIdUser()).isEqualTo(userInLibrary.getIdUser());
            assertThat(userIsPresent.getDsUserName()).isEqualTo(userForUpdate.getDsUserName());
            assertThat(userIsPresent.getDsPassword()).isEqualTo(userInLibrary.getDsPassword());
        });

        userInLibraryRepository.deleteUser(USER_UUID);
    }
}