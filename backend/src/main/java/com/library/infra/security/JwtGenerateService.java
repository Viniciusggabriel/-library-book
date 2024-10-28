package com.library.infra.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

@RequiredArgsConstructor
public class JwtGenerateService {
    // Pega a palavra para assinatura do token
    private static final Dotenv dotenv = Dotenv.load();
    private static final byte[] secretKey = dotenv.get("SIGNATURE_TOKEN_JWT").getBytes();

    // Assinatura em bytes
    private static final byte[] secretKeyBytes = Base64.getDecoder().decode(secretKey);
    protected static final SecretKey secretKeySpec = new SecretKeySpec(secretKeyBytes, "HmacSHA256");

    public static String generateToken(String username) throws JOSEException {
        return generateToken(new HashMap<>(), username);
    }

    public static String generateToken(Map<String, String> setClaims, String username) throws JOSEException {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        Date nextYear = calendar.getTime();

        JWTClaimsSet.Builder jwtClaimsSet = new JWTClaimsSet.Builder();
        jwtClaimsSet
                .subject(username)
                .issuer("MenuStream")
                .expirationTime(nextYear)
                .claim("claims", setClaims)
                .build();

        JWSSigner jwsSigner = new MACSigner(secretKeySpec);

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), jwtClaimsSet.build());
        signedJWT.sign(jwsSigner);

        return signedJWT.serialize();
    }
}
