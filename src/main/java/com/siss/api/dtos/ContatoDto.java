package com.siss.api.dtos;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public class ContatoDto {
	private String id;

	@NotEmpty(message = "usuarioId não pode ser vazio.")
	@Length(min = 1, message = "usuarioId deve conter ao menos 1 caractere.")
	private String usuarioId;

	@NotEmpty(message = "nome não pode ser vazio.")
	@Length(min = 3, message = "usuarioId deve conter entre 3 a 100 caracteres.")
	private String nome;

	@Column(name = "telefone", nullable = true, length = 10)
	private String telefone;

	@Column(name = "celular", nullable = true, length = 11)
	private String celular;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(String usuarioId) {
		this.usuarioId = usuarioId;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	@Override
	public String toString() {
		return "Contato[id=" + id + "," + "usuarioId=" + usuarioId + "," + "nome=" + nome + "telefone=" + telefone + ","
				+ "celular=" + celular + "]";
	}
}
