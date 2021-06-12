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

import com.siss.api.dtos.VeiculoDto;
import com.siss.api.entities.Veiculo;
import com.siss.api.response.Response;
import com.siss.api.services.VeiculoService;
import com.siss.api.exceptions.ConsistenciaException;
import com.siss.api.utils.ConversaoUtils;

@RestController
@RequestMapping("/api/veiculo")
@CrossOrigin(origins = "*")
public class VeiculoController {
	private static final Logger log = LoggerFactory.getLogger(VeiculoController.class);
	@Autowired
	private VeiculoService veiculoService;

	/**
	 * Retorna os dados do veiculo partir do seu id
	 *
	 * @param Id do Veiculo
	 * @return Dados do Veiculo
	 */
	@PreAuthorize("hasAnyRole('USUARIO')")
	@GetMapping(value = "/{id}")
	public ResponseEntity<Response<VeiculoDto>> buscarPorId(@PathVariable("id") int id) {
		Response<VeiculoDto> response = new Response<VeiculoDto>();
		try {
			log.info("Controller: buscando o veiculo com id: {}", id);
			Optional<Veiculo> veiculo = veiculoService.buscarPorId(id);

			response.setDados(ConversaoUtils.Converter(veiculo.get()));
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
	 * Retorna os veiculos de um usuário
	 *
	 * @param Id do Usuario
	 * @return Lista de Veiculos
	 */
	@PreAuthorize("hasAnyRole('USUARIO')")
	@GetMapping(value = "/pessoaFisicaId/{pessoaFisicaId}")
	public ResponseEntity<Response<List<VeiculoDto>>> buscarPorPessoaFisicaId(@PathVariable("pessoaFisicaId") int pessoaFisicaId) {
		Response<List<VeiculoDto>> response = new Response<List<VeiculoDto>>();
		try {
			log.info("Controller: buscando os veiculos da PF de ID: {}", pessoaFisicaId);
			Optional<List<Veiculo>> listaVeiculos = veiculoService.buscarPorPessoaFisicaId(pessoaFisicaId);
			
			response.setDados(ConversaoUtils.ConverterLista(listaVeiculos.get()));

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
	 * Persiste um veiculo na base.
	 *
	 * @param Dados de entrada do veiculo
	 * @return Dados do veiculo persistidp
	 */
	@PreAuthorize("hasAnyRole('USUARIO')")
	@PostMapping
	public ResponseEntity<Response<VeiculoDto>> salvar(@Valid @RequestBody VeiculoDto veiculoDto,
			BindingResult result) {
		Response<VeiculoDto> response = new Response<VeiculoDto>();
		try {
			log.info("Controller: salvando o veiculo: {}", veiculoDto.toString());

			// Verificando se todos os campos da DTO foram preenchidos
			if (result.hasErrors()) {
				for (int i = 0; i < result.getErrorCount(); i++) {
					response.adicionarErro(result.getAllErrors().get(i).getDefaultMessage());
				}
				log.info("Controller: Os campos obrigatórios não foram preenchidos");
				return ResponseEntity.badRequest().body(response);
			}

			// Converte o objeto VeiculoDto para um objeto do tipo Veiculo(entidade)
			Veiculo veiculo = ConversaoUtils.Converter(veiculoDto);

			// Salvando o veiculo
			response.setDados(ConversaoUtils.Converter(this.veiculoService.salvar(veiculo)));

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
			log.info("Controller: excluíndo veiculo de ID: {}", id);
			veiculoService.excluirPorId(id);
			return ResponseEntity.ok("veiculo de id: " + id + " excluído com sucesso");
		} catch (ConsistenciaException e) {
			log.info("Controller: Inconsistência de dados: {}", e.getMessage());
			return ResponseEntity.badRequest().body(e.getMensagem());
		} catch (Exception e) {
			log.error("Controller: Ocorreu um erro na aplicação: {}", e.getMessage());
			return ResponseEntity.status(500).body(e.getMessage());
		}
	}
}
