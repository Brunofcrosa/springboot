package br.ufsm.salas.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "sala")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
/**
 * Entidade que representa uma sala de aula.
 *
 * <p>As anotações de persistência dizem ao JPA como a classe vira uma tabela,
 * as anotações de validação protegem a entrada da API e as anotações
 * {@code @Schema} documentam os campos no Swagger.</p>
 */
public class Sala {

    /** Identificador gerado automaticamente pelo banco. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único da sala gerado pelo banco", example = "1")
    private Long id;

    /** Nome legível da sala. */
    @NotBlank
    @Size(min = 3, max = 50)
    @Schema(description = "Nome da sala (entre 3 e 50 caracteres)", example = "Laboratório de Redes")
    @Column(nullable = false, length = 50)
    private String nome;

    /** Código curto e único usado para identificar a sala. */
    @NotBlank
    @Size(max = 10)
    @Schema(description = "Código único da sala (até 10 caracteres)", example = "CT-105")
    @Column(name = "codigo_sala", nullable = false, length = 10, unique = true)
    private String codigo;

    /** Número máximo de alunos que a sala comporta. */
    @NotNull
    @Min(10)
    @Max(200)
    @Schema(description = "Capacidade de alunos (entre 10 e 200)", example = "40")
    @Column(name = "capacidade_alunos", nullable = false)
    private Integer capacidadeAlunos;

    /** Quantidade de computadores; o campo é opcional no cadastro. */
    @Min(0)
    @Schema(description = "Quantidade de computadores (maior ou igual a 0)", example = "30")
    @Column(name = "quantidade_computadores")
    private Integer quantidadeComputadores;

    /** Ano em que o prédio ou a sala foi construído. */
    @NotNull
    @Min(1960)
    @Max(2026)
    @Schema(description = "Ano de construção (entre 1960 e 2026)", example = "2015")
    @Column(name = "ano_construcao", nullable = false)
    private Integer anoConstrucao;

    /** Área física da sala em metros quadrados. */
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Schema(description = "Área da sala em metros quadrados (maior que zero)", example = "65.50")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal area;

    /** Situação atual, limitada aos valores do enum {@link SituacaoSala}. */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Schema(description = "Situação atual da sala", example = "DISPONIVEL")
    @Column(nullable = false, length = 20)
    private SituacaoSala situacao;
}
