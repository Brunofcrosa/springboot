# Material de estudo - Simulado de API REST

Esta pasta reúne os dois projetos, o enunciado e os PDFs de aula úteis para revisar antes do simulado.

## Projetos

- `projetos/springboot`: primeira versão do projeto.
- `projetos/springbootv2`: versão final limpa, mais indicada para entregar.
- `ControllerMontar.md`: como montar a camada HTTP.
- `RepositoryMontar.md`: como montar o acesso ao banco.
- `ServiceMontar.md`: como montar a camada de negócio.

As duas versões usam Spring Boot, JPA, Flyway, Bean Validation, Lombok, PostgreSQL e Swagger.

## Material recomendado

1. `Aula1-API_Rest.pdf`: revise HTTP, REST, endpoints e status.
2. `Aula1-JPA.pdf`: revise entidade, JPA, repository e relacionamento com banco.
3. `SpringDoc.pdf`: revise Swagger e documentação OpenAPI.
4. `Aula2 (1).pdf`: revise o conteúdo complementar da atividade.
5. `correcao_detalhes.pdf`: confira detalhes esperados na correção.
6. `Simulado (2).pdf`: leia o enunciado por último e tente montar a solução sozinho.

## Ordem para estudar o projeto

1. Abra o `README.md` do `springbootv2`.
2. Leia `Sala.java` e identifique os tipos e validações.
3. Leia `SituacaoSala.java` para entender o enum.
4. Leia `SalaRepository.java` para ver o acesso ao banco.
5. Leia `SalaService.java` e localize a regra de exclusão.
6. Leia `SalaController.java` e compare cada rota com o enunciado.
7. Leia as migrations `V1` e `V2`.
8. Abra o Swagger e execute as requisições manualmente.

## Roteiro para outro simulado

1. Extraia a entidade, os campos, os tipos e as validações.
2. Crie a entidade Java com JPA, Bean Validation e Swagger.
3. Crie a migration V1 com a tabela.
4. Crie a V2 com exemplos, se o enunciado pedir.
5. Crie o repository estendendo `JpaRepository`.
6. Crie o service com CRUD e regras de negócio.
7. Crie o controller com os verbos HTTP e status corretos.
8. Configure PostgreSQL e Flyway.
9. Inicie a aplicação e teste tudo no Swagger.

## Executar o projeto final

No Windows, dentro de `projetos\springbootv2`:

```powershell
docker compose up -d postgres
.\mvnw.cmd spring-boot:run
```

Depois abra:

- Swagger: http://localhost:8999/swagger-ui.html
- OpenAPI: http://localhost:8999/api-docs

Use apenas um dos dois projetos por vez, pois ambos usam a mesma porta e o mesmo banco configurados.

## Checklist rápido

- [ ] Entidade com todos os campos.
- [ ] Tipos corretos, especialmente `BigDecimal` para decimais.
- [ ] Validações com `@NotNull`, `@NotBlank`, `@Size`, `@Min`, `@Max` e `@DecimalMin`.
- [ ] Enum com os valores pedidos.
- [ ] V1 criando a tabela.
- [ ] V2 com registros iniciais.
- [ ] CRUD completo.
- [ ] Regra de negócio no service.
- [ ] Status HTTP corretos.
- [ ] Swagger funcionando.
