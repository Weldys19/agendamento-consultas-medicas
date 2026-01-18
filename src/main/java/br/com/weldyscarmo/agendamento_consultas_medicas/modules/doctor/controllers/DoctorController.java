package br.com.weldyscarmo.agendamento_consultas_medicas.modules.doctor.controllers;

import br.com.weldyscarmo.agendamento_consultas_medicas.modules.doctor.dtos.CreateDoctorRequestDTO;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.doctor.dtos.CreateDoctorResponseDTO;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.doctor.useCases.CreateDoctorUseCase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private CreateDoctorUseCase createDoctorUseCase;

    @PostMapping("/")
    public ResponseEntity<CreateDoctorResponseDTO> create(@Valid @RequestBody CreateDoctorRequestDTO createDoctorRequestDTO){
        var result = this.createDoctorUseCase.execute(createDoctorRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
