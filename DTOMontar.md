# Como montar um DTO no Spring Boot

## O que é DTO?

DTO significa **Data Transfer Object**. É um objeto usado para transportar dados entre a API e o cliente.

Sem DTO, o Controller recebe e devolve diretamente a entidade `Sala`:

```text
JSON → Sala → Service → Banco
Banco → Sala → JSON
```

Com DTO, a API separa o formato do JSON da entidade do banco:

```text
JSON → SalaRequestDTO → Service → Sala → Banco
Banco → Sala → Service → SalaResponseDTO → JSON
```

## O simulado exige DTO?

Não. O Simulado (2) não pede DTO. Por isso a versão final usa a entidade `Sala` diretamente no Controller e atende ao enunciado.

DTO é uma melhoria arquitetural. Use quando a atividade pedir separação entre API e banco, quando houver campos internos ou quando o formato de entrada for diferente do formato de saída.

## Onde criar?

Crie um pacote `dto`:

```text
src/main/java/br/ufsm/salas/dto/
├── SalaRequestDTO.java
└── SalaResponseDTO.java
```

Normalmente usamos um DTO para entrada e outro para saída.

## DTO de entrada

O DTO de entrada representa os dados que o cliente pode enviar:

```java
package br.ufsm.salas.dto;

import br.ufsm.salas.model.SituacaoSala;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record SalaRequestDTO(
        @NotBlank @Size(min = 3, max = 50) String nome,
        @NotBlank @Size(max = 10) String codigo,
        @NotNull @Min(10) @Max(200) Integer capacidadeAlunos,
        @Min(0) Integer quantidadeComputadores,
        @NotNull @Min(1960) @Max(2026) Integer anoConstrucao,
        @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal area,
        @NotNull SituacaoSala situacao
) {
}
```

`record` cria uma classe compacta para transportar dados, com construtor e métodos de acesso gerados pelo Java.

## DTO de saída

O DTO de saída representa o que a API devolve:

```java
package br.ufsm.salas.dto;

import br.ufsm.salas.model.Sala;
import br.ufsm.salas.model.SituacaoSala;

import java.math.BigDecimal;

public record SalaResponseDTO(
        Long id,
        String nome,
        String codigo,
        Integer capacidadeAlunos,
        Integer quantidadeComputadores,
        Integer anoConstrucao,
        BigDecimal area,
        SituacaoSala situacao
) {
    public static SalaResponseDTO fromEntity(Sala sala) {
        return new SalaResponseDTO(
                sala.getId(), sala.getNome(), sala.getCodigo(),
                sala.getCapacidadeAlunos(), sala.getQuantidadeComputadores(),
                sala.getAnoConstrucao(), sala.getArea(), sala.getSituacao()
        );
    }
}
```

O método `fromEntity` transforma a entidade do banco em um DTO de resposta.

## Usando DTO no Controller

```java
@PostMapping
public ResponseEntity<SalaResponseDTO> cadastrar(
        @RequestBody @Valid SalaRequestDTO dados) {

    Sala sala = salaService.cadastrarSala(dados);
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(SalaResponseDTO.fromEntity(sala));
}
```

O `@Valid` continua funcionando porque as validações ficam no DTO de entrada.

## Usando DTO no Service

```java
public Sala cadastrarSala(SalaRequestDTO dados) {
    Sala sala = Sala.builder()
            .nome(dados.nome())
            .codigo(dados.codigo())
            .capacidadeAlunos(dados.capacidadeAlunos())
            .quantidadeComputadores(dados.quantidadeComputadores())
            .anoConstrucao(dados.anoConstrucao())
            .area(dados.area())
            .situacao(dados.situacao())
            .build();

    return salaRepository.save(sala);
}
```

O Service converte os dados externos para a entidade antes de salvar.

## Vantagens

- evita expor diretamente a entidade do banco;
- permite esconder campos internos;
- permite ter JSON de entrada e saída diferentes;
- centraliza validações da entrada;
- facilita mudanças futuras no banco sem quebrar a API.

## Quando não usar na prova

Se o enunciado pedir somente um CRUD simples e não mencionar DTO, você pode usar a entidade diretamente, como neste projeto. Isso reduz o código e continua atendendo ao simulado.

Se o professor pedir DTO, crie o pacote, os DTOs, faça o mapeamento no Service e altere os tipos do Controller.

## Como adaptar para outra atividade

```text
SalaRequestDTO  → ProdutoRequestDTO
SalaResponseDTO → ProdutoResponseDTO
Sala            → Produto
```

Leve para o DTO somente os campos que a atividade realmente precisa receber ou devolver.

## Checklist

- [ ] O enunciado pede ou justifica DTO.
- [ ] Existe um pacote `dto`.
- [ ] O DTO de entrada possui validações.
- [ ] O DTO de saída não expõe dados desnecessários.
- [ ] O Service converte DTO para entidade.
- [ ] O Service converte entidade para DTO.
- [ ] O Controller não acessa o Repository.
