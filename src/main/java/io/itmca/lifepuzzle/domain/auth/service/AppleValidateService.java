package io.itmca.lifepuzzle.domain.auth.service;

import com.nimbusds.jwt.SignedJWT;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public class AppleValidateService {

    public String parseToken(String identityToken) throws ParseException {
        var signedJWT = SignedJWT.parse(identityToken);
        var payload = signedJWT.getJWTClaimsSet();

        return (String) payload.getClaim("sub");
    }
}
