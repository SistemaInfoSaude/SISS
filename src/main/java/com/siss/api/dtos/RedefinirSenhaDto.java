package com.siss.api.dtos;

import javax.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public class RedefinirSenhaDto {
	@NotEmpty(message = "Id do usuário não pode ser vazio.")
	private String idUsuario;

	@NotEmpty(message = "Codigo não pode ser vazio.")
	@Length(min = 5, max = 5, message = "Codigo deve conter 5 caracteres.")
	private String codigo;

	@NotEmpty(message = "Nova senha não pode ser vazio.")
	@Length(min = 8, max = 25, message = "Nome deve conter entre 8 e 25 caracteres.")
	private String novaSenha;

	public String getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNovaSenha() {
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}

	@Override
	public String toString() {
		return "SenhaDto[idUsuario=" + idUsuario + "," + "codigo=" + codigo + "," + "novaSenha=" + novaSenha + "]";
	}

}