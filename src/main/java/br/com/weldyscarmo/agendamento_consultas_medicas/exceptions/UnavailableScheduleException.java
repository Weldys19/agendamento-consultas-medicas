package br.com.weldyscarmo.agendamento_consultas_medicas.exceptions;

public class UnavailableScheduleException extends RuntimeException {
    public UnavailableScheduleException() {
        super("Já tem consultas cadastradas nesse horário");
    }
}
