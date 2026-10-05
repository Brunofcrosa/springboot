# Salas API - exemplo para o Simulado (2)

Este projeto implementa o back-end pedido no simulado: uma API REST para cadastrar, consultar, atualizar e excluir salas de aula da UFSM.

Ele foi organizado como material de estudo. Em outro simulado, você pode trocar `Sala` pela entidade pedida e repetir a mesma arquitetura, adaptando campos, migrations, endpoints e regras.

O código-fonte está comentado com Javadocs e comentários explicativos. Leia primeiro o `Controller`, depois o `Service`, o `Repository`, a entidade e as migrations. Essa ordem acompanha o caminho de uma requisição.

> **Sobre `SalaController`**
>
> Sim, `SalaController` está no lugar certo: `src/main/java/br/ufsm/salas/controller`.
> O enunciado não obriga esse nome nem essa pasta, mas essa é a organização convencional em Spring: `Controller` recebe HTTP, `Service` concentra regras, `Repository` acessa o banco e `model` representa os dados.

---

## 1. Requisitos atendidos

O simulado pediu:

- Java 17+ e Spring Boot;
- Flyway;
- JPA/Hibernate;
- Bean Validation;
- Lombok;
- Swagger/OpenAPI;
- entidade `Sala` com campos e validações;
- CRUD REST em `/salas`;
- regra impedindo excluir salas `DISPONIVEL`.

---

## 2. Estrutura de pastas

```text
salas-api/
├── pom.xml
├── compose.yaml
├── README.md
├── mvnw / mvnw.cmd
└── src/
    ├── main/
    │   ├── java/br/ufsm/salas/
    │   │   ├── SalasApiApplication.java
    │   │   ├── controller/SalaController.java
    │   │   ├── model/Sala.java
    │   │   ├── model/SituacaoSala.java
    │   │   ├── repository/SalaRepository.java
    │   │   ├── service/SalaService.java
    │   │   └── infra/
    │   │       ├── EntidadeNaoEncontradaException.java
    │   │       └── RegraNegocioException.java
    │   └── resources/
    │       ├── application.properties
    │       └── db/migration/
    │           ├── V1__criacao_tabela_sala.sql
    │           └── V2__insercao_salas_exemplo.sql
```

### O que cada parte faz

**`pom.xml`**: arquivo do Maven. Define Java, dependências e plugins. Aqui ficam Spring Boot, JPA, Flyway, Validation, Lombok, PostgreSQL e Swagger.

**`SalasApiApplication.java`**: ponto de entrada. `@SpringBootApplication` faz o Spring procurar as outras classes dentro do pacote `br.ufsm.salas`.

**`model`**: representa o domínio. `Sala.java` é a entidade persistida no banco; `SituacaoSala.java` é o enum com os valores permitidos.

**`controller`**: camada HTTP. `SalaController` recebe requisições, chama o service e devolve as respostas.

**`service`**: camada de negócio. `SalaService` faz o CRUD e contém regras como “sala disponível não pode ser excluída”.

**`repository`**: camada de banco. `SalaRepository` estende `JpaRepository` e recebe métodos prontos como `save`, `findAll`, `findById` e `delete`.

**`infra`**: exceções usadas pela aplicação. Uma representa entidade inexistente e outra regra de negócio violada.

**`resources/application.properties`**: configura banco, porta, Hibernate, Flyway e Swagger.

**`resources/db/migration`**: scripts versionados do banco. `V1` cria a tabela e `V2` insere exemplos.

---

## 3. Por que separar Controller, Service e Repository?

Uma requisição `POST /salas` segue este caminho:

```text
Cliente
  ↓ JSON + POST /salas
SalaController
  ↓ chama cadastrarSala
SalaService
  ↓ chama save
SalaRepository
  ↓ JPA/Hibernate
PostgreSQL
```

O controller cuida de HTTP, o service cuida da regra de negócio e o repository cuida do acesso ao banco. Assim cada arquivo tem uma responsabilidade clara.

### `SalaController` é obrigatório?

O nome não é obrigatório pelo enunciado. O que é obrigatório é expor as rotas. Porém, `SalaController` é o nome mais claro e esperado para a classe que controla os endpoints de `Sala`.

### A pasta `controller` é obrigatória?

Também não é uma exigência do compilador. O Spring poderia encontrar a classe em outro pacote, mas separar por responsabilidade é uma boa prática e normalmente é valorizado na avaliação.

