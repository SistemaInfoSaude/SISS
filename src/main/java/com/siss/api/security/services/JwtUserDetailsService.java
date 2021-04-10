package com.siss.api.security.services;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.siss.api.entities.Usuario;
import com.siss.api.security.JwtUserFactory;
import com.siss.api.services.UsuarioService;
import com.siss.api.exceptions.ConsistenciaException;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	@Autowired
	private UsuarioService usuarioService;

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
}
