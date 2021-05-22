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
import com.siss.api.entities.Doenca;
import com.siss.api.entities.Veiculo;
import com.siss.api.exceptions.ConsistenciaException;
import com.siss.api.repositories.DoencaRepository;
import com.siss.api.repositories.VeiculoRepository;
import com.siss.api.security.services.JwtUserDetailsService;

@Service
public class DoencaService {
	private static final Logger log = LoggerFactory.getLogger(DoencaService.class);

	@Autowired
	private DoencaRepository doencaRepository;

	@Autowired
	private CondicaoClinicaService condicaoClinicaService;

	@Autowired
	private JwtUserDetailsService userDetailsService;

	public Optional<Doenca> buscarPorId(int id) throws ConsistenciaException {
		log.info("Service: buscando a doenca com o id: {}", id);
		Optional<Doenca> doenca = doencaRepository.findById(id);

		if (!doenca.isPresent()) {
			log.info("Service: Nenhuma doenca com id: {} foi encontrado", id);
			throw new ConsistenciaException("Nenhuma doenca com id: {} foi encontrado", id);
		}
		userDetailsService.checkUser(doenca.get().getCondicaoClinica().getPessoaFisica().getUsuario());
		return doenca;
	}

	public Optional<List<Doenca>> buscarPorCondicaoClinicaId(int condicaoClinicaId) throws ConsistenciaException {
		log.info("Service: buscando as doencoas da condicao clinica de id: {}", condicaoClinicaId);

		Optional<List<Doenca>> doencas = Optional
				.ofNullable(doencaRepository.findByCondicaoClinicaId(condicaoClinicaId));
		
		if (!doencas.isPresent() || doencas.get().size() < 1) {
			log.info("Service: Nenhuma doenca encontrada para a condicao clinica de id: {}", condicaoClinicaId);
			throw new ConsistenciaException("Nenhuma doenca encontrada para a condicao clinica de id: {}", condicaoClinicaId);
		}
		userDetailsService.checkUser(doencas.get().get(0).getCondicaoClinica().getPessoaFisica().getUsuario());
		return doencas;
	}

	public Doenca salvar(Doenca doenca) throws ConsistenciaException {
		log.info("Service: salvando o veiculo: {}", doenca.toString());
		int condicaoClinicaId = doenca.getCondicaoClinica().getId();

		if (doenca.getId() > 0) {
			Doenca doencaExistente = buscarPorId(doenca.getId()).get();
			doenca.setDataCadastro(doencaExistente.getDataCadastro());
		}

		try {
			Optional<CondicaoClinica> condicaoClinica = condicaoClinicaService.buscarPorId(condicaoClinicaId);
			if (!condicaoClinica.isPresent()) {
				throw new ConsistenciaException("Nenhuma condicao clinica com id: {} encontrada!", condicaoClinicaId);
			}
			userDetailsService.checkUser(condicaoClinica.get().getPessoaFisica().getUsuario());
			return doencaRepository.save(doenca);
		} catch (DataIntegrityViolationException e) {
			log.info("Service: Inconsistência de dados.");
			throw new ConsistenciaException("Inconsistência de dados");
		}
	}
	
	public void excluirPorId(int id) throws ConsistenciaException {
		log.info("Service: excluíndo a doenca de id: {}", id);
		Optional<Doenca> doenca = buscarPorId(id);
		userDetailsService.checkUser(doenca.get().getCondicaoClinica().getPessoaFisica().getUsuario());
		doencaRepository.deleteById(id);
	}
}