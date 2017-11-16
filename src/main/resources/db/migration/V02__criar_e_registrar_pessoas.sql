CREATE TABLE pessoa (
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(30) NOT NULL,
	logradouro VARCHAR(30),
	numero VARCHAR(4),
	complemento VARCHAR(30),
	bairro VARCHAR(20),
	cep VARCHAR(8),
	cidade VARCHAR(20),
	estado VARCHAR(2),
	ativo BOOLEAN NOT NULL   
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) values ('João Silva', 'Rua do Abacaxi', '10', null, 'Brasil', '01454612', 'Uberlândia', 'MG', true);

INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) values ('João Roberto', 'Rua do Limão', '10', 'Próximo a Ponte', 'Jardim Ângela', '01308949', 'São Paulo', 'SP', true);

INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) values ('Rogério', 'Rua das Acácias', '30', 'Próximo a Escola', 'Jardim Aeroporto', '19434190', 'Assis', 'SP', false);

INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) values ('Paulo Angelino', 'Rua São Bento', '1433', null, 'Centro', '01984193', 'Bauru', 'SP', false);

INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) values ('Maria Paula', 'Rua Marília', '1432', null, 'Centro', '01353013', 'Araraquara', 'SP', true);

INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) values ('João Leão', 'Rua Garça', '14', null, 'Centro', '01307967', 'Araraquara', 'SP', true);

INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) values ('Marcia Regina', 'Rua São Paulo', '1444', null, 'Centro', '01302000', 'São Paulo', 'SP', true);

INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) values ('Carlos Rogério', 'Rua Luther King', '323', null, 'Centro', '01353013', 'São Paulo', 'SP', true);

INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) values ('Almir Sater', 'Avenida Getulio Vargas', '1999', null, 'Centro', '01353018', 'São Paulo', 'SP', true);
 
INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) values ('Zeca Balero', 'Avenida Mario de Vito', '1999', null, 'Centro', '01353097', 'São Paulo', 'SP', true);
