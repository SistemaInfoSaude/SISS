package com.siss.api.dtos;

import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PessoaFisicaDto {
	private String id;

	@NotEmpty(message = "usuarioId não pode ser vazio.")
	@Length(min = 1, message = "usuarioId deve conter ao menos 1 caractere.")
	private String usuarioId;

	@NotEmpty(message = "CPF não pode ser vazio.")
	@CPF(message = "CPF inválido.")
	private String cpf;

	@NotEmpty(message = "RG não pode ser vazio.")
	@Length(min = 9, max = 9, message = "RG deve conter 9 caracteres.")
	private String rg;

	@NotEmpty(message = "dataNascimento não pode ser vazio.")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private String dataNascimento;

	@Length(min = 10, max = 10, message = "telefone deve conter 10 caracteres.")
	private String telefone;

	@Length(min = 11, max = 11, message = "celular deve conter 11 caracteres.")
	private String celular;

	private ConvenioMedicoDto convenioMedico;

	private CondicaoClinicaDto condicaoClinica;

	private List<VeiculoDto> veiculos;

	private List<ContatoDto> contatos;

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

	public ConvenioMedicoDto getConvenioMedico() {
		return convenioMedico;
	}

	public void setConvenioMedico(ConvenioMedicoDto convenioMedico) {
		this.convenioMedico = convenioMedico;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(String dataNascimento) {
		this.dataNascimento = dataNascimento;
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

	public CondicaoClinicaDto getCondicaoClinica() {
		return condicaoClinica;
	}

	public void setCondicaoClinica(CondicaoClinicaDto condicaoClinica) {
		this.condicaoClinica = condicaoClinica;
	}

	public List<VeiculoDto> getVeiculos() {
		return veiculos;
	}

	public void setVeiculos(List<VeiculoDto> veiculos) {
		this.veiculos = veiculos;
	}

	public List<ContatoDto> getContatos() {
		return contatos;
	}

	public void setContatos(List<ContatoDto> contatos) {
		this.contatos = contatos;
	}

	@Override
	public String toString() {
		return "PessoaFisica[id=" + id + "," + "usuarioId=" + usuarioId + "," + "cpf=" + cpf + "," + "dataNascimento="
				+ dataNascimento + "," + "telefone=" + telefone + "," + "celular=" + celular + "]";
	}
}
