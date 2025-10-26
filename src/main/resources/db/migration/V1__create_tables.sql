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
