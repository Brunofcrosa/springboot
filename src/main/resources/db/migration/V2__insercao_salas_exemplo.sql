-- V2: dados iniciais para facilitar a demonstração no Swagger.
-- Cada registro usa uma situação diferente, como pedido no simulado.

-- Sala liberada para uso.
INSERT INTO sala (nome, codigo_sala, capacidade_alunos, quantidade_computadores, ano_construcao, area, situacao)
VALUES ('Sala 101', 'CT-101', 40, 0, 1980, 55.5, 'DISPONIVEL');

-- Laboratório temporariamente em reforma.
INSERT INTO sala (nome, codigo_sala, capacidade_alunos, quantidade_computadores, ano_construcao, area, situacao)
VALUES ('Laboratório de Redes', 'CT-205', 30, 30, 2010, 80.0, 'EM_REFORMA');

-- Auditório interditado.
INSERT INTO sala (nome, codigo_sala, capacidade_alunos, quantidade_computadores, ano_construcao, area, situacao)
VALUES ('Auditório Principal', 'CT-AUD', 200, 2, 1975, 150.0, 'INTERDITADA');
