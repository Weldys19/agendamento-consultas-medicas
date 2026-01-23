package br.com.weldyscarmo.agendamento_consultas_medicas.exceptions;

public class OverlappingSchedulesException extends RuntimeException {
    public OverlappingSchedulesException() {
        super("Não pode ter sobreposição de horário");
    }
}
