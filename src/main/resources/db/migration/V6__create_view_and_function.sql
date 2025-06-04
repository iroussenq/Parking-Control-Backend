-- src/main/resources/db/migration/V6__create_views_and_functions.sql

-- View para relatório de mensalistas ativos com seus veículos
CREATE OR REPLACE VIEW vw_mensalistas_ativos AS
SELECT
    m.id,
    m.nome,
    m.cpf,
    m.data_nascimento,
    m.status_mensalista,
    COUNT(v.id) as total_veiculos,
    array_agg(v.placa ORDER BY v.nome) FILTER (WHERE v.id IS NOT NULL) as placas_veiculos
FROM mensalistas m
         LEFT JOIN veiculos v ON m.id = v.mensalista_id
WHERE m.status_mensalista = 'ATIVO'
GROUP BY m.id, m.nome, m.cpf, m.data_nascimento, m.status_mensalista
ORDER BY m.nome;

-- View para relatório de pagamentos por período
CREATE OR REPLACE VIEW vw_pagamentos_resumo AS
SELECT
    m.nome as mensalista_nome,
    m.cpf,
    v.nome as veiculo_nome,
    v.placa,
    p.tempo_saida,
    p.tarifa,
    EXTRACT(MONTH FROM p.tempo_saida) as mes,
    EXTRACT(YEAR FROM p.tempo_saida) as ano
FROM pagamentos p
         JOIN mensalistas m ON p.mensalista_id = m.id
         JOIN veiculos v ON p.veiculo_id = v.id
ORDER BY p.tempo_saida DESC;

-- Função para calcular total de tarifas por mensalista em um período
CREATE OR REPLACE FUNCTION calcular_total_tarifas_periodo(
    p_mensalista_id UUID,
    p_data_inicio DATE,
    p_data_fim DATE
)
RETURNS DECIMAL(10,2) AS $$
DECLARE
total DECIMAL(10,2) := 0;
BEGIN
SELECT COALESCE(SUM(tarifa), 0)
INTO total
FROM pagamentos
WHERE mensalista_id = p_mensalista_id
  AND tempo_saida::DATE BETWEEN p_data_inicio AND p_data_fim
    AND tarifa IS NOT NULL;

RETURN total;
END;
$$ LANGUAGE plpgsql;

-- Função para buscar mensalistas por nome (busca flexível)
CREATE OR REPLACE FUNCTION buscar_mensalistas_por_nome(p_nome TEXT)
RETURNS TABLE(
    id UUID,
    nome VARCHAR(100),
    cpf VARCHAR(11),
    status_mensalista VARCHAR(20)
) AS $$
BEGIN
RETURN QUERY
SELECT m.id, m.nome, m.cpf, m.status_mensalista
FROM mensalistas m
WHERE m.nome ILIKE '%' || p_nome || '%'
ORDER BY m.nome;
END;
$$ LANGUAGE plpgsql;

-- Função para validar CPF
CREATE OR REPLACE FUNCTION validar_cpf(p_cpf VARCHAR(11))
RETURNS BOOLEAN AS $$
DECLARE
i INTEGER;
    soma INTEGER := 0;
    digito1 INTEGER;
    digito2 INTEGER;
BEGIN
    -- Verifica se o CPF tem 11 dígitos
    IF length(p_cpf) != 11 OR p_cpf ~ '[^0-9]' THEN
        RETURN FALSE;
END IF;
    
    -- Verifica se todos os dígitos são iguais
    IF p_cpf ~ '^(\d)\1{10}$' THEN
        RETURN FALSE;
END IF;
    
    -- Calcula o primeiro dígito verificador
FOR i IN 1..9 LOOP
        soma := soma + (substr(p_cpf, i, 1)::INTEGER * (11 - i));
END LOOP;
    
    digito1 := 11 - (soma % 11);
    IF digito1 >= 10 THEN
        digito1 := 0;
END IF;
    
    -- Verifica o primeiro dígito
    IF substr(p_cpf, 10, 1)::INTEGER != digito1 THEN
        RETURN FALSE;
END IF;
    
    -- Calcula o segundo dígito verificador
    soma := 0;
FOR i IN 1..10 LOOP
        soma := soma + (substr(p_cpf, i, 1)::INTEGER * (12 - i));
END LOOP;
    
    digito2 := 11 - (soma % 11);
    IF digito2 >= 10 THEN
        digito2 := 0;
END IF;
    
    -- Verifica o segundo dígito
    IF substr(p_cpf, 11, 1)::INTEGER != digito2 THEN
        RETURN FALSE;
END IF;

RETURN TRUE;
END;
$$ LANGUAGE plpgsql;

-- Comentários nas views e funções
COMMENT ON VIEW vw_mensalistas_ativos IS 'View com mensalistas ativos e seus veículos';
COMMENT ON VIEW vw_pagamentos_resumo IS 'View com resumo de pagamentos por mensalista';
COMMENT ON FUNCTION calcular_total_tarifas_periodo IS 'Calcula total de tarifas por mensalista em período específico';
COMMENT ON FUNCTION buscar_mensalistas_por_nome IS 'Busca mensalistas por nome (busca flexível)';
COMMENT ON FUNCTION validar_cpf IS 'Valida formato e dígitos verificadores do CPF';

---