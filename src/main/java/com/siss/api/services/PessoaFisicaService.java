package com.siss.api.services;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import com.siss.api.entities.PessoaFisica;
import com.siss.api.repositories.PessoaFisicaRepository;

@Service
public class PessoaFisicaService {
	private static final Logger log = LoggerFactory.getLogger(PessoaFisicaService.class);

	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;
}