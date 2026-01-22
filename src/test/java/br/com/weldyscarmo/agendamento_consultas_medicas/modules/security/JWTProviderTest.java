package br.com.weldyscarmo.agendamento_consultas_medicas.modules.security;

import br.com.weldyscarmo.agendamento_consultas_medicas.exceptions.InvalidTokenException;
import br.com.weldyscarmo.agendamento_consultas_medicas.security.JWTProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JWTProviderTest {

    private JWTProvider jwtProvider;

    @BeforeEach
    public void setup(){
        jwtProvider = new JWTProvider("secret");
    }

    @Test
    public void itShouldBePossibleToValidateAToken(){

        Algorithm algorithm = Algorithm.HMAC256("secret");

        String token = JWT.create()
                .sign(algorithm);

        DecodedJWT decodedToken = jwtProvider.validateToken(token);

        assertNotNull(decodedToken);
    }

    @Test
    public void itShouldNotBePossibleToValidateAToken(){

        Algorithm algorithm = Algorithm.HMAC256("otherSecret");

        String token = JWT.create()
                .withExpiresAt(Instant.now().minus(Duration.ofMinutes(1)))
                .sign(algorithm);

        assertThrows(InvalidTokenException.class, () -> {
            this.jwtProvider.validateToken(token);
        });
    }
}
