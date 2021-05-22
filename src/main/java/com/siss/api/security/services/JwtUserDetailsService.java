package com.siss.api.security.services;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.siss.api.entities.Usuario;
import com.siss.api.security.JwtUserFactory;
import com.siss.api.security.utils.JwtTokenUtil;
import com.siss.api.services.UsuarioService;
import com.siss.api.utils.SenhaUtils;

import io.jsonwebtoken.Claims;

import com.siss.api.exceptions.ConsistenciaException;
import javax.servlet.http.HttpServletRequest;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private HttpServletRequest httpServletRequest;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Usuario> usuario;

		try {
			usuario = usuarioService.verificarCredenciais(username);
			return JwtUserFactory.create(usuario.get());
		} catch (ConsistenciaException e) {
			throw new UsernameNotFoundException(e.getMensagem());
		}
	}
	
	public void checkUser(Usuario usr) throws ConsistenciaException {
		String token = httpServletRequest.getHeader("Authorization");
		
		if (token != null && token.startsWith("Bearer ")) {
			token = token.substring(7);
		}
		String username = jwtTokenUtil.getUsernameFromToken(token);
		
		if (!usr.getUsuario().equals(username)) {
			throw new ConsistenciaException("Você não tem permissão para realizar essa requisição.");
		}
	}
}
