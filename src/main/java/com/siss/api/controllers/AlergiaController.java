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

import com.siss.api.dtos.AlergiaDto;
import com.siss.api.dtos.DoencaDto;
import com.siss.api.dtos.VeiculoDto;
import com.siss.api.entities.Alergia;
import com.siss.api.entities.Doenca;
import com.siss.api.entities.Veiculo;
import com.siss.api.response.Response;
import com.siss.api.services.AlergiaService;
import com.siss.api.services.DoencaService;
import com.siss.api.services.VeiculoService;
import com.siss.api.exceptions.ConsistenciaException;
import com.siss.api.utils.ConversaoUtils;

@RestController
@RequestMapping("/api/alergia")
@CrossOrigin(origins = "*")
public class AlergiaController {
	private static final Logger log = LoggerFactory.getLogger(AlergiaController.class);
	@Autowired
	private AlergiaService alergiaService;

	/**
	 * Retorna os dados da Alergia partir do seu id
	 *
	 * @param Id da Alergia
	 * @return Dados do Alergia
	 */
	@PreAuthorize("hasAnyRole('USUARIO')")
	@GetMapping(value = "/{id}")
	public ResponseEntity<Response<AlergiaDto>> buscarPorId(@PathVariable("id") int id) {
		Response<AlergiaDto> response = new Response<AlergiaDto>();
		try {
			log.info("Controller: buscando a alergia com id: {}", id);
			Optional<Alergia> alergia = alergiaService.buscarPorId(id);

			response.setDados(ConversaoUtils.Converter(alergia.get()));
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
	 * Persiste uma Alergia na base.
	 *
	 * @param Dados de entrada da Alergia
	 * @return Dados da Alergia persistida
	 */
	@PreAuthorize("hasAnyRole('USUARIO')")
	@PostMapping
	public ResponseEntity<Response<AlergiaDto>> salvar(@Valid @RequestBody AlergiaDto alergiaDto,
			BindingResult result) {
		Response<AlergiaDto> response = new Response<AlergiaDto>();
		try {
			log.info("Controller: salvando a alergia: {}", alergiaDto.toString());

			// Verificando se todos os campos da DTO foram preenchidos
			if (result.hasErrors()) {
				for (int i = 0; i < result.getErrorCount(); i++) {
					response.adicionarErro(result.getAllErrors().get(i).getDefaultMessage());
				}
				log.info("Controller: Os campos obrigatórios não foram preenchidos");
				return ResponseEntity.badRequest().body(response);
			}

			// Converte o objeto AlergiaDto para um objeto do tipo Alergia(entidade)
			Alergia alergia = ConversaoUtils.Converter(alergiaDto);

			// Salvando a Alergia
			response.setDados(ConversaoUtils.Converter(this.alergiaService.salvar(alergia)));

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
			log.info("Controller: excluíndo alergia a ID: {}", id);
			alergiaService.excluirPorId(id);
			return ResponseEntity.ok("alergia de id: " + id + " excluído com sucesso");
		} catch (ConsistenciaException e) {
			log.info("Controller: Inconsistência de dados: {}", e.getMessage());
			return ResponseEntity.badRequest().body(e.getMensagem());
		} catch (Exception e) {
			log.error("Controller: Ocorreu um erro na aplicação: {}", e.getMessage());
			return ResponseEntity.status(500).body(e.getMessage());
		}

	}

}
