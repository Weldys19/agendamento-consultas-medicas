package br.com.weldyscarmo.agendamento_consultas_medicas.modules.appointments;

import br.com.weldyscarmo.agendamento_consultas_medicas.modules.appointments.dtos.AppointmentsResponseDTO;

public class MapperAppointmentResponseDTO {

    private MapperAppointmentResponseDTO(){}

    public static AppointmentsResponseDTO mapperAppointment(AppointmentsEntity appointmentsEntity){
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
