package com.siss.api.services;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import com.siss.api.entities.Contato;
import com.siss.api.repositories.ContatoRepository;

@Service
public class ContatoService {
	private static final Logger log = LoggerFactory.getLogger(ContatoService.class);
	
	@Autowired
	private ContatoRepository contatoRepository;

}