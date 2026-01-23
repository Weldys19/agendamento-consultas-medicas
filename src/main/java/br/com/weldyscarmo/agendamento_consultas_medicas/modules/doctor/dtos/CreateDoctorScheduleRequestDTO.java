package br.com.weldyscarmo.agendamento_consultas_medicas.modules.doctor.dtos;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
public class CreateDoctorScheduleRequestDTO {

    @NotNull
    private DayOfWeek dayOfWeek;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;
}
