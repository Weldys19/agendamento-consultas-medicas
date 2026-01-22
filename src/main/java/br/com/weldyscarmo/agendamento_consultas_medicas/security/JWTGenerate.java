package br.com.weldyscarmo.agendamento_consultas_medicas.security;

import br.com.weldyscarmo.agendamento_consultas_medicas.security.dtos.AuthUserDTO;
import br.com.weldyscarmo.agendamento_consultas_medicas.security.dtos.TokenResponseDTO;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class JWTGenerate {

    private String secret;

    public JWTGenerate(@Value("${security.token.secret}") String secret){
        this.secret = secret;
    }

    public TokenResponseDTO generateToken(AuthUserDTO authUserDTO){

        Instant expiresAt = Instant.now().plus(Duration.ofMinutes(15));
        Algorithm algorithm = Algorithm.HMAC256(secret);

        String token = JWT.create().withIssuer("agendamento-medico")
                .withSubject(authUserDTO.getId().toString())
                .withClaim("roles", authUserDTO.getRoles())
                .withExpiresAt(expiresAt)
                .sign(algorithm);

        return TokenResponseDTO.builder()
                .token(token)
                .expiresAt(expiresAt.toEpochMilli())
                .build();
    }
}
