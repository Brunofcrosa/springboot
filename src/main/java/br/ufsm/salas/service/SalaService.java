package br.ufsm.salas.service;

import br.ufsm.salas.model.Sala;
import br.ufsm.salas.model.SituacaoSala;
import br.ufsm.salas.infra.EntidadeNaoEncontradaException;
import br.ufsm.salas.infra.RegraNegocioException;
import br.ufsm.salas.repository.SalaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
/**
 * Camada que concentra as operações e regras de negócio das salas.
 *
 * <p>O service fica entre o controller e o repository. Assim, o controller
 * não precisa saber como o banco funciona e o repository não precisa saber
 * as regras do sistema.</p>
 */
public class SalaService {

    /** Repository usado para persistir e consultar salas no PostgreSQL. */
    private final SalaRepository salaRepository;

    @Transactional
    /**
     * Salva uma nova sala no banco.
     *
     * @param sala objeto recebido e validado pelo controller
     * @return sala persistida, normalmente já com o {@code id} gerado
     */
    public Sala cadastrarSala(final Sala sala) {
        // JpaRepository.save executa o INSERT quando a entidade ainda não tem id.
        return salaRepository.save(sala);
    }

    @Transactional(readOnly = true)
    /**
     * Busca todas as salas.
     *
     * @return lista de salas cadastradas
     */
    public List<Sala> buscarTodasSalas() {
        // findAll é um método pronto do JpaRepository.
        return salaRepository.findAll();
    }

    @Transactional(readOnly = true)
    /**
     * Busca uma sala pelo id.
     *
     * @param id identificador procurado
     * @return sala encontrada
     * @throws EntidadeNaoEncontradaException se o id não existir
     */
    public Sala buscarSalaPorId(final Long id) {
        // Centralizar a busca evita repetir o mesmo tratamento de 404.
        return buscarOuFalhar(id);
    }

    @Transactional
    /**
     * Atualiza todos os campos editáveis de uma sala existente.
     *
     * <p>Primeiro buscamos a entidade gerenciada pelo JPA. Depois copiamos os
     * novos valores para ela e salvamos o resultado.</p>
     *
     * @param id identificador da sala que será atualizada
     * @param salaAtualizada novos valores da sala
     * @return sala depois da atualização
     * @throws EntidadeNaoEncontradaException se o id não existir
     */
    public Sala atualizarSala(final Long id, final Sala salaAtualizada) {
        // Busca a entidade existente; se não encontrar, a exceção devolve 404.
        final Sala salaGerida = buscarOuFalhar(id);

        // Copiamos cada campo recebido para a entidade que o JPA está gerenciando.
        salaGerida.setNome(salaAtualizada.getNome());
        salaGerida.setCodigo(salaAtualizada.getCodigo());
        salaGerida.setCapacidadeAlunos(salaAtualizada.getCapacidadeAlunos());
        salaGerida.setQuantidadeComputadores(salaAtualizada.getQuantidadeComputadores());
        salaGerida.setAnoConstrucao(salaAtualizada.getAnoConstrucao());
        salaGerida.setArea(salaAtualizada.getArea());
        salaGerida.setSituacao(salaAtualizada.getSituacao());

        // O save persiste as alterações e devolve a versão atualizada.
        return salaRepository.save(salaGerida);
    }

    @Transactional
    /**
     * Exclui uma sala quando a regra de negócio permite.
     *
     * <p>O simulado determina que salas com situação DISPONIVEL não podem ser
     * excluídas. Por isso a verificação acontece antes do delete.</p>
     *
     * @param id identificador da sala que será excluída
     * @throws EntidadeNaoEncontradaException se o id não existir
     * @throws RegraNegocioException se a sala estiver DISPONIVEL
     */
    public void excluirSala(final Long id) {
        // Primeiro buscamos a sala para validar a regra antes do delete.
        final Sala salaExistente = buscarOuFalhar(id);

        // DISPONIVEL é a única situação protegida pelo enunciado.
        if (salaExistente.getSituacao() == SituacaoSala.DISPONIVEL) {
            throw new RegraNegocioException("Salas com situação DISPONIVEL não podem ser excluídas.");
        }

        // Se passou pela regra, o repository pode remover o registro.
        salaRepository.delete(salaExistente);
    }

    /**
     * Reutiliza a busca por id em todas as operações que precisam de uma sala.
     *
     * @param id identificador procurado
     * @return sala encontrada
     * @throws EntidadeNaoEncontradaException se o id não existir
     */
    private Sala buscarOuFalhar(final Long id) {
        // Optional.orElseThrow transforma ausência de registro em erro 404.
        return salaRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Sala não encontrada"));
    }
}
