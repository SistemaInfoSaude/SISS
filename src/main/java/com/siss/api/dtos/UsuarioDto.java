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
	
	private String executante;

	private List<RegraDto> regras;
	
	private PessoaFisicaDto pessoaFisica;
	
	private PessoaJuridicaDto pessoaJuridica;

	private CondicaoClinicaDto condicaoClinica;
	
	private List<VeiculoDto> veiculos;
	
	private List<ContatoDto> contatos;

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
	
	public String getExecutante() {
		return executante;
	}

	public void setExecutante(String executante) {
		this.executante = executante;
	}
	
	public PessoaFisicaDto getPessoaFisica() {
		return pessoaFisica;
	}
	
	public void setPessoaFisica(PessoaFisicaDto pessoaFisica) {
		this.pessoaFisica = pessoaFisica;
	}

	public PessoaJuridicaDto getPessoaJuridica() {
		return pessoaJuridica;
	}
	
	public void setPessoaJuridica(PessoaJuridicaDto pessoaJuridica) {
		this.pessoaJuridica = pessoaJuridica;
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
