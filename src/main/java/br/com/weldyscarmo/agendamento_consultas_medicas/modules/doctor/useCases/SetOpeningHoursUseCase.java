package br.com.weldyscarmo.agendamento_consultas_medicas.modules.doctor.useCases;

import br.com.weldyscarmo.agendamento_consultas_medicas.exceptions.InvalidScheduleException;
import br.com.weldyscarmo.agendamento_consultas_medicas.exceptions.OverlappingSchedulesException;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.doctor.DoctorScheduleEntity;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.doctor.DoctorScheduleRepository;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.doctor.dtos.CreateDoctorScheduleRequestDTO;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.doctor.dtos.DoctorScheduleResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class SetOpeningHoursUseCase {

    @Autowired
    private DoctorScheduleRepository doctorScheduleRepository;

    public DoctorScheduleResponseDTO execute(UUID doctorId, CreateDoctorScheduleRequestDTO createDoctorScheduleRequestDTO){

        if (createDoctorScheduleRequestDTO.getStartTime().
                isAfter(createDoctorScheduleRequestDTO.getEndTime())){
            throw new InvalidScheduleException();
        }

        //Valida se tem sobreposição de horário
        schedulingConflict(createDoctorScheduleRequestDTO, doctorId);

        DoctorScheduleEntity doctorScheduleEntity = builderDoctorScheduleEntity(doctorId, createDoctorScheduleRequestDTO);

        DoctorScheduleEntity saved = this.doctorScheduleRepository.save(doctorScheduleEntity);

        return builderDoctorScheduleResponse(saved);
    }

    private void schedulingConflict(CreateDoctorScheduleRequestDTO createDoctorScheduleRequestDTO, UUID doctorId){

        List<DoctorScheduleEntity> doctorSchedule = this.doctorScheduleRepository.findAllByDoctorId(doctorId);

        doctorSchedule.forEach(doctorScheduleEntity -> {
            if (createDoctorScheduleRequestDTO.getDayOfWeek() == doctorScheduleEntity.getDayOfWeek()
            && !createDoctorScheduleRequestDTO.getStartTime().isAfter(doctorScheduleEntity.getEndTime())
            && !createDoctorScheduleRequestDTO.getEndTime().isBefore(doctorScheduleEntity.getStartTime())){
                throw new OverlappingSchedulesException();
            }
        });
    }

    private DoctorScheduleResponseDTO builderDoctorScheduleResponse(DoctorScheduleEntity doctorSchedule){
        return DoctorScheduleResponseDTO.builder()
                .id(doctorSchedule.getId())
                .doctorId(doctorSchedule.getDoctorId())
                .dayOfWeek(doctorSchedule.getDayOfWeek())
                .startTime(doctorSchedule.getStartTime())
                .endTime(doctorSchedule.getEndTime())
                .build();
    }

    private DoctorScheduleEntity builderDoctorScheduleEntity
            (UUID doctorId, CreateDoctorScheduleRequestDTO createDoctorScheduleRequestDTO){
        return DoctorScheduleEntity.builder()
                .doctorId(doctorId)
                .dayOfWeek(createDoctorScheduleRequestDTO.getDayOfWeek())
                .startTime(createDoctorScheduleRequestDTO.getStartTime())
                .endTime(createDoctorScheduleRequestDTO.getEndTime())
                . build();
    }
}
