package br.com.weldyscarmo.agendamento_consultas_medicas.modules.appointments.useCases;

import br.com.weldyscarmo.agendamento_consultas_medicas.enums.AppointmentsStatus;
import br.com.weldyscarmo.agendamento_consultas_medicas.exceptions.*;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.appointments.AppointmentsEntity;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.appointments.AppointmentsRepository;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.appointments.MapperAppointmentResponseDTO;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.appointments.dtos.AppointmentsResponseDTO;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.appointments.dtos.CreateAppointmentsRequestDTO;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.doctor.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CreateAppointmentsUseCase {

    @Autowired
    private AppointmentsRepository appointmentsRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorScheduleRepository doctorScheduleRepository;

    @Autowired
    private DoctorTimeBlockRepository doctorTimeBlockRepository;

    public AppointmentsResponseDTO execute(UUID patientId, UUID doctorId, CreateAppointmentsRequestDTO createAppointmentsRequestDTO){

        DoctorEntity doctorEntity = this.doctorRepository.findById(doctorId).orElseThrow(() -> {
            throw new UserNotFoundException();
        });

        LocalDateTime dateConsultation = createAppointmentsRequestDTO.getDate()
                .atTime(createAppointmentsRequestDTO.getStartTime());

        if (dateConsultation.isBefore(LocalDateTime.now())){
            throw new InvalidDateException();
        }

        LocalTime endTime = createAppointmentsRequestDTO.getStartTime().plusMinutes(doctorEntity.getConsultationDurationInMinutes());

        //Retorna os horários de atendimento do médico no dia a ser marcado o agendamento
        List<DoctorScheduleEntity> schedules = doctorAvailable(doctorId, createAppointmentsRequestDTO);

        //Verifica se o horário do agendamento se encaixa no expediente do médico
        checkIfTheAppointmentIsWithinTheDoctorWorkingHours(schedules, createAppointmentsRequestDTO, endTime);

        //Verifica se o horário do agendamento conflita com outro já cadastrado
        checkIfTheAppointmentConflictsWithOthersAlreadyRegistered(createAppointmentsRequestDTO, doctorId, endTime);

        //Verifica se o horário do agendamento conflita com horários bloqueados pelo médico
        checkIfTheAppointmentConflictsWithTimesBlockedByTheDoctor(createAppointmentsRequestDTO, doctorId, endTime);

        AppointmentsEntity appointmentsEntity = builderAppointmentsEntity(createAppointmentsRequestDTO, doctorId,
                patientId, endTime);

        AppointmentsEntity saved = this.appointmentsRepository.save(appointmentsEntity);

        return MapperAppointmentResponseDTO.mapperAppointment(saved);
    }

    private List<DoctorScheduleEntity> doctorAvailable(UUID doctorId,
                                                       CreateAppointmentsRequestDTO createAppointmentsRequestDTO) {

        List<DoctorScheduleEntity> doctorSchedule = this.doctorScheduleRepository.findAllByDoctorId(doctorId);
        List<DoctorScheduleEntity> dailySchedules = new ArrayList<>();

        for (DoctorScheduleEntity schedule : doctorSchedule) {
            if (createAppointmentsRequestDTO.getDate().getDayOfWeek().equals(schedule.getDayOfWeek())) {
                dailySchedules.add(schedule);
            }
        }
        if (dailySchedules.isEmpty()) {
            throw new InvalidAppointmentDayException();
        }

        return dailySchedules;
    }

    private void checkIfTheAppointmentIsWithinTheDoctorWorkingHours(List<DoctorScheduleEntity> schedules,
                                                                    CreateAppointmentsRequestDTO createAppointmentsRequestDTO,
                                                                    LocalTime endTime){

        boolean invalidSchedule = true;
        for (DoctorScheduleEntity scheduleEntity : schedules){
            if (!createAppointmentsRequestDTO.getStartTime().isBefore(scheduleEntity.getStartTime())
                    && !endTime.isAfter(scheduleEntity.getEndTime())){
                invalidSchedule = false;
                break;
            }
        }

        if (invalidSchedule){
            throw new InvalidAppointmentHourException();
        }
    }

    private void checkIfTheAppointmentConflictsWithOthersAlreadyRegistered(CreateAppointmentsRequestDTO createAppointmentsRequestDTO,
                                                                           UUID doctorId,
                                                                           LocalTime endTime){
        List<AppointmentsEntity> appointmentsDoctor = this.appointmentsRepository.findAllByDoctorIdAndDate(doctorId,
                createAppointmentsRequestDTO.getDate());

        for (AppointmentsEntity appointments : appointmentsDoctor) {
            if (createAppointmentsRequestDTO.getStartTime().isBefore(appointments.getEndTime())
                    && endTime.isAfter(appointments.getStartTime())
                    && appointments.getStatus().equals(AppointmentsStatus.SCHEDULED)){
                throw new UnavailableScheduleException();
            }
        }
    }

    private void checkIfTheAppointmentConflictsWithTimesBlockedByTheDoctor(CreateAppointmentsRequestDTO createAppointmentsRequestDTO,
                                                                           UUID doctorId,
                                                                           LocalTime endTime){
        List<DoctorTimeBlockEntity> timesBlock = this.doctorTimeBlockRepository
                .findAllByDoctorIdAndDate(doctorId, createAppointmentsRequestDTO.getDate());

        for (DoctorTimeBlockEntity timeBlock : timesBlock){
            if (createAppointmentsRequestDTO.getStartTime().isBefore(timeBlock.getEndTime())
                    && endTime.isAfter(timeBlock.getStartTime())){
                throw new UnavailableScheduleException();
            }
        }
    }

    private AppointmentsEntity builderAppointmentsEntity(CreateAppointmentsRequestDTO createAppointmentsRequestDTO,
                                                  UUID doctorId, UUID patientId, LocalTime endTime){
        return AppointmentsEntity.builder()
                .date(createAppointmentsRequestDTO.getDate())
                .doctorId(doctorId)
                .patientId(patientId)
                .startTime(createAppointmentsRequestDTO.getStartTime())
                .endTime(endTime)
                .status(AppointmentsStatus.SCHEDULED)
                .build();
    }
}
