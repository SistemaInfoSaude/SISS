package com.siss.api.services;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import com.siss.api.entities.Alergia;
import com.siss.api.repositories.AlergiaRepository;

@Service
public class AlergiaService {
	private static final Logger log = LoggerFactory.getLogger(AlergiaService.class);

	@Autowired
	private AlergiaRepository alergiaRepository;
}