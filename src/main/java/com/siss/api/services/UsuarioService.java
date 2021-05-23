package com.siss.api.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.siss.api.dtos.CodigoEmailDto;
import com.siss.api.entities.Regra;
import com.siss.api.entities.Usuario;
import com.siss.api.repositories.RegraRepository;
import com.siss.api.repositories.UsuarioRepository;
import com.siss.api.security.utils.JwtTokenUtil;
import com.siss.api.exceptions.ConsistenciaException;
import com.siss.api.utils.EmailUtils;
import com.siss.api.utils.SenhaUtils;

@Service
public class UsuarioService {

	private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);
	@Autowired

	private UsuarioRepository usuarioRepository;
	@Autowired

	private RegraRepository regraReprository;
	@Autowired

	private HttpServletRequest httpServletRequest;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	EmailUtils emailUtils;

	public Optional<Usuario> buscarPorId(int id) throws ConsistenciaException {
		log.info("Service: buscando um usuário com o id: {}", id);
		Optional<Usuario> usuario = usuarioRepository.findById(id);

		if (!usuario.isPresent()) {
			log.info("Service: Nenhum usuário com id: {} foi encontrado", id);
			throw new ConsistenciaException("Nenhum usuário com id: {} foi encontrado", id);
		}

		return usuario;
	}

	public Optional<Usuario> buscarPorEmail(String email) throws ConsistenciaException {
		log.info("Service: buscando um usuário com o email: {}", email);
		Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

		if (!usuario.isPresent()) {
			log.info("Service: Nenhum usuário com email: {} foi encontrado", email);
			throw new ConsistenciaException("Nenhum usuário com email: {} foi encontrado", email);
		}

		return usuario;
	}

	public Optional<Usuario> verificarCredenciais(String usuarioNome) throws ConsistenciaException {
		log.info("Service: criando credenciais para o usuário: '{}'", usuarioNome);
		Optional<Usuario> usuario = Optional.ofNullable(usuarioRepository.findByUsuario(usuarioNome));

		if (!usuario.isPresent()) {
			log.info("Service: Nenhum usuário: {} foi encontrado", usuarioNome);
			throw new ConsistenciaException("Nenhum usuário: {} foi encontrado", usuarioNome);
		}

		usuario.get().setRegras(
				usuario.get().getRegras().stream().filter(r -> r.getAtivo() == true).collect(Collectors.toList()));

		return usuario;
	}

	public Usuario salvar(Usuario usuario) throws ConsistenciaException {
		log.info("Service: salvando o usuario: {}", usuario);

		// Se foi informando ID na DTO, é porque trata-se de uma ALTERAÇÃO
		if (usuario.getId() > 0) {
			// Verificar se o ID existe na base
			Optional<Usuario> usr = buscarPorId(usuario.getId());

			// Setando a senha do objeto usuário com a mesma senha encontarda na base.
			// Se não fizermos isso, a senha fica em branco.
			usuario.setSenha(usr.get().getSenha());
		} else {
			// Se NÃO foi informando ID na DTO, é porque trata-se de uma INCLUSÃO
			if (usuario.getSenha() == null || usuario.getSenha() == "") {
				throw new ConsistenciaException("Senha não foi informado para o usuario: {}", usuario.getUsuario());
			}

			usuario.setSenha(SenhaUtils.gerarHash(usuario.getSenha()));
			usuario.setDataCadastro(new Date());

			// Seta regra ao usuário
			Regra regra = new Regra();
			if (usuario.getExecutante()) {
				regra.setNome("ROLE_EXEC_USUARIO");
			} else {
				regra.setNome("ROLE_USUARIO");
			}
			usuario.setRegras(new ArrayList<Regra>());
			usuario.getRegras().add(regra);

		}

		usuario.setDataAlteracao(new Date());

		// Carregando as regras definidas para o usuário, caso existam
		if (usuario.getRegras() != null) {
			List<Regra> aux = new ArrayList<Regra>(usuario.getRegras().size());
			for (Regra regra : usuario.getRegras()) {
				Optional<Regra> rg = Optional.ofNullable(regraReprository.findByNome(regra.getNome()));
				if (rg.isPresent()) {
					aux.add(rg.get());
				} else {
					log.info("A regra '{}' não existe", regra.getNome());
					throw new ConsistenciaException("A regra '{}' não existe", regra.getNome());
				}
			}
			usuario.setRegras(aux);
		}

		try {
			return usuarioRepository.save(usuario);
		} catch (DataIntegrityViolationException e) {
			log.info("Service: O usuario '{}' já está cadastrado", usuario.getUsuario());
			throw new ConsistenciaException("O usuario '{}' já está cadastrado", usuario.getUsuario());
		}
	}

	public void alterarSenhaUsuario(String senhaAtual, String novaSenha, int id)
			throws ConsistenciaException {

		log.info("Service: alterando a senha do usuário: {}", id);

		Optional<Usuario> usr = buscarPorId(id);

		String token = httpServletRequest.getHeader("Authorization");
		if (token != null && token.startsWith("Bearer ")) {
			token = token.substring(7);
		}

		String username = jwtTokenUtil.getUsernameFromToken(token);
		
		// Verificar se o usuário do token é diferente do usuário que está sendo
		// alterado
		if (!usr.get().getUsuario().equals(username)) {
			log.info("Service: Usuario do token diferente do usuário a ser alterado");
			throw new ConsistenciaException("Você não tem permissão para alterar a senha de outro usuário.");
		}

		// Verificar se a senha atual do usuário diferente da informada na entrada
		if (!SenhaUtils.compararHash(senhaAtual, usr.get().getSenha())) {
			log.info("Service: A senha atual informada não é válida");
			throw new ConsistenciaException("A senha atual informada não é válida.");
		}

		usuarioRepository.alterarSenhaUsuario(SenhaUtils.gerarHash(novaSenha), id);
	}

	public CodigoEmailDto enviarCodigoAlteracaoSenha(String email) throws ConsistenciaException {
		Optional<Usuario> usuario = buscarPorEmail(email);
		CodigoEmailDto codigoEmailDto = new CodigoEmailDto();

		if (usuario.get().getId() > 0) {
			String hashCode = Integer.toString(SenhaUtils.gerarHashCode());
			usuario.get().setHashCode(hashCode);
			usuarioRepository.save(usuario.get());

			emailUtils.enviar(
				usuario.get().getEmail(), 
				"SISS - Alteração senha", 
				"Código: " + hashCode
			);
			
			codigoEmailDto.setEmail(usuario.get().getEmail());
			codigoEmailDto.setCodigo(hashCode);
			codigoEmailDto.setIdUsuario(Integer.toString(usuario.get().getId()));
			codigoEmailDto.setMensagem("Código enviado com sucesso.");
		}else {
			throw new ConsistenciaException("Não foi possível enviar o código.");
		}

		return codigoEmailDto;
	}
	
	public void redefinirSenhaUsuario(String codigo, String novaSenha, int id)
			throws ConsistenciaException {
		Optional<Usuario> usr = buscarPorId(id);
		
		// Verificar se o código de confirmação é igual ao enviado no email do usuário
		if (!usr.get().getHashCode().equals(codigo)) {
			log.info("Service: Código de confirmação inválido");
			throw new ConsistenciaException("Código de confirmação inválido.");
		}
		
		usuarioRepository.alterarSenhaUsuario(SenhaUtils.gerarHash(novaSenha), id);
		usuarioRepository.resetarHashCode(id);
	}
}