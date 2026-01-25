package br.com.weldyscarmo.agendamento_consultas_medicas.modules.appointments.controllers;

import br.com.weldyscarmo.agendamento_consultas_medicas.modules.appointments.AppointmentsEntity;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.appointments.dtos.CreateAppointmentsRequestDTO;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.appointments.dtos.CreateAppointmentsResponseDTO;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.appointments.useCases.CreateAppointmentsUseCase;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/patient")
public class AppointmentsController {

    @Autowired
    private CreateAppointmentsUseCase createAppointmentsUseCase;

    @PostMapping("/appointment/{doctorId}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<CreateAppointmentsResponseDTO> create(@RequestBody CreateAppointmentsRequestDTO createAppointmentsRequestDTO,
                                                     HttpServletRequest request, @PathVariable UUID doctorId){
        UUID patientId = UUID.fromString(request.getAttribute("user_id").toString());

        CreateAppointmentsResponseDTO result = this.createAppointmentsUseCase.execute(patientId, doctorId,
                createAppointmentsRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
