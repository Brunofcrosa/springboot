# Como montar um Service no Spring Boot

## O que é um Service?

O Service é a camada que concentra as operações e regras de negócio da aplicação.

Ele fica entre o Controller e o Repository:

```text
Controller → Service → Repository → Banco de dados
```

O Controller recebe HTTP. O Repository conversa com o banco. O Service coordena o processo e decide se a operação pode acontecer.

## Onde criar?

Dentro do pacote `service`:

```text
src/main/java/br/ufsm/salas/service/SalaService.java
```

## Service completo do projeto

```java
package br.ufsm.salas.service;

import br.ufsm.salas.infra.EntidadeNaoEncontradaException;
import br.ufsm.salas.infra.RegraNegocioException;
import br.ufsm.salas.model.Sala;
import br.ufsm.salas.model.SituacaoSala;
import br.ufsm.salas.repository.SalaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalaService {

    private final SalaRepository salaRepository;

    @Transactional
    public Sala cadastrarSala(final Sala sala) {
        return salaRepository.save(sala);
    }

    @Transactional(readOnly = true)
    public List<Sala> buscarTodasSalas() {
        return salaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Sala buscarSalaPorId(final Long id) {
        return buscarOuFalhar(id);
    }

    @Transactional
    public Sala atualizarSala(final Long id, final Sala salaAtualizada) {
        final Sala salaGerida = buscarOuFalhar(id);

        salaGerida.setNome(salaAtualizada.getNome());
        salaGerida.setCodigo(salaAtualizada.getCodigo());
        salaGerida.setCapacidadeAlunos(salaAtualizada.getCapacidadeAlunos());
        salaGerida.setQuantidadeComputadores(salaAtualizada.getQuantidadeComputadores());
        salaGerida.setAnoConstrucao(salaAtualizada.getAnoConstrucao());
        salaGerida.setArea(salaAtualizada.getArea());
        salaGerida.setSituacao(salaAtualizada.getSituacao());

        return salaRepository.save(salaGerida);
    }

    @Transactional
    public void excluirSala(final Long id) {
        final Sala salaExistente = buscarOuFalhar(id);

        if (salaExistente.getSituacao() == SituacaoSala.DISPONIVEL) {
            throw new RegraNegocioException("Salas com situação DISPONIVEL não podem ser excluídas.");
        }

        salaRepository.delete(salaExistente);
    }

    private Sala buscarOuFalhar(final Long id) {
        return salaRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Sala não encontrada"));
    }
}
```

## Entendendo cada parte

### `@Service`

Registra a classe como um componente de negócio do Spring.

### `@RequiredArgsConstructor`

Cria automaticamente um construtor para receber o `SalaRepository`.

### `@Transactional`

Indica que a operação deve ser executada dentro de uma transação.

- Use `@Transactional` para salvar, atualizar ou excluir.
- Use `@Transactional(readOnly = true)` para consultas.

## Métodos do CRUD

### Cadastro

```java
public Sala cadastrarSala(Sala sala) {
    return salaRepository.save(sala);
}
```

O `save` faz o `INSERT` quando a entidade ainda não possui id.

### Listagem

```java
public List<Sala> buscarTodasSalas() {
    return salaRepository.findAll();
}
```

O `findAll` busca todos os registros.

### Busca por id

```java
public Sala buscarSalaPorId(Long id) {
    return buscarOuFalhar(id);
}
```

O método auxiliar evita repetir a mesma lógica de erro em buscar, atualizar e excluir.

### Atualização

Primeiro busca o registro existente. Depois copia os valores recebidos para a entidade gerenciada pelo JPA e salva.

Não é recomendado simplesmente criar outra entidade com o mesmo id sem entender o contexto do JPA. Buscar a entidade antes deixa o fluxo mais claro.

### Exclusão e regra de negócio

```java
if (salaExistente.getSituacao() == SituacaoSala.DISPONIVEL) {
    throw new RegraNegocioException("...");
}
```

Essa regra fica no Service porque pertence ao negócio. O Controller apenas chama `excluirSala`.

## Exceções

```java
orElseThrow(() -> new EntidadeNaoEncontradaException("Sala não encontrada"));
```

Se o id não existir, a aplicação retorna `404 Not Found`.

Se a regra for violada, `RegraNegocioException` retorna `422 Unprocessable Entity`.

## Como adaptar para outro simulado

Para uma entidade `Produto`:

```text
SalaService     → ProdutoService
SalaRepository  → ProdutoRepository
Sala            → Produto
SituacaoSala    → enum do novo domínio, se necessário
```

Depois troque os campos copiados no método de atualização e substitua as regras pelo que o novo enunciado pedir.

## Checklist do Service

- [ ] Está dentro do pacote `service`.
- [ ] Usa `@Service`.
- [ ] Injeta o Repository.
- [ ] Possui os métodos pedidos no CRUD.
- [ ] Usa `@Transactional` nas alterações.
- [ ] Usa `readOnly = true` nas consultas.
- [ ] Trata id inexistente.
- [ ] Contém as regras de negócio.
- [ ] Não devolve status HTTP diretamente.
- [ ] Não recebe responsabilidade de montar rotas.
