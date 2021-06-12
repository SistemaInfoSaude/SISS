package com.siss.api.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.siss.api.entities.Alergia;
import com.siss.api.entities.CondicaoClinica;
import com.siss.api.exceptions.ConsistenciaException;
import com.siss.api.repositories.AlergiaRepository;
import com.siss.api.security.services.JwtUserDetailsService;

@Service
public class AlergiaService {
	private static final Logger log = LoggerFactory.getLogger(AlergiaService.class);

	@Autowired
	private AlergiaRepository alergiaRepository;

	@Autowired
	private CondicaoClinicaService condicaoClinicaService;

	@Autowired
	private JwtUserDetailsService userDetailsService;

	public Optional<Alergia> buscarPorId(int id) throws ConsistenciaException {
		log.info("Service: buscando a alergia com o id: {}", id);
		Optional<Alergia> alergia = alergiaRepository.findById(id);

		if (!alergia.isPresent()) {
			log.info("Service: Nenhuma alergia com id: {} foi encontrado", id);
			throw new ConsistenciaException("Nenhuma alergia com id: {} foi encontrado", id);
		}
		userDetailsService.checkUser(alergia.get().getCondicaoClinica().getPessoaFisica().getUsuario());
		return alergia;
	}

	public Optional<List<Alergia>> buscarPorCondicaoClinicaId(int condicaoClinicaId) throws ConsistenciaException {
		log.info("Service: buscando as alergias da condicao clinica de id: {}", condicaoClinicaId);

		Optional<List<Alergia>> alergias = Optional
				.ofNullable(alergiaRepository.findByCondicaoClinicaId(condicaoClinicaId));

		if (!alergias.isPresent() || alergias.get().size() < 1) {
			log.info("Service: Nenhuma alergia encontrada para a condicao clinica de id: {}", condicaoClinicaId);
			throw new ConsistenciaException("Nenhuma alergia encontrada para a condicao clinica de id: {}",
					condicaoClinicaId);
		}
		userDetailsService.checkUser(alergias.get().get(0).getCondicaoClinica().getPessoaFisica().getUsuario());
		return alergias;
	}

	public Alergia salvar(Alergia alergia) throws ConsistenciaException {
		log.info("Service: salvando a alergia: {}", alergia.toString());
		int condicaoClinicaId = alergia.getCondicaoClinica().getId();

		if (alergia.getId() > 0) {
			Alergia alergiaExistente = buscarPorId(alergia.getId()).get();
			alergia.setDataCadastro(alergiaExistente.getDataCadastro());
		}

		try {
			Optional<CondicaoClinica> condicaoClinica = condicaoClinicaService.buscarPorId(condicaoClinicaId);
			if (!condicaoClinica.isPresent()) {
				throw new ConsistenciaException("Nenhuma condicao clinica com id: {} encontrada!", condicaoClinicaId);
			}
			
			for (Alergia alergiaBanco : condicaoClinica.get().getAlergias()) {
				if(alergia.getTipo().equals(alergiaBanco.getTipo())) {
					throw new ConsistenciaException("Alergia '{}' já cadastrada anteriormente.", alergia.getTipo());
				}
			}
			
			userDetailsService.checkUser(condicaoClinica.get().getPessoaFisica().getUsuario());
			return alergiaRepository.save(alergia);
		} catch (DataIntegrityViolationException e) {
			log.info("Service: Inconsistência de dados.");
			throw new ConsistenciaException("Inconsistência de dados");
		}
	}

	public void excluirPorId(int id) throws ConsistenciaException {
		log.info("Service: excluíndo a alergia de id: {}", id);
		Optional<Alergia> alergia = buscarPorId(id);
		userDetailsService.checkUser(alergia.get().getCondicaoClinica().getPessoaFisica().getUsuario());
		alergiaRepository.deleteById(id);
	}
}