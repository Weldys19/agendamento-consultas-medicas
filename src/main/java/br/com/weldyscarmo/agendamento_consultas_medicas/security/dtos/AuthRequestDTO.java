package br.com.weldyscarmo.agendamento_consultas_medicas.security.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequestDTO {

    @Schema(example = "weldys@gmail.com")
    private String email;

    @Schema(example = "12345678")
    private String password;
}
