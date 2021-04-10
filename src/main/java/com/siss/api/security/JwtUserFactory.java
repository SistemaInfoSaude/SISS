package com.siss.api.security;

import com.siss.api.entities.Usuario;

public class JwtUserFactory {

	public static JwtUser create(Usuario usuario) {

		return new JwtUser(usuario);

	}

}