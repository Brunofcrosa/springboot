package br.ufsm.salas.infra;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
/**
 * Exceção usada quando uma operação viola uma regra do domínio.
 *
 * <p>Neste projeto, ela é usada quando alguém tenta excluir uma sala
 * DISPONIVEL. {@code @ResponseStatus} transforma o erro em HTTP 422.</p>
 */
public class RegraNegocioException extends RuntimeException {

    /**
     * Cria a exceção com a mensagem da regra violada.
     *
     * @param mensagem explicação do motivo do bloqueio
     */
    public RegraNegocioException(final String mensagem) {
        super(mensagem);
    }
}
