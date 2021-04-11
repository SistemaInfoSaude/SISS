package com.siss.api.services;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import com.siss.api.entities.PessoaFisica;
import com.siss.api.exceptions.ConsistenciaException;
import com.siss.api.repositories.PessoaFisicaRepository;
import com.siss.api.services.UsuarioService;

@Service
public class PessoaFisicaService {
	private static final Logger log = LoggerFactory.getLogger(PessoaFisicaService.class);

	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;

	@Autowired
	private UsuarioService usuarioService;

	public Optional<PessoaFisica> buscarPorId(int id) throws ConsistenciaException {
		log.info("Service: buscando a pessoa fisica com o id: {}", id);
		Optional<PessoaFisica> pessoaFisica = pessoaFisicaRepository.findById(id);

		if (!pessoaFisica.isPresent()) {
			log.info("Service: Nenhuma pessoa fisica com id: {} foi encontrado", id);
			throw new ConsistenciaException("Nenhum pessoa fisica com id: {} foi encontrado", id);
		}
		return pessoaFisica;
	}

	public Optional<PessoaFisica> buscarPorCpf(String cpf) throws ConsistenciaException {
		log.info("Service: buscando a pessoa fisica com o cpf: {}", cpf);
		Optional<PessoaFisica> pessoaFisica = pessoaFisicaRepository.findByCpf(cpf);

		if (!pessoaFisica.isPresent()) {
			log.info("Service: Nenhuma pessoa fisica com cpf: {} foi encontrado", cpf);
			throw new ConsistenciaException("Nenhum pessoa fisica com cpf: {} foi encontrado", cpf);
		}
		return pessoaFisica;
	}

	public Optional<PessoaFisica> buscarPorUsuarioId(int usuarioId) throws ConsistenciaException {
		log.info("Service: buscando a pessoa fisica com o usuarioId: {}", usuarioId);
		Optional<PessoaFisica> pessoaFisica = pessoaFisicaRepository.findByUsuarioId(usuarioId);

		if (!pessoaFisica.isPresent()) {
			log.info("Service: Nenhuma pessoa fisica com usuarioId: {} foi encontrado", usuarioId);
			throw new ConsistenciaException("Nenhum pessoa fisica com usuarioId: {} foi encontrado", usuarioId);
		}
		return pessoaFisica;
	}

	public PessoaFisica salvar(PessoaFisica pessoaFisica) throws ConsistenciaException {
		log.info("Service: salvando a pessoa fisica: {}", pessoaFisica.toString());
		int usuarioId = pessoaFisica.getUsuario().getId();
		
		if (pessoaFisica.getId() > 0) {
			buscarPorId(pessoaFisica.getId());
		}else {
			Optional<PessoaFisica> pfExistente = pessoaFisicaRepository.findByUsuarioId(usuarioId);
			if (pfExistente.isPresent()) {
				throw new ConsistenciaException("Já existe uma PF cadastrada para o usuario de ID: {}", usuarioId);
			}
		}

		try {
			if (!usuarioService.buscarPorId(usuarioId).isPresent()) {
				throw new ConsistenciaException("Nenhum usuario com id: {} encontrado!", usuarioId);
			}
			return pessoaFisicaRepository.save(pessoaFisica);
		} catch (DataIntegrityViolationException e) {
			log.info("Service: O cpf: {} já está cadastrado para outra pessoa fisica", pessoaFisica.getCpf());
			throw new ConsistenciaException("O cpf: {} já está cadastrado para outra pessoa fisica",
					pessoaFisica.getCpf());
		}
	}

}