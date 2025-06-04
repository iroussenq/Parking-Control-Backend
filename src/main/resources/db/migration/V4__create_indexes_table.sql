-- ===== V4__create_indexes.sql =====
-- src/main/resources/db/migration/V4__create_indexes.sql

-- Índices para performance em mensalistas
CREATE INDEX idx_mensalistas_cpf ON mensalistas(cpf);
CREATE INDEX idx_mensalistas_status ON mensalistas(status_mensalista);
CREATE INDEX idx_mensalistas_nome ON mensalistas(nome);
CREATE INDEX idx_mensalistas_data_nascimento ON mensalistas(data_nascimento);

-- Índices para performance em veiculos
CREATE INDEX idx_veiculos_placa ON veiculos(placa);
CREATE INDEX idx_veiculos_mensalista_id ON veiculos(mensalista_id);
CREATE INDEX idx_veiculos_ano ON veiculos(ano);
CREATE INDEX idx_veiculos_data_entrada ON veiculos(data_entrada);

-- Índices para performance em pagamentos
CREATE INDEX idx_pagamentos_mensalista_id ON pagamentos(mensalista_id);
CREATE INDEX idx_pagamentos_veiculo_id ON pagamentos(veiculo_id);
CREATE INDEX idx_pagamentos_tempo_saida ON pagamentos(tempo_saida);
CREATE INDEX idx_pagamentos_created_at ON pagamentos(created_at);
CREATE INDEX idx_pagamentos_tarifa ON pagamentos(tarifa) WHERE tarifa IS NOT NULL;

-- Índices compostos para consultas otimizadas
CREATE INDEX idx_veiculos_mensalista_ano ON veiculos(mensalista_id, ano);
CREATE INDEX idx_pagamentos_mensalista_periodo ON pagamentos(mensalista_id, tempo_saida);
CREATE INDEX idx_pagamentos_veiculo_periodo ON pagamentos(veiculo_id, tempo_saida);
CREATE INDEX idx_pagamentos_periodo_tarifa ON pagamentos(tempo_saida, tarifa) WHERE tarifa IS NOT NULL;

-- Comentários nos índices
COMMENT ON INDEX idx_mensalistas_cpf IS 'Índice para busca rápida por CPF';
COMMENT ON INDEX idx_veiculos_placa IS 'Índice para busca rápida por placa';
COMMENT ON INDEX idx_pagamentos_tempo_saida IS 'Índice para consultas por período';

---