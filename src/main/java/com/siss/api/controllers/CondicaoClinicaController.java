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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.siss.api.response.Response;
import com.siss.api.services.CondicaoClinicaService;
import com.siss.api.dtos.CondicaoClinicaDto;
import com.siss.api.entities.CondicaoClinica;
import com.siss.api.exceptions.ConsistenciaException;
import com.siss.api.utils.ConversaoUtils;

@RestController
@RequestMapping("/api/condicaoClinica")
@CrossOrigin(origins = "*")
public class CondicaoClinicaController {
	private static final Logger log = LoggerFactory.getLogger(CondicaoClinicaController.class);
	@Autowired
	private CondicaoClinicaService condicaoClinicaService;

	@PreAuthorize("hasAnyRole('USUARIO')")
	@GetMapping(value = "/{id}")
	public ResponseEntity<Response<CondicaoClinicaDto>> buscarPorId(@PathVariable("id") int id) {
		Response<CondicaoClinicaDto> response = new Response<CondicaoClinicaDto>();
		try {
			log.info("Controller: buscando a condicaoClinica com id: {}", id);
			Optional<CondicaoClinica> condicaoClinica = condicaoClinicaService.buscarPorId(id);

			response.setDados(ConversaoUtils.Converter(condicaoClinica.get()));
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
	@PostMapping
	public ResponseEntity<Response<CondicaoClinicaDto>> salvar(@Valid @RequestBody CondicaoClinicaDto condicaoClinicaDto,
			BindingResult result) {
		Response<CondicaoClinicaDto> response = new Response<CondicaoClinicaDto>();
		try {
			log.info("Controller: salvando a condicaoClinica: {}", condicaoClinicaDto.toString());

			// Verificando se todos os campos da DTO foram preenchidos
			if (result.hasErrors()) {
				for (int i = 0; i < result.getErrorCount(); i++) {
					response.adicionarErro(result.getAllErrors().get(i).getDefaultMessage());
				}
				log.info("Controller: Os campos obrigatórios não foram preenchidos");
				return ResponseEntity.badRequest().body(response);
			}

			// Converte o objeto CondicaoClinicaDto para um objeto do tipo CondicaoClinica(entidade)
			CondicaoClinica condicaoClinica = ConversaoUtils.Converter(condicaoClinicaDto);

			// Salvando a condicaoClinica
			response.setDados(ConversaoUtils.Converter(this.condicaoClinicaService.salvar(condicaoClinica)));

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
