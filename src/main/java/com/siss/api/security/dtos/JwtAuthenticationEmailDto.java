package com.siss.api.security.dtos;

import javax.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public class JwtAuthenticationEmailDto {
	@NotEmpty(message = "Email não pode ser vazio.")
	private String email;

	@NotEmpty(message = "Senha não pode ser vazia.")
	@Length(min = 8, max = 25, message = "Senha atual deve conter entre 8 e 25 caracteres.")
	private String senha;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@Override
	public String toString() {
		return "JwtAuthenticationRequestDto[email=" + email + ", senha=" + senha + "]";
	}
}