package br.com.weldyscarmo.agendamento_consultas_medicas.exceptions;

public class InvalidScheduleException extends RuntimeException {
    public InvalidScheduleException() {
        super("O horário inicial não pode ser menor que o horário final");
    }
}
