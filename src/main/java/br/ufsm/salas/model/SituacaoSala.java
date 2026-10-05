package br.ufsm.salas.model;

/**
 * Situações permitidas para uma sala.
 *
 * <p>Usar um enum impede que a API receba textos arbitrários para a situação.
 * O JSON precisa informar exatamente um destes valores.</p>
 */
public enum SituacaoSala {
    /** Sala liberada para uso. */
    DISPONIVEL,
    /** Sala temporariamente em manutenção. */
    EM_REFORMA,
    /** Sala sem autorização para uso. */
    INTERDITADA
}