---

## 4. Entidade e validações

| Campo | Tipo | Regra | Anotações principais |
| --- | --- | --- | --- |
| `id` | `Long` | gerado pelo banco | `@Id`, `@GeneratedValue` |
| `nome` | `String` | 3 a 50 caracteres | `@NotBlank`, `@Size` |
| `codigo` | `String` | único, até 10 caracteres | `@NotBlank`, `@Size`, `unique` |
| `capacidadeAlunos` | `Integer` | 10 a 200 | `@NotNull`, `@Min`, `@Max` |
| `quantidadeComputadores` | `Integer` | maior ou igual a 0 | `@Min(0)` |
| `anoConstrucao` | `Integer` | 1960 a 2026 | `@NotNull`, `@Min`, `@Max` |
| `area` | `BigDecimal` | maior que 0 | `@NotNull`, `@DecimalMin` |
| `situacao` | `SituacaoSala` | valor do enum | `@NotNull`, `@Enumerated` |

Todos os campos possuem `@Schema`, exigido pelo simulado para aparecerem no Swagger.

Use:

- `String` para texto;
- `Integer` ou `Long` para inteiros;
- `BigDecimal` para valores decimais ou medidas;
- `enum` para opções fixas;
- `Long` para identificadores.

Para `area`, `BigDecimal` é melhor que `double` porque representa casas decimais com mais segurança.

---

## 5. Migrations do Flyway

`V1__criacao_tabela_sala.sql` cria a tabela `sala`, incluindo `UNIQUE` em `codigo_sala`.

`V2__insercao_salas_exemplo.sql` insere três salas: uma `DISPONIVEL`, uma `EM_REFORMA` e uma `INTERDITADA`.

O prefixo define a ordem: `V1`, `V2`, `V3` etc. Não altere uma migration já executada. Para mudar o banco, crie uma nova migration.

---

## 6. Endpoints

| Método | Rota | Função | Status |
| --- | --- | --- | --- |
| `POST` | `/salas` | cadastrar | `201 Created` |
| `GET` | `/salas` | listar todas | `200 OK` |
| `GET` | `/salas/{id}` | buscar por id | `200 OK` |
| `PUT` | `/salas/{id}` | atualizar | `200 OK` |
| `DELETE` | `/salas/{id}` | excluir | `204 No Content` |

Exemplo de JSON:

```json
{
  "nome": "Laboratório de Programação",
  "codigo": "CT-301",
  "capacidadeAlunos": 40,
  "quantidadeComputadores": 40,
  "anoConstrucao": 2020,
  "area": 72.5,
  "situacao": "DISPONIVEL"
}
```

Erros esperados:

- `400`: JSON inválido ou valor fora da validação;
- `404`: id não encontrado;
- `422`: regra de negócio violada;
- erro de constraint: código duplicado.

---

## 7. Como executar no Windows

### Pré-requisitos

- Java 17 ou superior;
- Docker Desktop, se usar o PostgreSQL do `compose.yaml`;
- porta `5499` disponível para o banco.

### Subir o PostgreSQL

Na pasta do projeto:

```powershell
docker compose up -d postgres
```

Configuração criada: database `salasdb`, usuário `postgres`, senha `postgres`, porta `5499`.

### Iniciar a API

```powershell
.\mvnw.cmd spring-boot:run
```

A API ficará em `http://localhost:8999`.

Swagger UI: http://localhost:8999/swagger-ui.html

OpenAPI JSON: http://localhost:8999/api-docs

---

## 8. Como refazer outro simulado parecido

### Passo 1 - extrair requisitos

Antes de codar, liste: entidade, campos, tipos, obrigatoriedade, validações, enums, migrations, endpoints, status HTTP, regras de negócio, tecnologias e documentação.

### Passo 2 - criar o projeto

No Spring Initializr, selecione Spring Web, Spring Data JPA, PostgreSQL Driver, Flyway Migration, Validation e Lombok. Adicione Swagger/OpenAPI se necessário.

### Passo 3 - criar os pacotes

Para uma entidade `Produto`, por exemplo:

```text
br.seuprojeto.produto
├── controller/ProdutoController.java
├── model/Produto.java
├── repository/ProdutoRepository.java
├── service/ProdutoService.java
└── infra/...
```

### Passo 4 - fazer a entidade

Use `@Entity`, `@Table`, `@Id`, `@GeneratedValue`, `@Column`, Bean Validation, `@Schema` e os tipos corretos.

