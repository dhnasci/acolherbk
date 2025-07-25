-- Adiciona a coluna empresa_id na tabela psicologo
ALTER TABLE paciente
    ADD COLUMN empresa_id INTEGER;

-- Cria a chave estrangeira para a tabela empresa
ALTER TABLE paciente
    ADD CONSTRAINT fk_paciente_empresa
        FOREIGN KEY (empresa_id)
            REFERENCES empresa(id)
            ON DELETE SET NULL;
