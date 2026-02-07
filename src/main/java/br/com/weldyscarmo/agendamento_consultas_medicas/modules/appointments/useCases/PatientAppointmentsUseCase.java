package br.com.weldyscarmo.agendamento_consultas_medicas.modules.appointments.useCases;

import br.com.weldyscarmo.agendamento_consultas_medicas.modules.appointments.AppointmentsEntity;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.appointments.AppointmentsRepository;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.appointments.MapperAppointmentResponseDTO;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.appointments.dtos.AppointmentsResponseDTO;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.patient.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PatientAppointmentsUseCase {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentsRepository appointmentsRepository;

    public List<AppointmentsResponseDTO> execute(UUID patientId){

        List<AppointmentsEntity> appointmentsEntity = this.appointmentsRepository
                .findAllByPatientId(patientId);

        List<AppointmentsResponseDTO> appointmentsResponseDTO = new ArrayList<>();

        appointmentsEntity.forEach(appointment -> {
            appointmentsResponseDTO.add(MapperAppointmentResponseDTO.mapperAppointment(appointment));
        });

        return appointmentsResponseDTO;
    }
}
