-- Adiciona a coluna empresa_id na tabela psicologo
ALTER TABLE psicologo
    ADD COLUMN empresa_id INTEGER;

-- Cria a chave estrangeira para a tabela empresa
ALTER TABLE psicologo
    ADD CONSTRAINT fk_psicologo_empresa
        FOREIGN KEY (empresa_id)
            REFERENCES empresa(id)
            ON DELETE SET NULL;
