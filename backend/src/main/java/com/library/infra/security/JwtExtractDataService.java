package com.library.infra.security;

import com.library.util.errors.exceptions.InternalServerException;
import com.library.util.errors.exceptions.TokenParseException;
import com.library.util.errors.exceptions.TokenValidationException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.eclipse.jetty.http.HttpStatus;

import java.text.ParseException;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

import static com.library.infra.security.JwtGenerateService.secretKeySpec;

@RequiredArgsConstructor
public class    JwtExtractDataService {
    private final JwtGenerateService jwtGenerateService;

    public static String extractUserName(String token) {
        Optional<Object> subject = Optional.ofNullable(extractClaim(token, JWTClaimsSet::getSubject));

        return (String) subject.orElseThrow(() ->
                new TokenParseException(
                        "Erro ao realizar parse do seu token, subject invalido!",
                        HttpStatus.BAD_REQUEST_400
                )
        );
    }

    public static Date extractExpiration(String token) {
        Optional<Object> expiration = Optional.ofNullable(extractClaim(token, JWTClaimsSet::getExpirationTime));

        return (Date) expiration.orElseThrow(() ->
                new TokenParseException(
                        "Erro ao realizar parse da data de expiração. O campo expirationTime pode estar ausente ou não ser uma Date.",
                        HttpStatus.BAD_REQUEST_400
                )
        );
    }


    public static Object extractClaim(String token, Function<JWTClaimsSet, ?> claimExtractor) {
        try {
            JWTClaimsSet claims = extractAllClaims(token);
            return claimExtractor.apply(claims);
        } catch (TokenValidationException | TokenParseException exception) {
            throw new TokenParseException(
                    exception.getMessage(),
                    exception.getStatusCode()
            );
        } catch (ParseException | JOSEException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static JWTClaimsSet extractAllClaims(String token) throws JOSEException, ParseException {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(secretKeySpec);

            if (!signedJWT.verify(verifier)) {
                throw new TokenValidationException("Erro ao validar seu token, token com assinatura invalida!", HttpStatus.UNAUTHORIZED_401);
            }

            return signedJWT.getJWTClaimsSet();
        } catch (ParseException exception) {
            throw new TokenParseException(
                    "Erro ao extrair claims do token JWT: \n${exception.message}",
                    HttpStatus.BAD_REQUEST_400
            );
        } catch (TokenValidationException exception) {
            throw new InternalServerException(
                    String.format("Erro interno no servidor: %s", exception.getMessage())
            );
        }
    }
}
