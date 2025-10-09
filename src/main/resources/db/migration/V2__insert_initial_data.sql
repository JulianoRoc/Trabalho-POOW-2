-- Inserir funcionários iniciais
INSERT INTO funcionario (nome, email, senha, ativo) VALUES
                                                        ('Administrador', 'admin@biblioteca.com', '123456', true),
                                                        ('João Silva', 'joao@biblioteca.com', '123456', true),
                                                        ('Maria Santos', 'maria@biblioteca.com', '123456', true);

-- Inserir alguns livros
INSERT INTO livro (titulo, autor, ano_publicacao, disponivel) VALUES
                                                                  ('Dom Casmurro', 'Machado de Assis', 1899, true),
                                                                  ('O Cortiço', 'Aluísio Azevedo', 1890, true),
                                                                  ('Iracema', 'José de Alencar', 1865, true),
                                                                  ('Memórias Póstumas de Brás Cubas', 'Machado de Assis', 1881, true),
                                                                  ('O Alienista', 'Machado de Assis', 1882, true);

-- Inserir alguns clientes
INSERT INTO cliente (nome, cpf, telefone, endereco) VALUES
                                                        ('Carlos Oliveira', '12345678901', '(11) 9999-8888', 'Rua A, 123 - São Paulo/SP'),
                                                        ('Ana Pereira', '23456789012', '(11) 8888-7777', 'Av. B, 456 - São Paulo/SP'),
                                                        ('Pedro Santos', '34567890123', '(11) 7777-6666', 'Rua C, 789 - São Paulo/SP');