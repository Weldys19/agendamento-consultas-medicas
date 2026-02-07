package br.com.weldyscarmo.agendamento_consultas_medicas.modules.doctor.utils;

import br.com.weldyscarmo.agendamento_consultas_medicas.modules.doctor.DoctorEntity;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.doctor.dtos.DoctorResponseDTO;

public class MapperDoctorResponseDTO {

    private MapperDoctorResponseDTO(){}

    public static DoctorResponseDTO mapperDoctor(DoctorEntity doctorEntity){
        return DoctorResponseDTO.builder()
                .id(doctorEntity.getId())
                .name(doctorEntity.getName())
                .email(doctorEntity.getEmail())
                .specialty(doctorEntity.getSpecialty())
                .consultationDurationInMinutes(doctorEntity.getConsultationDurationInMinutes())
                .createdAt(doctorEntity.getCreatedAt())
                .build();
    }
}
