package com.siss.api.crons;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.siss.api.entities.PessoaFisica;
import com.siss.api.repositories.PessoaFisicaRepository;
import com.siss.api.utils.EmailUtils;

@EnableScheduling
public class PessoaFisicaCron {

	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;
	
	@Autowired
	private EmailUtils emailUtils;

	@Scheduled(cron = "0 0 1 */6 *")
	public void enviarEmailAtualizacao() {
		List<PessoaFisica> pfList = pessoaFisicaRepository.findAll();

		if (pfList != null && pfList.size() > 0) {
			for (PessoaFisica pf : pfList) {
				SimpleMailMessage email = new SimpleMailMessage();
				email.setTo(pf.getUsuario().getEmail());
				email.setSubject("SISS - Atualização de Dados");
				email.setText("Olá humano, poderia atualizar suas informações, por favor?");
				emailUtils.enviar(email);
			}
		}
	}
}
