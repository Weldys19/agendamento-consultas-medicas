package br.com.weldyscarmo.agendamento_consultas_medicas.modules.patient.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePatientRequestDTO {

    @NotBlank
    @Schema(example = "Weldys Carmo")
    private String name;

    @NotBlank
    @Pattern(regexp = "\\S+", message = "O campo username não pode conter espaços")
    @Schema(example = "weldyscarmo2025")
    private String username;

    @Email(message = "O campo email deve conter um email válido")
    @Schema(example = "weldys@gmail.com")
    private String email;

    @Length(min = 10, message = "O campo password deve ter no mínimo 10 caracteres")
    @Schema(example = "12345678")
    private String password;
}
