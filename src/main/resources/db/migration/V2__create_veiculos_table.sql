-- ===== V2__create_veiculos_table.sql =====
-- src/main/resources/db/migration/V2__create_veiculos_table.sql

-- Cria a tabela veiculos
CREATE TABLE veiculos (
                          id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                          nome VARCHAR(50) NOT NULL,
                          placa VARCHAR(8) UNIQUE NOT NULL,
                          ano INTEGER NOT NULL,
                          data_entrada TIMESTAMP NOT NULL,
                          mensalista_id UUID,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Foreign key para mensalista
ALTER TABLE veiculos
    ADD CONSTRAINT fk_veiculos_mensalista
        FOREIGN KEY (mensalista_id) REFERENCES mensalistas(id) ON DELETE SET NULL;

-- Constraints de validação
ALTER TABLE veiculos
    ADD CONSTRAINT chk_ano_veiculo
        CHECK (ano >= 1900 AND ano <= 2030);

-- Validação de placa (formato brasileiro antigo: ABC1234 ou Mercosul: ABC1D23)
ALTER TABLE veiculos
    ADD CONSTRAINT chk_placa_formato
        CHECK (
            placa ~ '^[A-Z]{3}[0-9]{4}$' OR  -- Formato antigo: ABC1234
    placa ~ '^[A-Z]{3}[0-9]{1}[A-Z]{1}[0-9]{2}$'  -- Formato Mercosul: ABC1D23
    );

-- Trigger para veiculos
CREATE TRIGGER update_veiculos_updated_at
    BEFORE UPDATE ON veiculos
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Comentários
COMMENT ON TABLE veiculos IS 'Tabela de veículos cadastrados';
COMMENT ON COLUMN veiculos.id IS 'Identificador único do veículo';
COMMENT ON COLUMN veiculos.nome IS 'Nome/modelo do veículo';
COMMENT ON COLUMN veiculos.placa IS 'Placa do veículo (formato brasileiro)';
COMMENT ON COLUMN veiculos.ano IS 'Ano de fabricação do veículo';
COMMENT ON COLUMN veiculos.data_entrada IS 'Data e hora de entrada no estacionamento';
COMMENT ON COLUMN veiculos.mensalista_id IS 'Referência ao mensalista proprietário';
