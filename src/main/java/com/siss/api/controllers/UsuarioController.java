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

import com.siss.api.dtos.CodigoEmailDto;
import com.siss.api.dtos.RedefinirSenhaDto;
import com.siss.api.dtos.SenhaDto;
import com.siss.api.dtos.UsuarioDto;
import com.siss.api.entities.Regra;
import com.siss.api.entities.Usuario;
import com.siss.api.response.Response;
import com.siss.api.services.UsuarioService;
import com.siss.api.exceptions.ConsistenciaException;
import com.siss.api.utils.ConversaoUtils;

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin(origins = "*")
public class UsuarioController {
	private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);
	@Autowired
	private UsuarioService usuarioService;

	/**
	 * Retorna os dados de um usuario a partir do id informado
	 *
	 * @param Id do usuário
	 * @return Dados do usuário
	 */
	@GetMapping(value = "/{id}")
	@PreAuthorize("hasAnyRole('USUARIO', 'ROLE_EXEC_USUARIO')")
	public ResponseEntity<Response<UsuarioDto>> buscarPorId(@PathVariable("id") int id) {
		Response<UsuarioDto> response = new Response<UsuarioDto>();
		try {
			log.info("Controller: buscando usuario com id: {}", id);
			Optional<Usuario> usuario = usuarioService.buscarPorId(id);
			response.setDados(ConversaoUtils.Converter(usuario.get()));

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
	 * Persiste um usuário na base.
	 *
	 * @param Dados de entrada do usuário
	 * @return Dados do usuario persistido
	 */
	@PostMapping(value = "/registrar")
	public ResponseEntity<Response<UsuarioDto>> salvar(@Valid @RequestBody UsuarioDto usuarioDto,
			BindingResult result) {
		Response<UsuarioDto> response = new Response<UsuarioDto>();
		try {
			log.info("Controller: salvando o usuario: {}", usuarioDto.toString());

			// Verificando se todos os campos da DTO foram preenchidos
			if (result.hasErrors()) {
				for (int i = 0; i < result.getErrorCount(); i++) {
					response.adicionarErro(result.getAllErrors().get(i).getDefaultMessage());
				}
				log.info("Controller: Os campos obrigatórios não foram preenchidos");
				return ResponseEntity.badRequest().body(response);
			}
			
			if(usuarioDto.getExecutante() == null || usuarioDto.getExecutante().equals("1")) {
				throw new ConsistenciaException("Você não possuí permissão para executante igual a 1");
			}

			// Converte o objeto usuarioDto para um objeto do tipo Usuario (entidade)
			Usuario usuario = ConversaoUtils.Converter(usuarioDto);

			// Salvando o usuário
			response.setDados(ConversaoUtils.Converter(this.usuarioService.salvar(usuario)));

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
	
	@PostMapping(value = "/registrarPj")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<UsuarioDto>> salvarPj(@Valid @RequestBody UsuarioDto usuarioDto,
			BindingResult result) {
		
		Response<UsuarioDto> response = new Response<UsuarioDto>();
		
		try {
			if (result.hasErrors()) {
				for (int i = 0; i < result.getErrorCount(); i++) {
					response.adicionarErro(result.getAllErrors().get(i).getDefaultMessage());
				}
				return ResponseEntity.badRequest().body(response);
			}
			
			if(usuarioDto.getExecutante() == null || !usuarioDto.getExecutante().equals("1")) {
				throw new ConsistenciaException("Necessário executante igual 1");
			}

			Usuario usuario = ConversaoUtils.Converter(usuarioDto);
			response.setDados(ConversaoUtils.Converter(this.usuarioService.salvar(usuario)));

			return ResponseEntity.ok(response);
		} catch (ConsistenciaException e) {
			response.adicionarErro(e.getMensagem());
			return ResponseEntity.badRequest().body(response);
		} catch (Exception e) {
			response.adicionarErro("Ocorreu um erro na aplicação: {}", e.getMessage());
			return ResponseEntity.status(500).body(response);
		}
	}

	/**
	 * Altera a senha do usuário, verificando o próprio usuário e a senha atual.
	 *
	 * @param Dados de entrada do usuário
	 * @return Dados do usuario persistido
	 */
	@PostMapping(value = "/alterarSenha")
	@PreAuthorize("hasAnyRole('USUARIO')")
	public ResponseEntity<Response<SenhaDto>> alterarSenhaUsuario(@Valid @RequestBody SenhaDto senhaDto,
			BindingResult result) {

		Response<SenhaDto> response = new Response<SenhaDto>();

		try {
			log.info("Controller: alterando a senha do usuário: {}", senhaDto.getIdUsuario());
			if (result.hasErrors()) {
				for (int i = 0; i < result.getErrorCount(); i++) {

					response.adicionarErro(result.getAllErrors().get(i).getDefaultMessage());
				}
				log.info("Controller: Os campos obrigatórios não foram preenchidos");
				return ResponseEntity.badRequest().body(response);
			}

			this.usuarioService.alterarSenhaUsuario(senhaDto.getSenhaAtual(), senhaDto.getNovaSenha(),
					Integer.parseInt(senhaDto.getIdUsuario()));

			response.setDados(senhaDto);
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
	 * Envia código de validação ao email do usuário para alterar a senha
	 *
	 * @param Email do usuario
	 * @return sucesso ou erro no envio
	 */
	@PostMapping(value = "/enviarCodigo")
	public ResponseEntity<Response<CodigoEmailDto>> enviarCodigo(@Valid @RequestBody CodigoEmailDto codigoEmailDto,
			BindingResult result) {

		Response<CodigoEmailDto> response = new Response<CodigoEmailDto>();

		try {
			response.setDados(usuarioService.enviarCodigoAlteracaoSenha(codigoEmailDto.getEmail()));
			return ResponseEntity.ok(response);
			
		} catch (ConsistenciaException e) {
			response.adicionarErro(e.getMensagem());
			return ResponseEntity.badRequest().body(response);
		} catch (Exception e) {
			response.adicionarErro("Ocorreu um erro na aplicação: {}", e.getMessage());
			return ResponseEntity.status(500).body(response);
		}
	}

	/**
	 * Altera a senha do usuário, verificando o código de email
	 *
	 * @param Dados de entrada do usuário
	 * @return Dados do usuario persistido
	 */
	@PostMapping(value = "/redefinirSenha")
	public ResponseEntity<Response<RedefinirSenhaDto>> novaSenhaUsuario(
			@Valid @RequestBody RedefinirSenhaDto redefinirSenhaDto, BindingResult result) {

		Response<RedefinirSenhaDto> response = new Response<RedefinirSenhaDto>();

		try {
			if (result.hasErrors()) {
				for (int i = 0; i < result.getErrorCount(); i++) {
					response.adicionarErro(result.getAllErrors().get(i).getDefaultMessage());
				}
				return ResponseEntity.badRequest().body(response);
			}

			this.usuarioService.redefinirSenhaUsuario(redefinirSenhaDto.getCodigo(), redefinirSenhaDto.getNovaSenha(),
					Integer.parseInt(redefinirSenhaDto.getIdUsuario()));

			response.setDados(redefinirSenhaDto);
			return ResponseEntity.ok(response);
		} catch (ConsistenciaException e) {

			response.adicionarErro(e.getMensagem());
			return ResponseEntity.badRequest().body(response);
		} catch (Exception e) {

			response.adicionarErro("Ocorreu um erro na aplicação: {}", e.getMessage());
			return ResponseEntity.status(500).body(response);
		}
	}
}
