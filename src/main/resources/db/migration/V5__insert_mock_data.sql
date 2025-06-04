-- src/main/resources/db/migration/V5__insert_initial_data.sql

-- Dados iniciais para teste (opcional)

-- Inserir mensalistas de exemplo
INSERT INTO mensalistas (nome, data_nascimento, status_mensalista, cpf, idade, documento_valido) VALUES
                                                                                                     ('João Silva Santos', '1985-03-15', 'ATIVO', '12345678901', '38', true),
                                                                                                     ('Maria Oliveira Costa', '1990-07-22', 'ATIVO', '98765432109', '33', true),
                                                                                                     ('Pedro Souza Lima', '1978-11-30', 'INATIVO', '11122233344', '45', false),
                                                                                                     ('Ana Paula Ferreira', '1992-05-08', 'ATIVO', '55566677788', '31', true),
                                                                                                     ('Carlos Eduardo Rocha', '1980-12-03', 'SUSPENSO', '99988877766', '43', true);

-- Inserir veículos de exemplo
INSERT INTO veiculos (nome, placa, ano, data_entrada, mensalista_id) VALUES
                                                                         ('Honda Civic', 'ABC1234', 2020, '2024-01-15 08:30:00', (SELECT id FROM mensalistas WHERE cpf = '12345678901')),
                                                                         ('Toyota Corolla', 'XYZ5678', 2019, '2024-01-15 09:15:00', (SELECT id FROM mensalistas WHERE cpf = '98765432109')),
                                                                         ('Volkswagen Gol', 'DEF9012', 2018, '2024-01-15 10:00:00', (SELECT id FROM mensalistas WHERE cpf = '11122233344')),
                                                                         ('Ford Ka', 'GHI3456', 2021, '2024-01-15 11:30:00', (SELECT id FROM mensalistas WHERE cpf = '55566677788')),
                                                                         ('Chevrolet Onix', 'JKL7890', 2022, '2024-01-15 14:20:00', (SELECT id FROM mensalistas WHERE cpf = '99988877766'));

-- Inserir alguns pagamentos de exemplo
INSERT INTO pagamentos (mensalista_id, veiculo_id, tempo_saida, tarifa) VALUES
                                                                            (
                                                                                (SELECT id FROM mensalistas WHERE cpf = '12345678901'),
                                                                                (SELECT id FROM veiculos WHERE placa = 'ABC1234'),
                                                                                '2024-01-15 18:30:00',
                                                                                15.50
                                                                            ),
                                                                            (
                                                                                (SELECT id FROM mensalistas WHERE cpf = '98765432109'),
                                                                                (SELECT id FROM veiculos WHERE placa = 'XYZ5678'),
                                                                                '2024-01-15 19:00:00',
                                                                                12.00
                                                                            ),
                                                                            (
                                                                                (SELECT id FROM mensalistas WHERE cpf = '55566677788'),
                                                                                (SELECT id FROM veiculos WHERE placa = 'GHI3456'),
                                                                                '2024-01-15 17:45:00',
                                                                                18.75
                                                                            );

---