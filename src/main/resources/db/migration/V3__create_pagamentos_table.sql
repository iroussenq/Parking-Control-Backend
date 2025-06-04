-- ===== V3__create_pagamentos_table.sql =====
-- src/main/resources/db/migration/V3__create_pagamentos_table.sql

-- Cria a tabela pagamentos
CREATE TABLE pagamentos (
                            id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                            mensalista_id UUID NOT NULL,
                            veiculo_id UUID NOT NULL,
                            tempo_saida TIMESTAMP NOT NULL,
                            tarifa DECIMAL(10,2),
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Foreign keys
ALTER TABLE pagamentos
    ADD CONSTRAINT fk_pagamentos_mensalista
        FOREIGN KEY (mensalista_id) REFERENCES mensalistas(id) ON DELETE CASCADE;

ALTER TABLE pagamentos
    ADD CONSTRAINT fk_pagamentos_veiculo
        FOREIGN KEY (veiculo_id) REFERENCES veiculos(id) ON DELETE CASCADE;

-- Constraints de validação
ALTER TABLE pagamentos
    ADD CONSTRAINT chk_tarifa_positiva
        CHECK (tarifa IS NULL OR tarifa > 0);

-- Constraint para garantir que tempo_saida não seja no futuro distante
ALTER TABLE pagamentos
    ADD CONSTRAINT chk_tempo_saida_valido
        CHECK (tempo_saida <= CURRENT_TIMESTAMP + INTERVAL '1 day');

-- Trigger para pagamentos
CREATE TRIGGER update_pagamentos_updated_at
    BEFORE UPDATE ON pagamentos
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Comentários
COMMENT ON TABLE pagamentos IS 'Tabela de registros de pagamentos do estacionamento';
COMMENT ON COLUMN pagamentos.id IS 'Identificador único do pagamento';
COMMENT ON COLUMN pagamentos.mensalista_id IS 'Referência ao mensalista';
COMMENT ON COLUMN pagamentos.veiculo_id IS 'Referência ao veículo';
COMMENT ON COLUMN pagamentos.tempo_saida IS 'Data e hora de saída do estacionamento';
COMMENT ON COLUMN pagamentos.tarifa IS 'Valor cobrado pelo estacionamento';
