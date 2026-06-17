-- MinhaCasaClandestinaTech | Código de criação do banco de dados. 
-- Banco e usuário usados pela aplicação
CREATE DATABASE IF NOT EXISTS poo
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'poo'@'localhost' IDENTIFIED BY 'melhormateria';
GRANT ALL PRIVILEGES ON poo.* TO 'poo'@'localhost';
FLUSH PRIVILEGES;

USE poo;

-- Tabelas
CREATE TABLE IF NOT EXISTS tb_responsavel (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    nome      VARCHAR(120) NOT NULL,
    endereco  VARCHAR(200),
    telefone  VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS tb_local (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    nome_casa       VARCHAR(120) NOT NULL,
    endereco        VARCHAR(200),
    compartimento   VARCHAR(120) NOT NULL,
    id_responsavel  INT NOT NULL,
    CONSTRAINT fk_local_resp FOREIGN KEY (id_responsavel) REFERENCES tb_responsavel(id)
);

CREATE TABLE IF NOT EXISTS tb_cliente (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    nome      VARCHAR(120) NOT NULL,
    endereco  VARCHAR(200),
    cpf       VARCHAR(14) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS tb_equipamento (
    numero_serie    INT PRIMARY KEY,
    nome            VARCHAR(120) NOT NULL,
    preco           DECIMAL(10,2) NOT NULL,
    qtde_estoque    INT NOT NULL DEFAULT 0,
    id_local        INT NOT NULL,
    id_responsavel  INT NOT NULL,
    CONSTRAINT fk_equip_local FOREIGN KEY (id_local) REFERENCES tb_local(id),
    CONSTRAINT fk_equip_resp  FOREIGN KEY (id_responsavel) REFERENCES tb_responsavel(id)
);

CREATE TABLE IF NOT EXISTS tb_funcionario (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nome        VARCHAR(120) NOT NULL,
    endereco    VARCHAR(200),
    login       VARCHAR(60) NOT NULL UNIQUE,
    senha_hash  CHAR(64) NOT NULL,           -- SHA-256 em hexadecimal
    perfil      VARCHAR(20) NOT NULL          -- ADMIN ou FUNCIONARIO
);

CREATE TABLE IF NOT EXISTS tb_compra (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    data_compra     DATE NOT NULL,
    id_responsavel  INT NOT NULL,
    CONSTRAINT fk_compra_resp FOREIGN KEY (id_responsavel) REFERENCES tb_responsavel(id)
);

CREATE TABLE IF NOT EXISTS tb_item_compra (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    id_compra           INT NOT NULL,
    numero_serie_equip  INT NOT NULL,
    quantidade          INT NOT NULL,
    preco_unitario      DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_itemc_compra FOREIGN KEY (id_compra) REFERENCES tb_compra(id)
);

CREATE TABLE IF NOT EXISTS tb_venda (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    data_venda  DATE NOT NULL,
    id_cliente  INT NOT NULL,
    CONSTRAINT fk_venda_cliente FOREIGN KEY (id_cliente) REFERENCES tb_cliente(id)
);

CREATE TABLE IF NOT EXISTS tb_item_venda (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    id_venda            INT NOT NULL,
    numero_serie_equip  INT NOT NULL,
    quantidade          INT NOT NULL,
    preco_unitario      DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_itemv_venda FOREIGN KEY (id_venda) REFERENCES tb_venda(id)
);

-- Dados

-- Usuario gerente padrao -> login: poo   senha: melhormateria
-- O hash é SHA-256("melhormateria") Ver classe AutenticacaoService para resgatar senhas hash.
INSERT INTO tb_funcionario (nome, endereco, login, senha_hash, perfil)
SELECT 'Administrador', 'Sede', 'poo',
       '35e46d4de53741ae5b75b947b5e2083170eceb9c7859e1e87316da4aeb58ab30', 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM tb_funcionario WHERE login = 'admin');

-- Responsaveis
INSERT INTO tb_responsavel (nome, endereco, telefone) VALUES
    ('Toinho', 'Rua das Flores, 10', '84999990000'),
    ('Kanalense', 'Rua do Porto, 22', '84988880000');

INSERT INTO tb_local (nome_casa, endereco, compartimento, id_responsavel) VALUES
    ('Casa do Toinho', 'Rua das Flores, 10', 'Garagem/Deposito', 1),
    ('Casa do Kanalense', 'Rua do Porto, 22', 'Garagem', 2);

-- Equipamentos
INSERT INTO tb_equipamento (numero_serie, nome, preco, qtde_estoque, id_local, id_responsavel) VALUES
    (1001, 'Mouse Gamer', 89.90, 15, 1, 1),
    (1002, 'Teclado Mecanico', 249.90, 4, 1, 1),
    (1003, 'Monitor 24 polegadas', 899.00, 10, 2, 2)
    (1004, 'MousePad RGB', 49.90, 50, 2, 2);

-- Cliente 
INSERT INTO tb_cliente (nome, endereco, cpf) VALUES
    ('Maria das Gracas', 'Av. Central, 100', '11122233344')
    ('João da Escóssia', 'Av. João da Escóssia, 3373', '55566677788');
