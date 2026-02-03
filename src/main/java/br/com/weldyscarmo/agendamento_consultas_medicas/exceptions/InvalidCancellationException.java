package br.com.weldyscarmo.agendamento_consultas_medicas.exceptions;

public class InvalidCancellationException extends RuntimeException {
    public InvalidCancellationException() {
        super("O cancelamento só pode ser feito até 2h antes da consulta");
    }
}
