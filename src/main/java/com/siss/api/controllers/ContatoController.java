package com.siss.api.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.siss.api.dtos.ContatoDto;
import com.siss.api.entities.Contato;
import com.siss.api.response.Response;
import com.siss.api.services.ContatoService;
import com.siss.api.exceptions.ConsistenciaException;
import com.siss.api.utils.ConversaoUtils;

@RestController
@RequestMapping("/api/contato")
@CrossOrigin(origins = "*")
public class ContatoController {
	private static final Logger log = LoggerFactory.getLogger(ContatoController.class);
	@Autowired
	private ContatoService contatoService;

	/**
	 * Retorna os dados do contato partir do seu id
	 *
	 * @param Id do Contato
	 * @return Dados do Contato
	 */
	@PreAuthorize("hasAnyRole('USUARIO')")
	@GetMapping(value = "/{id}")
	public ResponseEntity<Response<ContatoDto>> buscarPorId(@PathVariable("id") int id) {
		Response<ContatoDto> response = new Response<ContatoDto>();
		try {
			log.info("Controller: buscando o contato com id: {}", id);
			Optional<Contato> contato = contatoService.buscarPorId(id);

			response.setDados(ConversaoUtils.Converter(contato.get()));
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

	/**
	 * Persiste um contato na base.
	 *
	 * @param Dados de entrada do contato
	 * @return Dados da contato persistidp
	 */
	@PreAuthorize("hasAnyRole('USUARIO')")
	@PostMapping
	public ResponseEntity<Response<ContatoDto>> salvar(@Valid @RequestBody ContatoDto contatoDto,
			BindingResult result) {
		Response<ContatoDto> response = new Response<ContatoDto>();
		try {
			log.info("Controller: salvando o contato: {}", contatoDto.toString());

			// Verificando se todos os campos da DTO foram preenchidos
			if (result.hasErrors()) {
				for (int i = 0; i < result.getErrorCount(); i++) {
					response.adicionarErro(result.getAllErrors().get(i).getDefaultMessage());
				}
				log.info("Controller: Os campos obrigatórios não foram preenchidos");
				return ResponseEntity.badRequest().body(response);
			}

			// Converte o objeto ContatoDto para um objeto do tipo Contato(entidade)
			Contato contato = ConversaoUtils.Converter(contatoDto);

			// Salvando a pessoa fisica
			response.setDados(ConversaoUtils.Converter(this.contatoService.salvar(contato)));

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
	
	@PreAuthorize("hasAnyRole('USUARIO')")
	@DeleteMapping(value = "excluir/{id}")
	public ResponseEntity<String> excluirPorId(@PathVariable("id") int id) {
		try {
			log.info("Controller: excluíndo contato de ID: {}", id);
			contatoService.excluirPorId(id);
			return ResponseEntity.ok("contato de id: " + id + " excluído com sucesso");
		} catch (ConsistenciaException e) {
			log.info("Controller: Inconsistência de dados: {}", e.getMessage());
			return ResponseEntity.badRequest().body(e.getMensagem());
		} catch (Exception e) {
			log.error("Controller: Ocorreu um erro na aplicação: {}", e.getMessage());
			return ResponseEntity.status(500).body(e.getMessage());
		}
	}
}
