package br.com.weldyscarmo.agendamento_consultas_medicas.security;

import br.com.weldyscarmo.agendamento_consultas_medicas.exceptions.InvalidTokenException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;

@Service
public class JWTProvider {

    public DecodedJWT validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256("@Weldys2025");

            var tokenDecoded = JWT.require(algorithm)
                    .build()
                    .verify(token);
            return tokenDecoded;
        }catch (JWTVerificationException e){
            throw new InvalidTokenException();
        }
    }
}
