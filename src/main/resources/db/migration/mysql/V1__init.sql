CREATE TABLE IF NOT EXISTS `Usuario` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `usuario` VARCHAR(100) NOT NULL,
  `senha` VARCHAR(255) NOT NULL,
  `data_Cadastro` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `data_Alteracao` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `Convenio_Medico` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `Pessoa_Fisica` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `data_Nascimento` DATE NOT NULL,
  `cpf` VARCHAR(11) NOT NULL,
  `celular` VARCHAR(11) NULL,
  `telefone` VARCHAR(10) NULL,
  `convenio_medico_id` INT NOT NULL,
  `usuario_id` INT NOT NULL,
  UNIQUE INDEX `cpf_UNIQUE` (`cpf` ASC) VISIBLE,
  PRIMARY KEY (`id`),
  INDEX `fk_Pessoa_Fisica_Convenio_Medico_idx` (`convenio_medico_id` ASC) VISIBLE,
  INDEX `fk_Pessoa_Fisica_Usuario_idx` (`usuario_id` ASC) VISIBLE,
  CONSTRAINT `fk_Pessoa_Fisica_Convenio_Medico`
    FOREIGN KEY (`convenio_medico_id`)
    REFERENCES `Convenio_Medico` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Pessoa_Fisica_Usuario`
    FOREIGN KEY (`usuario_id`)
    REFERENCES `Usuario` (`id`)
    ON DELETE NO ACTION
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
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `Contato` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(100) NOT NULL,
  `telefone` VARCHAR(10) NULL,
  `celular` VARCHAR(11) NULL,
  `data_Cadastro` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `data_Alteracao` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Contatos_Usuario_idx` (`usuario_id` ASC) VISIBLE,
  CONSTRAINT `fk_Contatos_Usuario`
    FOREIGN KEY (`usuario_id`)
    REFERENCES `Usuario` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `Veiculo` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `marca` VARCHAR(45) NOT NULL,
  `modelo` VARCHAR(45) NOT NULL,
  `placa` VARCHAR(8) NOT NULL,
  `renavam` VARCHAR(11) NOT NULL,
  `informacoes_Adicionais` TEXT NULL,
  `data_Cadastro` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `data_Alteracao` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Veiculos_Usuario_idx` (`usuario_id` ASC) VISIBLE,
  CONSTRAINT `fk_Veiculos_Usuario`
    FOREIGN KEY (`usuario_id`)
    REFERENCES `Usuario` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `Tipo_Sanguineo` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `tipo` VARCHAR(3) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `Condicao_Clinica` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `informacao_Adicional` TEXT NULL,
  `usuario_id` INT NOT NULL,
  `tipo_sanguineo_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Condicao_Clinica_Usuario_idx` (`usuario_id` ASC) VISIBLE,
  INDEX `fk_Condicao_Clinica_Tipo_Sanguineo_idx` (`tipo_sanguineo_id` ASC) VISIBLE,
  CONSTRAINT `fk_Condicao_Clinica_Usuario`
    FOREIGN KEY (`usuario_id`)
    REFERENCES `Usuario` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Condicao_Clinica_Tipo_Sanguineo`
    FOREIGN KEY (`tipo_sanguineo_id`)
    REFERENCES `Tipo_Sanguineo` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `Doenca` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `tipo` VARCHAR(100) NULL,
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
  `tipo` VARCHAR(100) NULL,
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
 UNIQUE INDEX `nome_UNIQUE` (`nome` ASC))
ENGINE = InnoDB;


CREATE TABLE `Usuario_Regra` (
 `usuario_id` INT NOT NULL,
 `regra_id` INT NOT NULL,
 PRIMARY KEY (`usuario_id`, `regra_id`),
 INDEX `fk_Usuario_Regra_Regra_idx` (`regra_id` ASC),
 INDEX `fk_Usuario_Regra_Usuario_idx` (`usuario_id` ASC),
 CONSTRAINT `fk_Usuario_Regra_Usuario`
	FOREIGN KEY (`usuario_id`)
	REFERENCES `Usuario` (`id`)
	ON DELETE NO ACTION
	ON UPDATE NO ACTION,
 CONSTRAINT `fk_Usuario_Regra_Regra`
	FOREIGN KEY (`regra_id`)
	REFERENCES `Regra` (`id`)
	ON DELETE NO ACTION
	ON UPDATE NO ACTION)
ENGINE = InnoDB;

INSERT INTO `Usuario` (`id`, `usuario`, `senha`) VALUES
(DEFAULT, 'usuario_comum', '$2a$10$FHayM6spzm5LGUa//VKYKe9iWLPlSnYpdwGEkvHMlCEZUIsr4EEIG');
INSERT INTO `Usuario` (`id`, `usuario`, `senha`) VALUES
(DEFAULT, 'usuario_executante', '$2a$10$FHayM6spzm5LGUa//VKYKe9iWLPlSnYpdwGEkvHMlCEZUIsr4EEIG');

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

INSERT INTO `Convenio_Medico` (`id`, `nome`) VALUES
(DEFAULT, 'Unimed'),
(DEFAULT, 'Amil Assistência Médica Internacional'),
(DEFAULT, 'Bradesco Saúde'),
(DEFAULT, 'Medial Saúde'),
(DEFAULT, 'Amico Saúde'),
(DEFAULT, 'Golden Cross'),
(DEFAULT, 'Sul América Seguro Saúde'),
(DEFAULT, 'Hospitalar');

INSERT INTO `Tipo_Sanguineo` (`id`, `tipo`) VALUES
(DEFAULT, 'A+'),
(DEFAULT, 'A-'),
(DEFAULT, 'B+'),
(DEFAULT, 'B-'),
(DEFAULT, 'AB+'),
(DEFAULT, 'AB-'),
(DEFAULT, 'O+'),
(DEFAULT, 'O-');