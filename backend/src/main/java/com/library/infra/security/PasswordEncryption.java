package com.library.infra.security;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordEncryption {
    /**
     * <h3>Método para criptografar a senha usando Bcrypt</h3>
     *
     * @param password -> <strong>Senha a ser criptografada</strong>
     * @return String -> <strong>Senha criptografada</strong>
     */
    public static String setupBcrypt(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    /**
     * <h3>Método para descriptografar a senha</h3>
     *
     * @param password     -> <strong>Senha inserida para verificação</strong>
     * @param hashPassword -> <strong>Senha criptografada do usuário</strong>
     * @return BCrypt.Result -> <strong>Resultado após o decrypt da senha</strong>
     */
    public static BCrypt.Result verifyBcrypt(String password, String hashPassword) {
        return BCrypt.verifyer().verify(password.toCharArray(), hashPassword);
    }
}
