package com.library.infra.security;

import at.favre.lib.crypto.bcrypt.BCrypt;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
class PasswordEncryptionTest {
    private static final Logger logger = LoggerFactory.getLogger(PasswordEncryptionTest.class);

    /**
     * <h3>Teste para verificar se a senha foi criptografada de descriptografada corretamente</h3>
     */
    @Test
    public void encryptPassword() {
        String hashPassword = PasswordEncryption.setupBcrypt("Senha");
        logger.info("Senha foi criptografada: {}", hashPassword);

        BCrypt.Result result = PasswordEncryption.verifyBcrypt("Senha", hashPassword);
        logger.info("A senha foi descriptografada: {}", result.verified);
    }
}