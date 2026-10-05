package br.ufsm.salas.infra;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
/**
 * Exceção usada quando o recurso solicitado não existe.
 *
 * <p>{@code @ResponseStatus} transforma essa exceção automaticamente em uma
 * resposta HTTP 404.</p>
 */
public class EntidadeNaoEncontradaException extends RuntimeException {

    /**
     * Cria a exceção com a mensagem que será devolvida ao cliente.
     *
     * @param mensagem explicação do recurso que não foi encontrado
     */
    public EntidadeNaoEncontradaException(final String mensagem) {
        super(mensagem);
    }
}
