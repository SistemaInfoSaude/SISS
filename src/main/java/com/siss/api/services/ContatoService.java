package com.siss.api.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import com.siss.api.entities.Contato;
import com.siss.api.exceptions.ConsistenciaException;
import com.siss.api.repositories.ContatoRepository;

@Service
public class ContatoService {
	private static final Logger log = LoggerFactory.getLogger(ContatoService.class);

	@Autowired
	private ContatoRepository contatoRepository;

	@Autowired
	private UsuarioService usuarioService;

	public Optional<Contato> buscarPorId(int id) throws ConsistenciaException {
		log.info("Service: buscando o contato de id: {}", id);
		Optional<Contato> contato = contatoRepository.findById(id);

		if (!contato.isPresent()) {
			log.info("Service: Nenhum contato com id: {} foi encontrado", id);
			throw new ConsistenciaException("Nenhum contato com id: {} foi encontrado", id);
		}
		return contato;
	}

	public Optional<List<Contato>> buscarPorUsuarioId(int usuarioId) throws ConsistenciaException {
		log.info("Service: buscando os contatos do usuario de id: {}", usuarioId);
		
		Optional<List<Contato>> contatos = Optional.ofNullable(contatoRepository.findByUsuarioId(usuarioId));
		if (!contatos.isPresent() || contatos.get().size() < 1) {
			log.info("Service: Nenhum contato encontrado para o usuario de id: {}", usuarioId);
			throw new ConsistenciaException("Nenhum contato encontrado para o usuario de id: {}", usuarioId);
		}
		return contatos;
	}

	public Contato salvar(Contato contato) throws ConsistenciaException {
		log.info("Service: salvando o contato: {}", contato.toString());
		int usuarioId = contato.getUsuario().getId();

		if (contato.getId() > 0) {
			buscarPorId(contato.getId());
		} else {
			contato.setDataCadastro(new Date());
		}
		contato.setDataAlteracao(new Date());

		try {
			if (!usuarioService.buscarPorId(usuarioId).isPresent()) {
				throw new ConsistenciaException("Nenhum usuario com id: {} encontrado!", usuarioId);
			}
			return contatoRepository.save(contato);
		} catch (DataIntegrityViolationException e) {
			log.info("Service: Inconsistência de dados.");
			throw new ConsistenciaException("Inconsistência de dados");
		}
	}
}