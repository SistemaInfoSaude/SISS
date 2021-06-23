package com.siss.api.controllers;

import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.siss.api.dtos.PessoaJuridicaDto;
import com.siss.api.entities.PessoaJuridica;
import com.siss.api.response.Response;
import com.siss.api.services.PessoaJuridicaService;
import com.siss.api.exceptions.ConsistenciaException;
import com.siss.api.utils.ConversaoUtils;

@RestController
@RequestMapping("/api/pessoaJuridica")
@CrossOrigin(origins = "*")
public class PessoaJuridicaController {
	private static final Logger log = LoggerFactory.getLogger(PessoaJuridicaController.class);
	
	@Autowired
	private PessoaJuridicaService pessoaJuridicaService;

	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping
	public ResponseEntity<Response<PessoaJuridicaDto>> salvar(@Valid @RequestBody PessoaJuridicaDto pessoaJuridicaDto,
			BindingResult result) {
		Response<PessoaJuridicaDto> response = new Response<PessoaJuridicaDto>();
		try {
			log.info("Controller: salvando a pessoa juridica: {}", pessoaJuridicaDto.toString());

			if (result.hasErrors()) {
				for (int i = 0; i < result.getErrorCount(); i++) {
					response.adicionarErro(result.getAllErrors().get(i).getDefaultMessage());
				}
				log.info("Controller: Os campos obrigatórios não foram preenchidos");
				return ResponseEntity.badRequest().body(response);
			}
			
			PessoaJuridica pessoaJuridica = ConversaoUtils.Converter(pessoaJuridicaDto);

			response.setDados(ConversaoUtils.Converter(this.pessoaJuridicaService.salvar(pessoaJuridica)));

			return ResponseEntity.ok(response);
		} catch (ConsistenciaException e) {
			log.info("Controller: Inconsistência de dados: {}", e.getMessage());
			response.adicionarErro(e.getMensagem());

			return ResponseEntity.badRequest().body(response);
		} catch (Exception e) {
			log.error("Controller: Ocorreu um erro na aplicação: {}", e.getMessage());
			response.adicionarErro("Ocorreu um erro na aplicação: {}", e.getMessage());

			return ResponseEntity.status(500).body(response);
		}
	}
}
