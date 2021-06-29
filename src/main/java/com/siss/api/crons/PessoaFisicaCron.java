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

	/*
	 * Às 10:00 PM, no dia 1 do mês, a cada 6 meses
	 * 
	 * Testar em: https://bradymholt.github.io/cron-expression-descriptor/
	**/
	@Scheduled(cron = "0 0 16 1 1/6 *", zone = TIME_ZONE)
	public void enviarEmailAtualizacao() {
		List<PessoaFisica> pfList = pessoaFisicaRepository.findAll();

		if (pfList != null && pfList.size() > 0) {
			for (PessoaFisica pf : pfList) {
				emailUtils.enviar(
					pf.getUsuario().getEmail(),
					"SIS - Atualização de Dados",
					"Olá, lembre-se de manter seus dados cadastrados no SIS sempre atualizados. :)"
				);
			}
		}
	}
}
