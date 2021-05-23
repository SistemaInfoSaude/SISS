package com.siss.api.crons;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.siss.api.entities.PessoaFisica;
import com.siss.api.repositories.PessoaFisicaRepository;
import com.siss.api.utils.EmailUtils;

@Component
@EnableScheduling
public class PessoaFisicaCron {
	
	private static final Logger log = LoggerFactory.getLogger(PessoaFisicaCron.class);
	
	private static final String TIME_ZONE = "America/Sao_Paulo";

	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;

	@Autowired
	EmailUtils emailUtils;

	@Scheduled(cron = "0 0 0 6 * *", zone = TIME_ZONE)
	public void enviarEmailAtualizacao() {
		log.info("Cron: teste teste teste");
		
		List<PessoaFisica> pfList = pessoaFisicaRepository.findAll();
		
		log.info("Size: {}", pfList.size());

		if (pfList != null && pfList.size() > 0) {
			for (PessoaFisica pf : pfList) {
				emailUtils.enviar(
					pf.getUsuario().getEmail(),
					"SISS - Atualização de Dados",
					"Olá humano, poderia atualizar suas informações, por favor?"
				);
			}
		}
	}
}
