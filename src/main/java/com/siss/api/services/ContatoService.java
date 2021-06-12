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
import com.siss.api.entities.PessoaFisica;
import com.siss.api.exceptions.ConsistenciaException;
import com.siss.api.repositories.ContatoRepository;
import com.siss.api.security.services.JwtUserDetailsService;

@Service
public class ContatoService {
	private static final Logger log = LoggerFactory.getLogger(ContatoService.class);

	@Autowired
	private ContatoRepository contatoRepository;

	@Autowired
	private PessoaFisicaService pessoaFisicaService;

	@Autowired
	private JwtUserDetailsService userDetailsService;

	public Optional<Contato> buscarPorId(int id) throws ConsistenciaException {
		log.info("Service: buscando o contato de id: {}", id);
		Optional<Contato> contato = contatoRepository.findById(id);

		if (!contato.isPresent()) {
			log.info("Service: Nenhum contato com id: {} foi encontrado", id);
			throw new ConsistenciaException("Nenhum contato com id: {} foi encontrado", id);
		}

		if (contato.get() != null && contato.get().getPessoaFisica() != null
				&& contato.get().getPessoaFisica().getUsuario() != null) {
			userDetailsService.checkUser(contato.get().getPessoaFisica().getUsuario());
		}
		return contato;
	}

	public Optional<List<Contato>> buscarPorPessoaFisicaId(int pessoaFisicaId) throws ConsistenciaException {
		log.info("Service: buscando os contatos do usuario de id: {}", pessoaFisicaId);

		Optional<List<Contato>> contatos = Optional.ofNullable(contatoRepository.findByPessoaFisicaId(pessoaFisicaId));
		if (!contatos.isPresent() || contatos.get().size() < 1) {
			log.info("Service: Nenhum contato encontrado para o usuario de id: {}", pessoaFisicaId);
			throw new ConsistenciaException("Nenhum contato encontrado para o usuario de id: {}", pessoaFisicaId);
		}

		if (contatos.get().get(0).getPessoaFisica() != null
				&& contatos.get().get(0).getPessoaFisica().getUsuario() != null) {
			userDetailsService.checkUser(contatos.get().get(0).getPessoaFisica().getUsuario());
		}
		return contatos;
	}

	public Contato salvar(Contato contato) throws ConsistenciaException {
		log.info("Service: salvando o contato: {}", contato.toString());
		int pfId = contato.getPessoaFisica().getId();

		if (contato.getId() > 0) {
			Contato contatoExistente = buscarPorId(contato.getId()).get();
			contato.setDataCadastro(contatoExistente.getDataCadastro());
		}

		try {
			Optional<PessoaFisica> pf = pessoaFisicaService.buscarPorId(pfId);
			if (!pf.isPresent()) {
				throw new ConsistenciaException("Nenhuma PF com id: {} encontrado!", pfId);
			}

			if (pf.get() != null && pf.get().getUsuario() != null) {
				userDetailsService.checkUser(pf.get().getUsuario());
			}
			return contatoRepository.save(contato);
		} catch (DataIntegrityViolationException e) {
			log.info("Service: Inconsistência de dados.");
			throw new ConsistenciaException("Inconsistência de dados");
		}
	}

	public void excluirPorId(int id) throws ConsistenciaException {
		log.info("Service: excluíndo o contato de id: {}", id);
		Optional<Contato> contato = buscarPorId(id);

		if (contato.get().getPessoaFisica() != null && contato.get().getPessoaFisica().getUsuario() != null) {
			userDetailsService.checkUser(contato.get().getPessoaFisica().getUsuario());
		}
		contatoRepository.deleteById(id);
	}
}