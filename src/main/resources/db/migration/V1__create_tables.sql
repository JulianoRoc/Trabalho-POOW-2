CREATE TABLE cliente (
                         id_cliente BIGSERIAL NOT NULL PRIMARY KEY,
                         nome VARCHAR(100) NOT NULL,
                         cpf VARCHAR(11) NOT NULL UNIQUE,
                         telefone VARCHAR(15),
                         endereco TEXT
);

CREATE TABLE funcionario (
                             id_funcionario BIGSERIAL NOT NULL PRIMARY KEY,
                             nome VARCHAR(100) NOT NULL,
                             email VARCHAR(100) NOT NULL UNIQUE,
                             senha VARCHAR(255) NOT NULL,
                             ativo BOOLEAN DEFAULT true
);

CREATE TABLE livro (
                       id_livro BIGSERIAL NOT NULL PRIMARY KEY,
                       titulo VARCHAR(200) NOT NULL,
                       autor VARCHAR(100) NOT NULL,
                       ano_publicacao INTEGER,
                       disponivel BOOLEAN DEFAULT true
);

CREATE TABLE emprestimo (
                            id_emprestimo BIGSERIAL NOT NULL PRIMARY KEY,
                            data_emprestimo TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            data_devolucao TIMESTAMP,
                            data_devolucao_prevista TIMESTAMP NOT NULL,
                            id_cliente BIGINT NOT NULL REFERENCES cliente(id_cliente),
                            id_livro BIGINT NOT NULL REFERENCES livro(id_livro),
                            id_funcionario BIGINT NOT NULL REFERENCES funcionario(id_funcionario)
);