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

	@NotEmpty(message = "pessoaFisicaId não pode ser vazio.")
	@Length(min = 1, message = "pessoaFisicaId deve conter ao menos 1 caractere.")
	private String pessoaFisicaId;

	private String convenioMedico;

	private String tipoSanguineo;

	private List<DoencaDto> doencas;

	private List<AlergiaDto> alergias;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPessoaFisicaId() {
		return pessoaFisicaId;
	}

	public void setPessoaFisicaId(String pessoaFisicaId) {
		this.pessoaFisicaId = pessoaFisicaId;
	}

	public String getTipoSanguineo() {
		return tipoSanguineo;
	}

	public void setTipoSanguineo(String tipoSanguineo) {
		this.tipoSanguineo = tipoSanguineo;
	}

	public String getConvenioMedico() {
		return convenioMedico;
	}

	public void setConvenioMedico(String convenioMedico) {
		this.convenioMedico = convenioMedico;
	}

	public List<DoencaDto> getDoencas() {
		return doencas;
	}

	public void setDoencas(List<DoencaDto> doencas) {
		this.doencas = doencas;
	}

	public List<AlergiaDto> getAlergias() {
		return alergias;
	}

	public void setAlergias(List<AlergiaDto> alergias) {
		this.alergias = alergias;
	}

	@Override
	public String toString() {
		return "CondicaoClinica[id=" + id + "," + "pessoaFisicaId=" + pessoaFisicaId + "," + "tipoSanguineo="
				+ tipoSanguineo + "," + "convenioMedico=" + convenioMedico + "]";
	}
}
