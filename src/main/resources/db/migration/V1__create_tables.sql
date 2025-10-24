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

CREATE TABLE categoria (
                           id_categoria BIGSERIAL NOT NULL PRIMARY KEY,
                           uuid UUID NOT NULL UNIQUE DEFAULT gen_random_uuid(),
                           nome VARCHAR(50) NOT NULL UNIQUE,
                           descricao TEXT,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE livro_categoria (
                                 id_livro BIGINT NOT NULL REFERENCES livro(id_livro) ON DELETE CASCADE,
                                 id_categoria BIGINT NOT NULL REFERENCES categoria(id_categoria) ON DELETE CASCADE,
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 PRIMARY KEY (id_livro, id_categoria)
);

CREATE TABLE emprestimo (
                            id_emprestimo BIGSERIAL NOT NULL PRIMARY KEY,
                            uuid UUID NOT NULL UNIQUE DEFAULT gen_random_uuid(),
                            data_emprestimo TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            data_devolucao TIMESTAMP,
                            data_devolucao_prevista TIMESTAMP NOT NULL DEFAULT (CURRENT_TIMESTAMP + INTERVAL '14 days'),
                            status VARCHAR(20) DEFAULT 'ATIVO' CHECK (status IN ('ATIVO', 'FINALIZADO', 'ATRASADO')),
                            id_cliente BIGINT NOT NULL REFERENCES cliente(id_cliente),
                            id_livro BIGINT NOT NULL REFERENCES livro(id_livro),
                            id_funcionario BIGINT NOT NULL REFERENCES funcionario(id_funcionario),
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
