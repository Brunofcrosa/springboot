# Como montar um Repository no Spring Boot

## O que é um Repository?

O Repository é a camada responsável por conversar com o banco de dados.

Ele fica entre o Service e o PostgreSQL:

```text
Controller → Service → Repository → Banco de dados
```

O Repository não deve conter regra de negócio. Ele deve apenas buscar, salvar e excluir dados.

## Onde criar?

Dentro do pacote `repository`:

```text
src/main/java/br/ufsm/salas/repository/SalaRepository.java
```

## Repository completo do projeto

```java
package br.ufsm.salas.repository;

import br.ufsm.salas.model.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Long> {
}
```

## Entendendo cada parte

### `@Repository`

Informa ao Spring que a interface pertence à camada de persistência.

### `JpaRepository<Sala, Long>`

O primeiro tipo é a entidade que será persistida: `Sala`.

O segundo tipo é o tipo do id: `Long`.

Ao estender `JpaRepository`, você recebe métodos prontos:

| Método | Função |
| --- | --- |
| `save(entidade)` | salva ou atualiza |
| `findAll()` | busca todos |
| `findById(id)` | busca pelo id |
| `existsById(id)` | verifica se existe |
| `delete(entidade)` | exclui |
| `count()` | conta registros |

Por isso o Repository do projeto fica vazio: o CRUD básico já está pronto no Spring Data JPA.

## Como o Service usa o Repository

```java
private final SalaRepository salaRepository;

public Sala cadastrarSala(Sala sala) {
    return salaRepository.save(sala);
}
```

O Controller não chama `save` diretamente. Ele chama o Service, e o Service chama o Repository.

## Criando consultas personalizadas

Se a atividade pedir busca por código, você pode criar:

```java
Optional<Sala> findByCodigo(String codigo);
```

O Spring entende o nome do método e gera a consulta automaticamente.

Outros exemplos:

```java
List<Sala> findBySituacao(SituacaoSala situacao);

List<Sala> findByCapacidadeAlunosGreaterThan(Integer capacidade);

boolean existsByCodigo(String codigo);
```

Só crie métodos personalizados quando o enunciado pedir uma consulta diferente do CRUD.

## Como adaptar para outro simulado

Para uma entidade `Produto`:

```java
package br.seuprojeto.produto.repository;

import br.seuprojeto.produto.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
```

Troque:

```text
Sala           → Produto
SalaRepository → ProdutoRepository
```

## Checklist do Repository

- [ ] Está dentro do pacote `repository`.
- [ ] Usa `@Repository`.
- [ ] Estende `JpaRepository`.
- [ ] Usa a entidade correta.
- [ ] Usa o tipo correto do id.
- [ ] Não contém regra de negócio.
- [ ] Possui métodos personalizados somente quando necessários.
