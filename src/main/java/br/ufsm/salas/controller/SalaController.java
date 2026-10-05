package br.ufsm.salas.controller;

import br.ufsm.salas.model.Sala;
import br.ufsm.salas.service.SalaService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/salas")
@RequiredArgsConstructor
@Tag(name = "Salas", description = "Cadastro e manutenção das salas de aula da UFSM")
/**
 * Camada HTTP da API de salas.
 *
 * <p>O controller recebe as requisições, encaminha o trabalho para o service
 * e devolve a resposta com o status HTTP adequado. As regras de negócio ficam
 * em {@code SalaService}, e não neste arquivo.</p>
 */
public class SalaController {

    /** Service que concentra o cadastro e as regras de negócio das salas. */
    private final SalaService salaService;

    @PostMapping
    @Operation(summary = "Cadastrar sala")
    /**
     * Cadastra uma nova sala.
     *
     * @param sala dados recebidos no corpo da requisição; {@code @Valid}
     *             executa as validações da entidade
     * @return a sala salva com status HTTP 201 (Created)
     */
    public ResponseEntity<Sala> cadastrarSala(@RequestBody @Valid final Sala sala) {
        // O service salva a entidade e devolve o objeto já persistido.
        final Sala salaCadastrada = salaService.cadastrarSala(sala);

        // Cadastro bem-sucedido usa 201 Created, e não o 200 padrão.
        return ResponseEntity.status(HttpStatus.CREATED).body(salaCadastrada);
    }

    @GetMapping
    @Operation(summary = "Buscar todas as salas")
    /**
     * Busca todas as salas cadastradas.
     *
     * @return lista de salas com status HTTP 200 (OK)
     */
    public ResponseEntity<List<Sala>> buscarTodasSalas() {
        // Delega a consulta ao service e devolve a lista com 200 OK.
        return ResponseEntity.ok(salaService.buscarTodasSalas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar sala por id")
    /**
     * Busca uma sala pelo identificador informado na URL.
     *
     * @param id identificador da sala
     * @return sala encontrada com status HTTP 200 (OK)
     * @throws br.ufsm.salas.infra.EntidadeNaoEncontradaException
     *         quando não existe sala com o id informado
     */
    public ResponseEntity<Sala> buscarSalaPorId(@PathVariable final Long id) {
        // O service lança 404 automaticamente se o id não existir.
        return ResponseEntity.ok(salaService.buscarSalaPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar sala")
    /**
     * Atualiza os dados de uma sala existente.
     *
     * @param id identificador da sala que será alterada
     * @param sala novos dados recebidos no corpo da requisição
     * @return sala atualizada com status HTTP 200 (OK)
     */
    public ResponseEntity<Sala> atualizarSala(@PathVariable final Long id, @RequestBody @Valid final Sala sala) {
        // O id vem da URL; os novos valores vêm do corpo JSON.
        return ResponseEntity.ok(salaService.atualizarSala(id, sala));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir sala")
    /**
     * Exclui uma sala, respeitando as regras de negócio do service.
     *
     * @param id identificador da sala que será excluída
     * @return resposta vazia com status HTTP 204 (No Content)
     */
    public ResponseEntity<Void> excluirSala(@PathVariable final Long id) {
        // A regra que pode bloquear a exclusão é verificada dentro do service.
        salaService.excluirSala(id);

        // Exclusão bem-sucedida não precisa devolver corpo, por isso usa 204.
        return ResponseEntity.noContent().build();
    }
}
