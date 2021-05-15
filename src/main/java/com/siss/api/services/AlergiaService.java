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
import com.siss.api.exceptions.ConsistenciaException;
import com.siss.api.repositories.AlergiaRepository;

@Service
public class AlergiaService {
	private static final Logger log = LoggerFactory.getLogger(AlergiaService.class);

	@Autowired
	private AlergiaRepository alergiaRepository;

	@Autowired
	private CondicaoClinicaService condicaoClinicaService;

	public Optional<Alergia> buscarPorId(int id) throws ConsistenciaException {
		log.info("Service: buscando a alergia com o id: {}", id);
		Optional<Alergia> alergia = alergiaRepository.findById(id);

		if (!alergia.isPresent()) {
			log.info("Service: Nenhuma alergia com id: {} foi encontrado", id);
			throw new ConsistenciaException("Nenhuma alergia com id: {} foi encontrado", id);
		}
		return alergia;
	}

	public Optional<List<Alergia>> buscarPorCondicaoClinicaId(int condicaoClinicaId) throws ConsistenciaException {
		log.info("Service: buscando as alergias da condicao clinica de id: {}", condicaoClinicaId);

		Optional<List<Alergia>> alergias = Optional
				.ofNullable(alergiaRepository.findByCondicaoClinicaId(condicaoClinicaId));
		
		if (!alergias.isPresent() || alergias.get().size() < 1) {
			log.info("Service: Nenhuma alergia encontrada para a condicao clinica de id: {}", condicaoClinicaId);
			throw new ConsistenciaException("Nenhuma alergia encontrada para a condicao clinica de id: {}", condicaoClinicaId);
		}
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
			if (!condicaoClinicaService.buscarPorId(condicaoClinicaId).isPresent()) {
				throw new ConsistenciaException("Nenhuma condicao clinica com id: {} encontrada!", condicaoClinicaId);
			}
			return alergiaRepository.save(alergia);
		} catch (DataIntegrityViolationException e) {
			log.info("Service: Inconsistência de dados.");
			throw new ConsistenciaException("Inconsistência de dados");
		}
	}
}