-- ===== V1__create_mensalistas_table.sql =====
-- src/main/resources/db/migration/V1__create_mensalistas_table.sql

-- Habilita extensão para UUID
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Cria a tabela mensalistas
CREATE TABLE mensalistas (
                             id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                             nome VARCHAR(100) NOT NULL,
                             data_nascimento DATE NOT NULL,
                             status_mensalista VARCHAR(20) NOT NULL,
                             cpf VARCHAR(11) UNIQUE NOT NULL,
                             idade VARCHAR(10),
                             documento_valido BOOLEAN,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Constraint para validar status
ALTER TABLE mensalistas
    ADD CONSTRAINT chk_status_mensalista
        CHECK (status_mensalista IN ('ATIVO', 'INATIVO', 'SUSPENSO', 'PENDENTE'));

-- Constraint para validar CPF (11 dígitos)
ALTER TABLE mensalistas
    ADD CONSTRAINT chk_cpf_length
        CHECK (length(cpf) = 11 AND cpf ~ '^[0-9]+$');

-- Função para atualizar updated_at automaticamente
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$ LANGUAGE 'plpgsql';

-- Trigger para mensalistas
CREATE TRIGGER update_mensalistas_updated_at
    BEFORE UPDATE ON mensalistas
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Comentários nas colunas
COMMENT ON TABLE mensalistas IS 'Tabela de mensalistas do estacionamento';
COMMENT ON COLUMN mensalistas.id IS 'Identificador único do mensalista';
COMMENT ON COLUMN mensalistas.nome IS 'Nome completo do mensalista';
COMMENT ON COLUMN mensalistas.data_nascimento IS 'Data de nascimento do mensalista';
COMMENT ON COLUMN mensalistas.status_mensalista IS 'Status atual do mensalista (ATIVO, INATIVO, SUSPENSO, PENDENTE)';
COMMENT ON COLUMN mensalistas.cpf IS 'CPF do mensalista (apenas números)';
COMMENT ON COLUMN mensalistas.idade IS 'Idade do mensalista em texto';
COMMENT ON COLUMN mensalistas.documento_valido IS 'Indica se o documento foi validado';

---