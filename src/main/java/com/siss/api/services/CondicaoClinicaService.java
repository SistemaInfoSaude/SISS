package com.siss.api.services;

import java.util.Date;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import com.siss.api.entities.CondicaoClinica;
import com.siss.api.entities.PessoaFisica;
import com.siss.api.entities.Veiculo;
import com.siss.api.exceptions.ConsistenciaException;
import com.siss.api.repositories.CondicaoClinicaRepository;
import com.siss.api.security.services.JwtUserDetailsService;

@Service
public class CondicaoClinicaService {
	private static final Logger log = LoggerFactory.getLogger(CondicaoClinicaService.class);

	@Autowired
	private CondicaoClinicaRepository condicaoClinicaRepository;

	@Autowired
	private PessoaFisicaService pessoaFisicaService;

	@Autowired
	private JwtUserDetailsService userDetailsService;
	
	public Optional<CondicaoClinica> buscarPorId(int id) throws ConsistenciaException {
		log.info("Service: buscando a condicaoClinica com o id: {}", id);
		Optional<CondicaoClinica> condicaoClinica = condicaoClinicaRepository.findById(id);

		if (!condicaoClinica.isPresent()) {
			log.info("Service: Nenhuma condicaoClinica com id: {} foi encontrada", id);
			throw new ConsistenciaException("Nenhuma condicaoClinica com id: {} foi encontrada", id);
		}
		
		if(condicaoClinica.get().getPessoaFisica() != null && condicaoClinica.get().getPessoaFisica().getUsuario() != null) {
			userDetailsService.checkUser(condicaoClinica.get().getPessoaFisica().getUsuario());
		}
		return condicaoClinica;
	}
	
	public CondicaoClinica salvar(CondicaoClinica condicaoClinica) throws ConsistenciaException {
		log.info("Service: salvando a condicaoClinica: {}", condicaoClinica.toString());
		int pfId = condicaoClinica.getPessoaFisica().getId();

		if (condicaoClinica.getId() > 0) {
			buscarPorId(condicaoClinica.getId());
		}

		try {
			Optional<PessoaFisica> pf = pessoaFisicaService.buscarPorId(pfId);
			if (!pf.isPresent()) {
				throw new ConsistenciaException("Nenhuma PF com id: {} encontrado!", pfId);
			}
			
			if(pf.get() != null && pf.get().getUsuario() != null) {
				userDetailsService.checkUser(pf.get().getUsuario());
			}
			return condicaoClinicaRepository.save(condicaoClinica);
		} catch (DataIntegrityViolationException e) {
			log.info("Service: Inconsistência de dados.");
			throw new ConsistenciaException("Inconsistência de dados");
		}
	}
}