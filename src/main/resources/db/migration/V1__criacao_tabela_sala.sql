-- V1: cria a estrutura permanente usada pela entidade Sala.
-- O Flyway executa este arquivo antes das demais migrations.
CREATE TABLE sala (
                      -- BIGSERIAL gera um id numérico automaticamente.
                      id BIGSERIAL PRIMARY KEY,
                      -- Campo obrigatório com limite igual ao da entidade Java.
                      nome VARCHAR(50) NOT NULL,
                      -- UNIQUE impede dois registros com o mesmo código.
                      codigo_sala VARCHAR(10) NOT NULL UNIQUE,
                      -- Capacidade obrigatória de alunos.
                      capacidade_alunos INT NOT NULL,
                      -- Campo opcional; se não for informado no SQL, começa em 0.
                      quantidade_computadores INT DEFAULT 0,
                      -- Ano obrigatório da construção.
                      ano_construcao INT NOT NULL,
                      -- DECIMAL preserva as casas decimais da área.
                      area DECIMAL(10,2) NOT NULL,
                      -- O valor é armazenado como texto do enum.
                      situacao VARCHAR(20) NOT NULL
);
