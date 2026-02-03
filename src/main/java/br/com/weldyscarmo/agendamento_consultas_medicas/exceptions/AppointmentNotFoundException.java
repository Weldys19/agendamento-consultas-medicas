package br.com.weldyscarmo.agendamento_consultas_medicas.exceptions;

public class AppointmentNotFoundException extends RuntimeException {
    public AppointmentNotFoundException() {
        super("Agendamento não encontrado");
    }
}
