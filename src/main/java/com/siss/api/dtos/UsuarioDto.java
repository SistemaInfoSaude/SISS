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
	
	private String senha;

	private List<RegraDto> regras;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id= id;
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

	public List<RegraDto> getRegras() {
		return regras;
	}

	public void setRegras(List<RegraDto> regras) {
		this.regras = regras;
	}

	@Override
	public String toString() {
		return "Usuario[id=" + id+ "," + "usuario=" + usuario + "]";
	}

}
