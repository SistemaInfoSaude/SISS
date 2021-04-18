package com.siss.api.services;

import java.util.Date;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import com.siss.api.entities.CondicaoClinica;
import com.siss.api.entities.Veiculo;
import com.siss.api.exceptions.ConsistenciaException;
import com.siss.api.repositories.CondicaoClinicaRepository;

@Service
public class CondicaoClinicaService {
	private static final Logger log = LoggerFactory.getLogger(CondicaoClinicaService.class);

	@Autowired
	private CondicaoClinicaRepository condicaoClinicaRepository;

	@Autowired
	private UsuarioService usuarioService;

	public Optional<CondicaoClinica> buscarPorId(int id) throws ConsistenciaException {
		log.info("Service: buscando a condicaoClinica com o id: {}", id);
		Optional<CondicaoClinica> condicaoClinica = condicaoClinicaRepository.findById(id);

		if (!condicaoClinica.isPresent()) {
			log.info("Service: Nenhuma condicaoClinica com id: {} foi encontrada", id);
			throw new ConsistenciaException("Nenhuma condicaoClinica com id: {} foi encontrada", id);
		}
		return condicaoClinica;
	}
	
	public CondicaoClinica salvar(CondicaoClinica condicaoClinica) throws ConsistenciaException {
		log.info("Service: salvando a condicaoClinica: {}", condicaoClinica.toString());
		int usuarioId = condicaoClinica.getUsuario().getId();

		if (condicaoClinica.getId() > 0) {
			buscarPorId(condicaoClinica.getId());
		}

		try {
			if (!usuarioService.buscarPorId(usuarioId).isPresent()) {
				throw new ConsistenciaException("Nenhum usuario com id: {} encontrado!", usuarioId);
			}
			return condicaoClinicaRepository.save(condicaoClinica);
		} catch (DataIntegrityViolationException e) {
			log.info("Service: Inconsistência de dados.");
			throw new ConsistenciaException("Inconsistência de dados");
		}
	}
}