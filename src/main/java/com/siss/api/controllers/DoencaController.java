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

import com.siss.api.dtos.DoencaDto;
import com.siss.api.dtos.VeiculoDto;
import com.siss.api.entities.Doenca;
import com.siss.api.entities.Veiculo;
import com.siss.api.response.Response;
import com.siss.api.services.DoencaService;
import com.siss.api.services.VeiculoService;
import com.siss.api.exceptions.ConsistenciaException;
import com.siss.api.utils.ConversaoUtils;

@RestController
@RequestMapping("/api/doenca")
@CrossOrigin(origins = "*")
public class DoencaController {
	private static final Logger log = LoggerFactory.getLogger(DoencaController.class);
	@Autowired
	private DoencaService doencaService;

	/**
	 * Retorna os dados da doenca partir do seu id
	 *
	 * @param Id da doenca
	 * @return Dados do doenca
	 */
	@PreAuthorize("hasAnyRole('USUARIO')")
	@GetMapping(value = "/{id}")
	public ResponseEntity<Response<DoencaDto>> buscarPorId(@PathVariable("id") int id) {
		Response<DoencaDto> response = new Response<DoencaDto>();
		try {
			log.info("Controller: buscando a doenca com id: {}", id);
			Optional<Doenca> doenca = doencaService.buscarPorId(id);

			response.setDados(ConversaoUtils.Converter(doenca.get()));
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
	 * Persiste uma doenca na base.
	 *
	 * @param Dados de entrada da doenca
	 * @return Dados da doenca persistida
	 */
	@PreAuthorize("hasAnyRole('USUARIO')")
	@PostMapping
	public ResponseEntity<Response<DoencaDto>> salvar(@Valid @RequestBody DoencaDto doencaDto,
			BindingResult result) {
		Response<DoencaDto> response = new Response<DoencaDto>();
		try {
			log.info("Controller: salvando o veiculo: {}", doencaDto.toString());

			// Verificando se todos os campos da DTO foram preenchidos
			if (result.hasErrors()) {
				for (int i = 0; i < result.getErrorCount(); i++) {
					response.adicionarErro(result.getAllErrors().get(i).getDefaultMessage());
				}
				log.info("Controller: Os campos obrigatórios não foram preenchidos");
				return ResponseEntity.badRequest().body(response);
			}

			// Converte o objeto DoencaDto para um objeto do tipo Doenca(entidade)
			Doenca doenca = ConversaoUtils.Converter(doencaDto);

			// Salvando a doenca
			response.setDados(ConversaoUtils.Converter(this.doencaService.salvar(doenca)));

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
