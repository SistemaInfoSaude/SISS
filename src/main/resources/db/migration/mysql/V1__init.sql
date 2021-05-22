CREATE TABLE IF NOT EXISTS `Usuario` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `usuario` VARCHAR(100) NOT NULL,
  `email` VARCHAR(150) NOT NULL,
  `senha` VARCHAR(255) NOT NULL,
  `data_Cadastro` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `data_Alteracao` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX (`usuario` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `Pessoa_Fisica` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(255) NOT NULL,
  `data_Nascimento` DATE NOT NULL,
  `cpf` VARCHAR(11) NOT NULL,
  `rg` VARCHAR(9) NOT NULL,
  `celular` VARCHAR(11) NULL DEFAULT NULL,
  `telefone` VARCHAR(10) NULL DEFAULT NULL,
  `usuario_id` INT NOT NULL,
  UNIQUE INDEX `cpf_UNIQUE` (`cpf` ASC) VISIBLE,
  PRIMARY KEY (`id`),
  INDEX `fk_Pessoa_Fisica_Usuario_idx` (`usuario_id` ASC) VISIBLE,
  UNIQUE INDEX `rg_UNIQUE` (`rg` ASC) VISIBLE,
  CONSTRAINT `fk_Pessoa_Fisica_Usuario`
    FOREIGN KEY (`usuario_id`)
    REFERENCES `Usuario` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `Pessoa_Juridica` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `cnpj` VARCHAR(14) NOT NULL,
  `usuario_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Pessoa_Juridica_Usuario_idx` (`usuario_id` ASC) VISIBLE,
  CONSTRAINT `fk_Pessoa_Juridica_Usuario`
    FOREIGN KEY (`usuario_id`)
    REFERENCES `Usuario` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `Contato` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(100) NOT NULL,
  `parentesco` VARCHAR(50) NOT NULL,
  `telefone` VARCHAR(10) NULL DEFAULT NULL,
  `celular` VARCHAR(11) NULL DEFAULT NULL,
  `data_Cadastro` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `data_Alteracao` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `pessoa_fisica_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Contato_Pessoa_Fisica_idx` (`pessoa_fisica_id` ASC) VISIBLE,
  CONSTRAINT `fk_Contato_Pessoa_Fisica`
    FOREIGN KEY (`pessoa_fisica_id`)
    REFERENCES `Pessoa_Fisica` (`id`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE)
ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `Veiculo` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `marca` VARCHAR(45) NOT NULL,
  `modelo` VARCHAR(45) NOT NULL,
  `placa` VARCHAR(8) NOT NULL,
  `renavam` VARCHAR(11) NOT NULL,
  `informacoes_Adicionais` TEXT NULL DEFAULT NULL,
  `data_Cadastro` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `data_Alteracao` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `pessoa_fisica_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Veiculo_Pessoa_Fisica_idx` (`pessoa_fisica_id` ASC) VISIBLE,
  CONSTRAINT `fk_Veiculo_Pessoa_Fisica`
    FOREIGN KEY (`pessoa_fisica_id`)
    REFERENCES `Pessoa_Fisica` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `Condicao_Clinica` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `convenio_Medico` VARCHAR(255) NOT NULL,
  `tipo_Sanguineo` VARCHAR(3) NOT NULL,
  `pessoa_fisica_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Condicao_Clinica_Pessoa_Fisica_idx` (`pessoa_fisica_id` ASC) VISIBLE,
  CONSTRAINT `fk_Condicao_Clinica_Pessoa_Fisica`
    FOREIGN KEY (`pessoa_fisica_id`)
    REFERENCES `Pessoa_Fisica` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `Doenca` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `tipo` VARCHAR(100) NULL DEFAULT NULL,
  `data_Cadastro` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `data_Alteracao` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `condicao_clinica_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Doenca_Condicao_Clinica_idx` (`condicao_clinica_id` ASC) VISIBLE,
  CONSTRAINT `fk_Doenca_Condicao_Clinica`
    FOREIGN KEY (`condicao_clinica_id`)
    REFERENCES `Condicao_Clinica` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `Alergia` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `tipo` VARCHAR(100) NULL DEFAULT NULL,
  `data_Cadastro` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `data_Alteracao` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `condicao_clinica_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Alergia_Condicao_Clinica_idx` (`condicao_clinica_id` ASC) VISIBLE,
  CONSTRAINT `fk_Alergia_Condicao_Clinica`
    FOREIGN KEY (`condicao_clinica_id`)
    REFERENCES `Condicao_Clinica` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `Regra` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(50) NOT NULL,
  `descricao` VARCHAR(255) NOT NULL,
  `ativo` BIT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `nome_UNIQUE` (`nome` ASC) VISIBLE)
ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `Usuario_Regra` (
  `usuario_id` INT NOT NULL,
  `regra_id` INT NOT NULL,
  PRIMARY KEY (`usuario_id`, `regra_id`),
  INDEX `fk_Usuario_Regra_Regra_idx` (`regra_id` ASC) VISIBLE,
  INDEX `fk_Usuario_Regra_Usuario_idx` (`usuario_id` ASC) VISIBLE,
  CONSTRAINT `fk_Usuario_Regra_Usuario`
    FOREIGN KEY (`usuario_id`)
    REFERENCES `Usuario` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Usuario_Regra_Regra`
    FOREIGN KEY (`regra_id`)
    REFERENCES `Regra` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

INSERT INTO `Usuario` (`id`, `usuario`, `email`, `senha`) VALUES
(DEFAULT, 'usuario_comum', 'email_teste_1@email.com', '$2a$10$FHayM6spzm5LGUa//VKYKe9iWLPlSnYpdwGEkvHMlCEZUIsr4EEIG');
INSERT INTO `Usuario` (`id`, `usuario`, `email`, `senha`) VALUES
(DEFAULT, 'usuario_executante', 'email_teste_2@email.com', '$2a$10$FHayM6spzm5LGUa//VKYKe9iWLPlSnYpdwGEkvHMlCEZUIsr4EEIG');

INSERT INTO `Regra` (`id`, `nome`, `descricao`, `ativo`) VALUES
(DEFAULT, 'ROLE_EXEC_USUARIO', 'Permite acesso aos serviços de executante', TRUE);
INSERT INTO `Regra` (`id`, `nome`, `descricao`, `ativo`) VALUES
(DEFAULT, 'ROLE_USUARIO', 'Permite acesso aos serviços padrões', TRUE);

INSERT INTO `Usuario_Regra` (`usuario_id`, `regra_id`) VALUES (
(SELECT `id` FROM usuario WHERE usuario = 'usuario_comum'),
(SELECT `id` FROM regra WHERE nome = 'ROLE_USUARIO')
);
INSERT INTO `Usuario_Regra` (`usuario_id`, `regra_id`) VALUES (
(SELECT `id` FROM usuario WHERE usuario = 'usuario_executante'),
(SELECT `id` FROM regra WHERE nome = 'ROLE_EXEC_USUARIO')
);