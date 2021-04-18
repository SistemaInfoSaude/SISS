package com.siss.api.dtos;

import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PessoaFisicaInformacoesDto {
	private String id;

	private String usuarioId;

	private String cpf;
	
	private String dataNascimento;

	private String telefone;

	private String celular;
	
	private ConvenioMedicoDto convenioMedico;
	
	private CondicaoClinicaDto condicaoClinica;
	
	private List<ContatoDto> contatos;
	
	private List<VeiculoDto> veiculos;

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

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
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

	public ConvenioMedicoDto getConvenioMedico() {
		return convenioMedico;
	}

	public void setConvenioMedico(ConvenioMedicoDto convenioMedico) {
		this.convenioMedico = convenioMedico;
	}
	
	public CondicaoClinicaDto getCondicaoClinica() {
		return condicaoClinica;
	}

	public void setCondicaoClinica(CondicaoClinicaDto condicaoClinica) {
		this.condicaoClinica = condicaoClinica;
	}
	
	public List<ContatoDto> getContatos() {
		return contatos;
	}

	public void setContatos(List<ContatoDto> contatos) {
		this.contatos = contatos;
	}
	
	public List<VeiculoDto> getVeiculos() {
		return veiculos;
	}

	public void setVeiculos(List<VeiculoDto> veiculos) {
		this.veiculos = veiculos;
	}
}
