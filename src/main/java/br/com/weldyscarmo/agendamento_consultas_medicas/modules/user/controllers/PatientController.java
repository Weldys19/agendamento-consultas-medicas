package br.com.weldyscarmo.agendamento_consultas_medicas.modules.user.controllers;

import br.com.weldyscarmo.agendamento_consultas_medicas.modules.user.PatientEntity;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.user.dtos.CreatePatientRequestDTO;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.user.dtos.CreatePatientResponseDTO;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.user.useCases.CreatePatientUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patient")
@Tag(name = "Informações do paciente")
public class PatientController {

    @Autowired
    private CreatePatientUseCase createPatientUseCase;

    @Operation(summary = "Cadastrar paciente",
            description = "Essa função é responsável pelo cadastramento de novos pacientes")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {
                    @Content(schema = @Schema(implementation = CreatePatientResponseDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Usuário já existe")
    })
    @PostMapping("/")
    public ResponseEntity<Object> create(@Valid @RequestBody CreatePatientRequestDTO createPatientRequestDTO){
        var result = this.createPatientUseCase.execute(createPatientRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
