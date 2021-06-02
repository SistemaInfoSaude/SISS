package com.siss.api.security.controllers;

import java.util.Optional;

import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.siss.api.security.utils.JwtTokenUtil;
import com.siss.api.services.UsuarioService;
import com.siss.api.entities.Usuario;
import com.siss.api.exceptions.ConsistenciaException;
import com.siss.api.response.Response;
import com.siss.api.security.dtos.JwtAuthenticationDto;
import com.siss.api.security.dtos.JwtAuthenticationEmailDto;
import com.siss.api.security.dtos.TokenDto;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthenticationController {

	private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private UsuarioService usuarioService;

	/**
	 * Retorna uma novo token válido caso o login (usuario) e senha sejam válidos
	 *
	 * @param Dados para autenticação (usuario e senha)
	 * @return Token válido
	 * @throws AuthenticationException
	 */
	@PostMapping(value = "/username")
	public ResponseEntity<Response<TokenDto>> gerarTokenJwtUsername(
			@Valid @RequestBody JwtAuthenticationDto authenticationDto, BindingResult result) {
		Response<TokenDto> response = new Response<TokenDto>();
		try {
			log.info("Gerando token para o username {}.", authenticationDto.getUsuario());
			if (result.hasErrors()) {
				for (int i = 0; i < result.getErrorCount(); i++) {

					response.adicionarErro(result.getAllErrors().get(i).getDefaultMessage());
				}
				log.info("Controller: Os campos obrigatórios não foram preenchidos");
				return ResponseEntity.badRequest().body(response);
			}
			Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationDto.getUsuario(), authenticationDto.getSenha()));

			SecurityContextHolder.getContext().setAuthentication(authentication);

			UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationDto.getUsuario());
			response.setDados(new TokenDto(jwtTokenUtil.obterToken(userDetails)));

			return ResponseEntity.ok(response);
		} catch (BadCredentialsException e) {
			log.info("Controller: Usuário ou senha inválido!");
			response.adicionarErro("Usuário ou senha inválido!");

			return ResponseEntity.badRequest().body(response);
		} catch (Exception e) {
			log.error("Controller: Ocorreu um erro na aplicação: {}", e.getMessage());
			response.adicionarErro("Ocorreu um erro na aplicação: {}", e.getMessage());

			return ResponseEntity.status(500).body(response);
		}
	}

	/**
	 * Retorna uma novo token válido caso o login (email) e senha sejam válidos
	 *
	 * @param Dados para autenticação (email e senha)
	 * @return Token válido
	 * @throws AuthenticationException
	 */
	@PostMapping(value = "/email")
	public ResponseEntity<Response<TokenDto>> gerarTokenJwtEmail(
			@Valid @RequestBody JwtAuthenticationEmailDto authenticationDto, BindingResult result) {
		Response<TokenDto> response = new Response<TokenDto>();
		try {
			log.info("Gerando token para o email {}.", authenticationDto.getEmail());

			if (result.hasErrors()) {
				for (int i = 0; i < result.getErrorCount(); i++) {

					response.adicionarErro(result.getAllErrors().get(i).getDefaultMessage());
				}
				log.info("Controller: Os campos obrigatórios não foram preenchidos");
				return ResponseEntity.badRequest().body(response);
			}

			Optional<Usuario> usr = usuarioService.buscarPorEmail(authenticationDto.getEmail());
			String username = usr.get().getUsuario();

			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(username, authenticationDto.getSenha()));

			SecurityContextHolder.getContext().setAuthentication(authentication);

			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			response.setDados(new TokenDto(jwtTokenUtil.obterToken(userDetails)));

			return ResponseEntity.ok(response);
		} catch (ConsistenciaException e) {
			log.info("Controller: {}", e.getMessage());
			response.adicionarErro(e.getMensagem());

			return ResponseEntity.badRequest().body(response);
		} catch (BadCredentialsException e) {
			log.info("Controller: Email ou senha inválido!");
			response.adicionarErro("Email ou senha inválido!");

			return ResponseEntity.badRequest().body(response);
		} catch (Exception e) {
			log.error("Controller: Ocorreu um erro na aplicação: {}", e.getMessage());
			response.adicionarErro("Ocorreu um erro na aplicação: {}", e.getMessage());

			return ResponseEntity.status(500).body(response);
		}
	}
}
