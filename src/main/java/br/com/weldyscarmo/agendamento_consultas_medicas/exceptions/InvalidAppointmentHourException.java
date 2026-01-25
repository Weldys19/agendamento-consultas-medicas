package br.com.weldyscarmo.agendamento_consultas_medicas.exceptions;

public class InvalidAppointmentHourException extends RuntimeException {
    public InvalidAppointmentHourException() {
        super("O médico não estará atendendo nesse horário");
    }
}
