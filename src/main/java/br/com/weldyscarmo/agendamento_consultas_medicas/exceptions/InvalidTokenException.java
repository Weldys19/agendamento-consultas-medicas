package br.com.weldyscarmo.agendamento_consultas_medicas.exceptions;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super("Token inválido");
    }
}