### Passo 5 - fazer as migrations

Crie `V1__criacao_tabela_produto.sql` e, se o enunciado pedir dados iniciais, `V2__insercao_produtos_exemplo.sql`.

### Passo 6 - fazer o repository

```java
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
```

### Passo 7 - fazer o service

Implemente cadastrar, listar, buscar por id, atualizar e excluir. Coloque neste arquivo as regras como “não pode excluir se estiver ativo”.

### Passo 8 - fazer o controller

```text
POST   /produtos       cadastrar
GET    /produtos       listar
GET    /produtos/{id}  buscar um
PUT    /produtos/{id}  atualizar
DELETE /produtos/{id}  excluir
```

Use o verbo HTTP e o status exigidos no enunciado.

### Passo 9 - tratar erros

Crie uma exceção para entidade inexistente e outra para regra de negócio. Use `@ResponseStatus` ou `@RestControllerAdvice`.

### Passo 10 - testar pelo Swagger

Abra o Swagger, execute todas as rotas, teste uma entrada inválida, um id inexistente e a regra de negócio. Esse é o fluxo de teste manual usado neste exemplo.

### Como adaptar para outra atividade

Se o próximo exercício pedir, por exemplo, uma API de `Produto`, use esta correspondência:

| Projeto atual | Novo projeto |
| --- | --- |
| `Sala.java` | `Produto.java` |
| `SalaController.java` | `ProdutoController.java` |
| `SalaService.java` | `ProdutoService.java` |
| `SalaRepository.java` | `ProdutoRepository.java` |
| `SituacaoSala.java` | outro enum, se necessário |
| tabela `sala` | tabela `produto` |
| rota `/salas` | rota `/produtos` |

A ordem para montar deve ser:

1. ler o enunciado e separar requisitos;
2. definir a entidade e os tipos;
3. colocar validações e `@Schema`;
4. criar a V1 com a tabela;
5. criar a V2 com exemplos, se solicitado;
6. criar o repository estendendo `JpaRepository`;
7. criar o service com CRUD e regras;
8. criar o controller com as rotas e status;
9. configurar o banco no `application.properties`;
10. iniciar a aplicação e testar no Swagger.

Se a nova atividade tiver relacionamentos, como `Produto` pertencendo a uma `Categoria`, primeiro desenhe as tabelas e as chaves estrangeiras. Depois modele as relações com `@ManyToOne`, `@OneToMany` ou a anotação pedida pelo domínio. Não copie os campos de `Sala` sem conferir o novo enunciado.

Se a nova atividade não pedir banco, JPA ou Flyway, não adicione essas camadas só por copiar este exemplo. Use apenas as tecnologias realmente solicitadas.

---

## 9. Checklist da prova

```text
[ ] Entidade com todos os campos do enunciado.
[ ] Tipos Java corretos.
[ ] Campos obrigatórios com @NotNull ou @NotBlank.
[ ] Limites com @Min, @Max, @Size ou @DecimalMin.
[ ] Enum persistido como String.
[ ] V1 criando a tabela.
[ ] UNIQUE criada quando necessária.
[ ] V2 com os exemplos pedidos.
[ ] Repository estendendo JpaRepository.
[ ] Service com o CRUD.
[ ] Regra de negócio no Service.
[ ] Controller com verbos HTTP corretos.
[ ] Status 201, 200 e 204 corretos.
[ ] Exceções coerentes.
[ ] @Schema em todos os campos.
[ ] Swagger funcionando.
[ ] Rotas testadas diretamente no Swagger.
```

---

## 10. Problemas comuns

### Porta 8999 ocupada

Altere temporariamente `server.port=8998` no `application.properties` ou encerre a aplicação antiga.

### Erro de banco

Confira se o PostgreSQL está ativo e se a URL é `jdbc:postgresql://localhost:5499/salasdb`, com usuário e senha `postgres`.

### Migration não executa

Confira o caminho `src/main/resources/db/migration`, o padrão `V1__`, `V2__` e se você não alterou uma migration já executada.

### Validação não funciona

Confira a dependência Validation, o `@Valid` no controller, as anotações da entidade e o header `Content-Type: application/json`.

---

## 11. Comandos resumidos

```powershell
cd C:\Users\bruno\Desktop\ESTUDAR-AULA\salas-api\salas-api
docker compose up -d postgres
.\mvnw.cmd spring-boot:run
```
