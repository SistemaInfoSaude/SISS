package com.siss.api.dtos;

import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CondicaoClinicaDto {
	private String id;	

	@NotEmpty(message = "usuarioId não pode ser vazio.")
	@Length(min = 1, message = "usuarioId deve conter ao menos 1 caractere.")
	private String usuarioId;

	@NotEmpty(message = "tipoSanguineoId não pode ser vazio.")
	@Length(min = 1, message = "tipoSanguineoId deve conter ao menos 1 caractere.")
	private String tipoSanguineoId;

	private String informacaoAdicional;

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

	public String getTipoSanguineoId() {
		return tipoSanguineoId;
	}

	public void setTipoSanguineoId(String tipoSanguineoId) {
		this.tipoSanguineoId = tipoSanguineoId;
	}

	public String getInformacaoAdicional() {
		return informacaoAdicional;
	}

	public void setInformacaoAdicional(String informacaoAdicional) {
		this.informacaoAdicional = informacaoAdicional;
	}

	@Override
	public String toString() {
		return "CondicaoClinica[id=" + id + "," + "usuarioId=" + usuarioId + "," + "tipoSanguineoId=" + tipoSanguineoId
				+ "," + "informacaoAdicional=" + informacaoAdicional + "]";
	}
}
