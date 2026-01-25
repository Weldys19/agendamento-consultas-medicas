package br.com.weldyscarmo.agendamento_consultas_medicas.exceptions;

public class InvalidAppointmentDayException extends RuntimeException {
    public InvalidAppointmentDayException() {
        super("O médico não estará trabalhando nesse dia");
    }
}
