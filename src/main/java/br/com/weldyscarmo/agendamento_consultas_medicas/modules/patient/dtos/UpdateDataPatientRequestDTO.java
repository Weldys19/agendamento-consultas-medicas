package br.com.weldyscarmo.agendamento_consultas_medicas.modules.patient.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateDataPatientRequestDTO {

    private String name;

    @Pattern(regexp = "\\S+", message = "O campo username não pode conter espaços")
    private String username;
}
