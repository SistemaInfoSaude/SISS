package com.siss.api.services;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.siss.api.entities.PessoaFisica;
import com.siss.api.entities.PessoaJuridica;
import com.siss.api.entities.Usuario;
import com.siss.api.exceptions.ConsistenciaException;
import com.siss.api.repositories.PessoaJuridicaRepository;

@Service
public class PessoaJuridicaService {
	private static final Logger log = LoggerFactory.getLogger(PessoaJuridicaService.class);

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private PessoaJuridicaRepository pessoaJuridicaRepository;
	
	public Optional<PessoaJuridica> buscarPorId(int id) throws ConsistenciaException {
		log.info("Service: buscando a pessoa juridica com o id: {}", id);
		Optional<PessoaJuridica> pessoaJuridica= pessoaJuridicaRepository.findById(id);

		if (!pessoaJuridica.isPresent()) {
			log.info("Service: Nenhuma pessoa juridica com id: {} foi encontrado", id);
			throw new ConsistenciaException("Nenhuma pessoa juridica com id: {} foi encontrado", id);
		}

		return pessoaJuridica;
	}
	
	public PessoaJuridica salvar(PessoaJuridica pessoaJuridica) throws ConsistenciaException {
		log.info("Service: salvando a pessoa juridica: {}", pessoaJuridica.toString());
		int usuarioId = pessoaJuridica.getUsuario().getId();

		if (pessoaJuridica.getId() > 0) {
			buscarPorId(pessoaJuridica.getId());
		}

		try {
			Optional<Usuario> usr = usuarioService.buscarPorId(usuarioId);
			if (!usr.isPresent()) {
				throw new ConsistenciaException("Nenhum usuario com id: {} encontrado!", usuarioId);
			}

			return pessoaJuridicaRepository.save(pessoaJuridica);
		} catch (DataIntegrityViolationException e) {
			throw new ConsistenciaException("Error: {}", e.getMessage());
		}
	}
}