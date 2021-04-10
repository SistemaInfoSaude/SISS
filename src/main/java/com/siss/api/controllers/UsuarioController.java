package com.siss.api.controllers;

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
import com.siss.api.dtos.SenhaDto;
import com.siss.api.dtos.UsuarioDto;
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
	@PreAuthorize("hasAnyRole('USUARIO')")
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
}
