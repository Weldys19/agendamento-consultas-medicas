package br.com.weldyscarmo.agendamento_consultas_medicas.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("Usuário não existe");
    }
}
