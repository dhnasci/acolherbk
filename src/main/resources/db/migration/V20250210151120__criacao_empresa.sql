-- Criar a tabela empresa
CREATE TABLE empresa (
                         id         SERIAL PRIMARY KEY,
                         nome       VARCHAR(100) NOT NULL,
                         cnpjcpf    VARCHAR(20) UNIQUE NOT NULL,
                         usuario    VARCHAR(12) UNIQUE NOT NULL,
                         email      VARCHAR(50) UNIQUE NOT NULL
);

-- Adicionar a coluna empresa_id na tabela paciente
ALTER TABLE paciente
    ADD COLUMN empresa_id INTEGER,
    ADD CONSTRAINT fk_paciente_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id) ON DELETE SET NULL;

-- Adicionar a coluna empresa_id na tabela psicologo
ALTER TABLE psicologo
    ADD COLUMN empresa_id INTEGER,
    ADD CONSTRAINT fk_psicologo_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id) ON DELETE SET NULL;
