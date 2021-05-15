package com.siss.api.dtos;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;

public class UsuarioDto {
	private String id;

	@NotEmpty(message = "Usuario não pode ser vazio.")
	@Length(min = 5, max = 100, message = "Usuario deve conter entre 5 e 100 caracteres.")
	private String usuario;

	@NotEmpty(message = "Email não pode ser vazio.")
	@Length(min = 10, max = 150, message = "Email deve conter entre 10 e 150 caracteres.")
	private String email;

	private String senha;

	private String executante;

	private List<RegraDto> regras;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

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

	public String getExecutante() {
		return executante;
	}

	public void setExecutante(String executante) {
		this.executante = executante;
	}

	public List<RegraDto> getRegras() {
		return regras;
	}

	public void setRegras(List<RegraDto> regras) {
		this.regras = regras;
	}

	@Override
	public String toString() {
		return "Usuario[id=" + id + "," + "usuario=" + usuario + "," + "email=" + email + "]";
	}
}
