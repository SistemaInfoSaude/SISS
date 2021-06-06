package com.siss.api.dtos;

import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class DoencaDto {
	private String id;

	@NotEmpty(message = "condicaoClinicaId não pode ser vazio.")
	@Length(min = 1, message = "condicaoClinicaId deve conter ao menos 1 caractere.")
	private String condicaoClinicaId;

	@NotEmpty(message = "tipo não pode ser vazio.")
	@Length(min = 1, message = "tipo deve conter ao menos 1 caractere.")
	private String tipo;
	
	private String dataCadastro;
	
	private String dataAtualizacao;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCondicaoClinicaId() {
		return condicaoClinicaId;
	}

	public void setCondicaoClinicaId(String condicaoClinicaId) {
		this.condicaoClinicaId = condicaoClinicaId;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(String dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
	
	public String getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(String dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}
	
	@Override
	public String toString() {
		return "Doenca[id=" + id + "," + "condicaoClinicaId=" + condicaoClinicaId + "," + "tipo=" + tipo + "]";
	}
}
