package com.siss.api.security.dtos;

import javax.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public class JwtAuthenticationDto {
	@NotEmpty(message = "Usuario não pode ser vazio.")
	private String usuario;

	@NotEmpty(message = "Senha não pode ser vazia.")
	@Length(min = 8, max = 25, message = "Senha atual deve conter entre 8 e 25 caracteres.")
	private String senha;

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@Override
	public String toString() {
		return "JwtAuthenticationRequestDto[usuario=" + usuario + ", senha=" + senha + "]";
	}

}