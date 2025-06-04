
-- Cria tipo ENUM para tipos de operação de auditoria
CREATE TYPE audit_operation AS ENUM ('INSERT', 'UPDATE', 'DELETE');

-- Tabela de auditoria para rastrear mudanças importantes
CREATE TABLE audit_log (
                           id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                           table_name VARCHAR(50) NOT NULL,
                           record_id UUID NOT NULL,
                           operation audit_operation NOT NULL,
                           old_values JSONB,
                           new_values JSONB,
                           changed_by VARCHAR(100),
                           changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices para a tabela de auditoria
CREATE INDEX idx_audit_log_table_name ON audit_log(table_name);
CREATE INDEX idx_audit_log_record_id ON audit_log(record_id);
CREATE INDEX idx_audit_log_changed_at ON audit_log(changed_at);
CREATE INDEX idx_audit_log_operation ON audit_log(operation);

-- Função genérica para auditoria
CREATE OR REPLACE FUNCTION audit_trigger_function()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'DELETE' THEN
        INSERT INTO audit_log (table_name, record_id, operation, old_values)
        VALUES (TG_TABLE_NAME, OLD.id, 'DELETE', row_to_json(OLD));
RETURN OLD;
ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO audit_log (table_name, record_id, operation, old_values, new_values)
        VALUES (TG_TABLE_NAME, NEW.id, 'UPDATE', row_to_json(OLD), row_to_json(NEW));
RETURN NEW;
ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO audit_log (table_name, record_id, operation, new_values)
        VALUES (TG_TABLE_NAME, NEW.id, 'INSERT', row_to_json(NEW));
RETURN NEW;
END IF;
RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- Triggers de auditoria para as tabelas principais (comentados por padrão)
-- Descomente se quiser ativar auditoria completa

-- CREATE TRIGGER audit_mensalistas
--     AFTER INSERT OR UPDATE OR DELETE ON mensalistas
--     FOR EACH ROW EXECUTE FUNCTION audit_trigger_function();

-- CREATE TRIGGER audit_veiculos
--     AFTER INSERT OR UPDATE OR DELETE ON veiculos
--     FOR EACH ROW EXECUTE FUNCTION audit_trigger_function();

-- CREATE TRIGGER audit_pagamentos
--     AFTER INSERT OR UPDATE OR DELETE ON pagamentos
--     FOR EACH ROW EXECUTE FUNCTION audit_trigger_function();

COMMENT ON TABLE audit_log IS 'Tabela de auditoria para rastrear mudanças nos dados';
COMMENT ON FUNCTION audit_trigger_function IS 'Função genérica para triggers de auditoria';