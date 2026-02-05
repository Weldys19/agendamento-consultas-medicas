package br.com.weldyscarmo.agendamento_consultas_medicas.modules.security;

import br.com.weldyscarmo.agendamento_consultas_medicas.security.JWTGenerate;
import br.com.weldyscarmo.agendamento_consultas_medicas.security.dtos.AuthUserDTO;
import br.com.weldyscarmo.agendamento_consultas_medicas.security.dtos.TokenResponseDTO;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class JWTGenerateTest {

    JWTGenerate jwtGenerate;
    AuthUserDTO authUserDTO;

    @BeforeEach
    public void setup(){
        jwtGenerate = new JWTGenerate("secret");
        authUserDTO = AuthUserDTO.builder()
                .id(UUID.randomUUID())
                .roles("roles")
                .build();
    }

    @Test
    public void itShouldBePossibleToGenerateAToken(){

        TokenResponseDTO token = jwtGenerate.generateToken(authUserDTO);

        DecodedJWT tokenDecoded = JWT.decode(token.getToken());

        assertThat(tokenDecoded.getSubject()).isEqualTo(authUserDTO.getId().toString());
        assertThat(tokenDecoded.getClaim("roles").asString()).isEqualTo(authUserDTO.getRoles());
    }
}
