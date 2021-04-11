package com.siss.api.controllers;

import java.util.ArrayList;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.siss.api.dtos.PessoaFisicaDto;
import com.siss.api.entities.PessoaFisica;
import com.siss.api.response.Response;
import com.siss.api.services.PessoaFisicaService;
import com.siss.api.exceptions.ConsistenciaException;
import com.siss.api.utils.ConversaoUtils;

@RestController
@RequestMapping("/api/pessoaFisica")
@CrossOrigin(origins = "*")
public class PessoaFisicaController {
	private static final Logger log = LoggerFactory.getLogger(PessoaFisicaController.class);
	@Autowired
	private PessoaFisicaService pessoaFisicaService;

	/**
	 * Persiste uma entidade Pessoa Fisica na base.
	 *
	 * @param Dados de entrada da Pessoa Fisica
	 * @return Dados da entidade pessoa fisica persistida
	 */
	@PreAuthorize("hasAnyRole('USUARIO')")
	@PostMapping(value = "/registrar")
	public ResponseEntity<Response<PessoaFisicaDto>> salvar(@Valid @RequestBody PessoaFisicaDto pessoaFisicaDto,
			BindingResult result) {
		Response<PessoaFisicaDto> response = new Response<PessoaFisicaDto>();
		try {
			log.info("Controller: salvando a pessoa fisica: {}", pessoaFisicaDto.toString());

			// Verificando se todos os campos da DTO foram preenchidos
			if (result.hasErrors()) {
				for (int i = 0; i < result.getErrorCount(); i++) {
					response.adicionarErro(result.getAllErrors().get(i).getDefaultMessage());
				}
				log.info("Controller: Os campos obrigatórios não foram preenchidos");
				return ResponseEntity.badRequest().body(response);
			}


			// Converte o objeto pessoaFisicaDto para um objeto do tipo
			// Pessoa Fisica(entidade)
			PessoaFisica pessoaFisica = ConversaoUtils.Converter(pessoaFisicaDto);
			
			// Salvando a pessoa fisica
			response.setDados(ConversaoUtils.Converter(this.pessoaFisicaService.salvar(pessoaFisica)));

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
