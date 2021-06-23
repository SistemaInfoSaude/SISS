package com.siss.api.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PessoaJuridicaDto {
	private String id;

	@NotBlank
	@NotEmpty(message = "usuarioId não pode ser vazio.")
	@Length(min = 1, message = "usuarioId deve conter ao menos 1 caractere.")
	private String usuarioId;

	@NotEmpty(message = "CNPJ não pode ser vazio.")
	@CNPJ(message = "CNPJ inválido.")
	private String cnpj;

	@NotBlank
	@NotEmpty(message = "Razao Social não pode ser vazio.")
	private String razaoSocial;
	
	@NotBlank
	@NotEmpty(message = "Nome Fantasia não pode ser vazio.")
	private String nomeFantasia;

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
	
	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
	
	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}
	
	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	@Override
	public String toString() {
		return "PessoaFisica[id=" + id + "," + "usuarioId=" + usuarioId + "," + "cnpj=" + cnpj +  "]";
	}
}
