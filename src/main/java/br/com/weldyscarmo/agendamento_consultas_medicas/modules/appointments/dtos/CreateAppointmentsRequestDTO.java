package br.com.weldyscarmo.agendamento_consultas_medicas.modules.appointments.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateAppointmentsRequestDTO{

    private DayOfWeek day;
    private LocalTime startTime;
}
