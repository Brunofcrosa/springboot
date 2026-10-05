# Como montar um Controller no Spring Boot

## O que é um Controller?

O Controller é a classe responsável por receber requisições HTTP da API.

Ele deve:

1. receber a requisição;
2. pegar os dados enviados pelo cliente;
3. chamar o Service;
4. devolver a resposta com o status HTTP correto.

O Controller não deve acessar o Repository diretamente nem concentrar regras de negócio.

## Onde criar?

Dentro do pacote `controller`:

```text
src/main/java/br/ufsm/salas/controller/SalaController.java
```

## Estrutura básica

```java
package br.ufsm.salas.controller;

import br.ufsm.salas.model.Sala;
import br.ufsm.salas.service.SalaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/salas")
@RequiredArgsConstructor
public class SalaController {

    private final SalaService salaService;
}
```

### Anotações da classe

- `@RestController`: informa que a classe expõe uma API REST.
- `@RequestMapping("/salas")`: define o caminho base das rotas.
- `@RequiredArgsConstructor`: cria o construtor para receber o `SalaService`.

## Controller completo do projeto

```java
package br.ufsm.salas.controller;

import br.ufsm.salas.model.Sala;
import br.ufsm.salas.service.SalaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/salas")
@RequiredArgsConstructor
@Tag(name = "Salas", description = "Cadastro e manutenção das salas de aula da UFSM")
public class SalaController {

    private final SalaService salaService;

    @PostMapping
    @Operation(summary = "Cadastrar sala")
    public ResponseEntity<Sala> cadastrarSala(@RequestBody @Valid final Sala sala) {
        final Sala salaCadastrada = salaService.cadastrarSala(sala);
        return ResponseEntity.status(HttpStatus.CREATED).body(salaCadastrada);
    }

    @GetMapping
    @Operation(summary = "Buscar todas as salas")
    public ResponseEntity<List<Sala>> buscarTodasSalas() {
        return ResponseEntity.ok(salaService.buscarTodasSalas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar sala por id")
    public ResponseEntity<Sala> buscarSalaPorId(@PathVariable final Long id) {
        return ResponseEntity.ok(salaService.buscarSalaPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar sala")
    public ResponseEntity<Sala> atualizarSala(
            @PathVariable final Long id,
            @RequestBody @Valid final Sala sala) {

        return ResponseEntity.ok(salaService.atualizarSala(id, sala));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir sala")
    public ResponseEntity<Void> excluirSala(@PathVariable final Long id) {
        salaService.excluirSala(id);
        return ResponseEntity.noContent().build();
    }
}
```

## Entendendo cada rota

### POST - cadastrar

```java
@PostMapping
public ResponseEntity<Sala> cadastrarSala(
        @RequestBody @Valid Sala sala) {
```

- `@PostMapping`: responde a `POST /salas`.
- `@RequestBody`: transforma o JSON recebido em um objeto `Sala`.
- `@Valid`: executa as validações da entidade.
- `201 Created`: indica que um novo recurso foi criado.

### GET - listar

```java
@GetMapping
public ResponseEntity<List<Sala>> buscarTodasSalas() {
```

Responde a `GET /salas` e devolve uma lista com status `200 OK`.

### GET por id

```java
@GetMapping("/{id}")
public ResponseEntity<Sala> buscarSalaPorId(
        @PathVariable Long id) {
```

- `@GetMapping("/{id}")`: define uma variável na URL.
- `@PathVariable`: captura o valor da URL.

Exemplo:

```text
GET /salas/1
```

### PUT - atualizar

```java
@PutMapping("/{id}")
public ResponseEntity<Sala> atualizarSala(
        @PathVariable Long id,
        @RequestBody @Valid Sala sala) {
```

O `id` identifica o registro que será alterado e o JSON contém os novos dados.

### DELETE - excluir

```java
@DeleteMapping("/{id}")
public ResponseEntity<Void> excluirSala(
        @PathVariable Long id) {
```

Depois que o Service exclui, o Controller retorna `204 No Content`, pois não há corpo para devolver.

## Tabela para lembrar na prova

| Operação | Método | Rota | Status |
| --- | --- | --- | --- |
| cadastrar | `POST` | `/salas` | `201` |
| listar | `GET` | `/salas` | `200` |
| buscar um | `GET` | `/salas/{id}` | `200` |
| atualizar | `PUT` | `/salas/{id}` | `200` |
| excluir | `DELETE` | `/salas/{id}` | `204` |

## Como adaptar para outra atividade

Se o próximo simulado pedir uma entidade `Produto`, faça estas trocas:

```text
SalaController  -> ProdutoController
SalaService     -> ProdutoService
Sala            -> Produto
/salas          -> /produtos
```

O formato das rotas continua igual. O que muda são o nome da entidade, os campos do JSON e as regras do novo enunciado.

## Checklist do Controller

- [ ] Está dentro do pacote `controller`.
- [ ] Usa `@RestController`.
- [ ] Possui `@RequestMapping`.
- [ ] Injeta o Service.
- [ ] Usa `POST`, `GET`, `PUT` e `DELETE` corretamente.
- [ ] Usa `@RequestBody` para JSON.
- [ ] Usa `@PathVariable` para ids na URL.
- [ ] Usa `@Valid` quando há validação.
- [ ] Retorna os status pedidos no enunciado.
- [ ] Não acessa o Repository diretamente.
- [ ] Não coloca regra de negócio dentro do Controller.
