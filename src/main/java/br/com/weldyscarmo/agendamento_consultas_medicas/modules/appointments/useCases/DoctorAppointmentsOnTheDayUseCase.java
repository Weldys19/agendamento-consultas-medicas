package br.com.weldyscarmo.agendamento_consultas_medicas.modules.appointments.useCases;

import br.com.weldyscarmo.agendamento_consultas_medicas.modules.appointments.AppointmentsEntity;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.appointments.AppointmentsRepository;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.appointments.MapperAppointmentResponseDTO;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.appointments.dtos.AppointmentsResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DoctorAppointmentsOnTheDayUseCase {

    @Autowired
    private AppointmentsRepository appointmentsRepository;

    public List<AppointmentsResponseDTO> execute(UUID doctorId){
        
        List<AppointmentsEntity> appointmentsEntity = this.appointmentsRepository
                .findAllByDoctorIdAndDate(doctorId, LocalDate.now());
        
        List<AppointmentsResponseDTO> appointmentsResponseDTO = new ArrayList<>();
        
        if (!appointmentsEntity.isEmpty()){
            appointmentsEntity.forEach(appointment -> {
               appointmentsResponseDTO.add(MapperAppointmentResponseDTO.mapperAppointment(appointment));
            });   
        }
        return appointmentsResponseDTO;
    }
}
