package com.siss.api.crons;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
public class PessoaFisicaCron {

	@Scheduled(cron = "0 0 1 */6 *")
	public void enviarEmailAtualizacao() {
		
	}
	
}
