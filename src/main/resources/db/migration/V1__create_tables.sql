CREATE TABLE cliente (
                         id_cliente BIGSERIAL NOT NULL PRIMARY KEY,
                         uuid UUID NOT NULL UNIQUE DEFAULT gen_random_uuid(),
                         nome VARCHAR(100) NOT NULL,
                         cpf VARCHAR(11) NOT NULL UNIQUE,
                         telefone VARCHAR(15),
                         endereco TEXT,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE funcionario (
                             id_funcionario BIGSERIAL NOT NULL PRIMARY KEY,
                             uuid UUID NOT NULL UNIQUE DEFAULT gen_random_uuid(),
                             nome VARCHAR(100) NOT NULL,
                             email VARCHAR(100) NOT NULL UNIQUE,
                             senha VARCHAR(255) NOT NULL,
                             ativo BOOLEAN DEFAULT true,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE categoria (
                           id_categoria BIGSERIAL NOT NULL PRIMARY KEY,
                           uuid UUID NOT NULL UNIQUE DEFAULT gen_random_uuid(),
                           nome VARCHAR(50) NOT NULL UNIQUE,
                           descricao TEXT,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE livro (
                       id_livro BIGSERIAL NOT NULL PRIMARY KEY,
                       uuid UUID NOT NULL UNIQUE DEFAULT gen_random_uuid(),
                       titulo VARCHAR(200) NOT NULL,
                       autor VARCHAR(100) NOT NULL,
                       ano_publicacao INTEGER,
                       disponivel BOOLEAN DEFAULT true,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE livro_categoria (
                                 id_livro BIGINT NOT NULL,
                                 id_categoria BIGINT NOT NULL,
                                 PRIMARY KEY (id_livro, id_categoria),
                                 FOREIGN KEY (id_livro) REFERENCES livro(id_livro) ON DELETE CASCADE,
                                 FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria) ON DELETE CASCADE
);

CREATE TABLE emprestimo (
                            id_emprestimo BIGSERIAL NOT NULL PRIMARY KEY,
                            uuid UUID NOT NULL UNIQUE DEFAULT gen_random_uuid(),
                            data_emprestimo TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            data_devolucao TIMESTAMP NULL,
                            data_devolucao_prevista TIMESTAMP NOT NULL,
                            id_cliente BIGINT NOT NULL,
                            id_livro BIGINT NOT NULL,
                            id_funcionario BIGINT NOT NULL,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente),
                            FOREIGN KEY (id_livro) REFERENCES livro(id_livro),
                            FOREIGN KEY (id_funcionario) REFERENCES funcionario(id_funcionario)
);

CREATE INDEX idx_cliente_cpf ON cliente(cpf);
CREATE INDEX idx_cliente_uuid ON cliente(uuid);
CREATE INDEX idx_funcionario_email ON funcionario(email);
CREATE INDEX idx_funcionario_uuid ON funcionario(uuid);
CREATE INDEX idx_funcionario_ativo ON funcionario(ativo);
CREATE INDEX idx_categoria_nome ON categoria(nome);
CREATE INDEX idx_categoria_uuid ON categoria(uuid);
CREATE INDEX idx_livro_titulo ON livro(titulo);
CREATE INDEX idx_livro_autor ON livro(autor);
CREATE INDEX idx_livro_disponivel ON livro(disponivel);
CREATE INDEX idx_livro_uuid ON livro(uuid);
CREATE INDEX idx_emprestimo_cliente ON emprestimo(id_cliente);
CREATE INDEX idx_emprestimo_livro ON emprestimo(id_livro);
CREATE INDEX idx_emprestimo_funcionario ON emprestimo(id_funcionario);
CREATE INDEX idx_emprestimo_data_devolucao ON emprestimo(data_devolucao);
CREATE INDEX idx_emprestimo_data_prevista ON emprestimo(data_devolucao_prevista);
CREATE INDEX idx_emprestimo_uuid ON emprestimo(uuid);

CREATE OR REPLACE FUNCTION update_updated_at_column()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_cliente_updated_at
    BEFORE UPDATE ON cliente
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_funcionario_updated_at
    BEFORE UPDATE ON funcionario
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_categoria_updated_at
    BEFORE UPDATE ON categoria
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_livro_updated_at
    BEFORE UPDATE ON livro
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_emprestimo_updated_at
    BEFORE UPDATE ON emprestimo
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

INSERT INTO funcionario (nome, email, senha, ativo) VALUES
                                                        ('Administrador', 'admin@biblioteca.com', 'admin123', true),
                                                        ('Juliano Rocha', 't@1', '1', true);

INSERT INTO cliente (nome, cpf, telefone, endereco) VALUES
                                                        ('João Silva', '12345678901', '(11) 99999-9999', 'Rua A, 123 - Centro - São Paulo/SP'),
                                                        ('Maria Santos', '98765432100', '(11) 88888-8888', 'Av. B, 456 - Jardim - Rio de Janeiro/RJ');

INSERT INTO categoria (nome, descricao) VALUES
                                            ('Romance', 'Livros de narrativas ficcionais sobre relacionamentos'),
                                            ('Ficção Científica', 'Obras que exploram conceitos científicos futuristas'),
                                            ('Terror', 'Livros que provocam medo e suspense'),
                                            ('Fantasia', 'Mundo imaginário com elementos mágicos'),
                                            ('Biografia', 'Relatos da vida de pessoas reais');

INSERT INTO livro (titulo, autor, ano_publicacao, disponivel) VALUES
                                                                  ('Dom Casmurro', 'Machado de Assis', 1899, true),
                                                                  ('1984', 'George Orwell', 1949, true),
                                                                  ('O Hobbit', 'J.R.R. Tolkien', 1937, true),
                                                                  ('It: A Coisa', 'Stephen King', 1986, true);

INSERT INTO livro_categoria (id_livro, id_categoria) VALUES (1, 1), (2, 2), (3, 4), (4, 3);