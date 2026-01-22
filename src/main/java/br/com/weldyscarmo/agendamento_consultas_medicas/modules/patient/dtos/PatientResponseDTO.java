package br.com.weldyscarmo.agendamento_consultas_medicas.modules.patient.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientResponseDTO {

    private UUID id;
    private String name;
    private String username;
    private String email;
    private LocalDateTime createdAt;
}
