package br.com.weldyscarmo.agendamento_consultas_medicas.modules.appointments.useCases;

import br.com.weldyscarmo.agendamento_consultas_medicas.enums.AppointmentsStatus;
import br.com.weldyscarmo.agendamento_consultas_medicas.exceptions.AppointmentNotFoundException;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.appointments.AppointmentsEntity;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.appointments.AppointmentsRepository;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.appointments.dtos.AppointmentsResponseDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SetStatusToFinishedUseCase {

    @Autowired
    private AppointmentsRepository appointmentsRepository;

    @Transactional
    public AppointmentsResponseDTO execute(UUID doctorId, UUID appointmentId){

        AppointmentsEntity appointment = this.appointmentsRepository.findByIdAndDoctorId(appointmentId, doctorId)
                .orElseThrow(() -> {
                    throw new AppointmentNotFoundException();
                });

        appointment.setStatus(AppointmentsStatus.FINISHED);

        return builderAppointmentsResponse(appointment);
    }

    private AppointmentsResponseDTO builderAppointmentsResponse(AppointmentsEntity appointmentsEntity){
        return AppointmentsResponseDTO.builder()
                .id(appointmentsEntity.getId())
                .doctorId(appointmentsEntity.getDoctorId())
                .patientId(appointmentsEntity.getPatientId())
                .startTime(appointmentsEntity.getStartTime())
                .endTime(appointmentsEntity.getEndTime())
                .date(appointmentsEntity.getDate())
                .status(appointmentsEntity.getStatus())
                .createdAt(appointmentsEntity.getCreatedAt())
                .build();
    }
}
