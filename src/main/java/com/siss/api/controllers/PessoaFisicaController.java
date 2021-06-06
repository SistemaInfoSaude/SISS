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

import com.siss.api.dtos.PessoaFisicaDto;
import com.siss.api.dtos.PessoaFisicaInfoDto;
import com.siss.api.dtos.UsuarioDto;
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
	 * Retorna os dados de uma pessoa fisica a partir do seu id
	 *
	 * @param Id da PessoaFisica
	 * @return Dados da Pessoa Fisica
	 */
	@PreAuthorize("hasAnyRole('USUARIO')")
	@GetMapping(value = "/{id}")
	public ResponseEntity<Response<PessoaFisicaInfoDto>> buscarPorId(@PathVariable("id") int id) {
		Response<PessoaFisicaInfoDto> response = new Response<PessoaFisicaInfoDto>();
		try {
			log.info("Controller: buscando a PF com id: {}", id);
			Optional<PessoaFisica> pessoaFisica = pessoaFisicaService.buscarPorId(id);
			
			response.setDados(ConversaoUtils.Converter(pessoaFisica.get()));
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
	 * Retorna os dados de uma pessoa fisica a partir de seu CPF
	 *
	 * @param CPF da PessoaFisica
	 * @return Dados da Pessoa Fisica
	 */
	@PreAuthorize("hasAnyRole('EXEC_USUARIO')")
	@GetMapping(value = "/cpf/{cpf}")
	public ResponseEntity<Response<PessoaFisicaInfoDto>> buscarPorCpf(@PathVariable("cpf") String cpf) {
		Response<PessoaFisicaInfoDto> response = new Response<PessoaFisicaInfoDto>();
		try {
			log.info("Controller: buscando a PF com CPF: {}", cpf);
			Optional<PessoaFisica> pessoaFisica = pessoaFisicaService.buscarPorCpf(cpf);
			
			response.setDados(ConversaoUtils.Converter(pessoaFisica.get()));
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
	 * Retorna os dados de uma pessoa fisica a partir de seu RG
	 *
	 * @param RG da PessoaFisica
	 * @return Dados da Pessoa Fisica
	 */
	@PreAuthorize("hasAnyRole('EXEC_USUARIO')")
	@GetMapping(value = "/rg/{rg}")
	public ResponseEntity<Response<PessoaFisicaInfoDto>> buscarPorRg(@PathVariable("rg") String rg) {
		Response<PessoaFisicaInfoDto> response = new Response<PessoaFisicaInfoDto>();
		try {
			log.info("Controller: buscando a PF com RG: {}", rg);
			Optional<PessoaFisica> pessoaFisica = pessoaFisicaService.buscarPorRg(rg);
			
			response.setDados(ConversaoUtils.Converter(pessoaFisica.get()));
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
	 * Retorna os dados de uma pessoa fisica a partir da placa do veiculo
	 *
	 * @param Placa do Veiculo
	 * @return Dados da Pessoa Fisica
	 */
	@PreAuthorize("hasAnyRole('EXEC_USUARIO')")
	@GetMapping(value = "/placaVeiculo/{placa}")
	public ResponseEntity<Response<List<PessoaFisica>>> buscarPorPlacaVeiculo(@PathVariable("placa") String placa) {
		Response<List<PessoaFisica>> response = new Response<List<PessoaFisica>>();
		try {
			log.info("Controller: buscando a PF que possuir o veiculo com a placa: {}", placa);
			Optional<List<PessoaFisica>> pessoaFisica = pessoaFisicaService.buscarPorPlacaVeiculo(placa);
			
			response.setDados(pessoaFisica.get());
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
	 * Retorna os dados de uma pessoa fisica a partir de seu usuarioId
	 *
	 * @param usuarioId da PessoaFisica
	 * @return Dados da Pessoa Fisica
	 */
	@PreAuthorize("hasAnyRole('USUARIO')")
	@GetMapping(value = "/usuarioId/{usuarioId}")
	public ResponseEntity<Response<PessoaFisicaInfoDto>> buscarPorUsuarioId(@PathVariable("usuarioId") int usuarioId) {
		Response<PessoaFisicaInfoDto> response = new Response<PessoaFisicaInfoDto>();
		try {
			log.info("Controller: buscando a PF com usuarioId: {}", usuarioId);
			Optional<PessoaFisica> pessoaFisica = pessoaFisicaService.buscarPorUsuarioId(usuarioId);
			
			response.setDados(ConversaoUtils.Converter(pessoaFisica.get()));
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
	 * Persiste uma entidade Pessoa Fisica na base.
	 *
	 * @param Dados de entrada da Pessoa Fisica
	 * @return Dados da entidade pessoa fisica persistida
	 */
	@PreAuthorize("hasAnyRole('USUARIO')")
	@PostMapping
	public ResponseEntity<Response<PessoaFisicaInfoDto>> salvar(@Valid @RequestBody PessoaFisicaDto pessoaFisicaDto,
			BindingResult result) {
		Response<PessoaFisicaInfoDto> response = new Response<PessoaFisicaInfoDto>();
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
