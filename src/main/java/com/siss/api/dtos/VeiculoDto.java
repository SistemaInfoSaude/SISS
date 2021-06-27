package com.siss.api.dtos;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class VeiculoDto {
	private String id;

	@NotBlank
	@NotEmpty(message = "pessoaFisicaId não pode ser vazio.")
	@Length(min = 1, message = "pessoaFisicaId deve conter ao menos 1 caractere.")
	private String pessoaFisicaId;

	@NotEmpty(message = "marca não pode ser vazio.")
	@Length(min = 3, max = 45, message = "marca deve conter no máximo 45 caracteres.")
	private String marca;

	@NotEmpty(message = "modelo não pode ser vazio.")
	@Length(min = 3, max = 45, message = "modelo deve conter no máximo 45 caracteres.")
	private String modelo;

	@NotEmpty(message = "placa não pode ser vazio.")
	@Length(min = 7, max = 8, message = "placa deve conter 7 caracteres para novos modelos (AAA9A99) ou 8 caracteres para modelos antigos (AAA-9999).")
	private String placa;

	@Length(min = 11, max = 11, message = "renavam deve conter 11 caracteres.")
	private String renavam;

	@NotEmpty(message = "cor não pode ser vazio.")
	@Length(min = 3, max = 50, message = "cor deve conter de 3 a 50 caracteres.")
	private String cor;

	private String informacoesAdicionais;

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

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getRenavam() {
		return renavam;
	}

	public void setRenavam(String renavam) {
		this.renavam = renavam;
	}
	
	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}

	public String getInformacoesAdicionais() {
		return informacoesAdicionais;
	}

	public void setInformacoesAdicionais(String informacoesAdicionais) {
		this.informacoesAdicionais = informacoesAdicionais;
	}

	@Override
	public String toString() {
		return "Veiculo[id=" + id + "," + "pessoaFisicaId=" + pessoaFisicaId + "," + "marca=" + marca + "," + "modelo="
				+ modelo + "," + "placa=" + placa + "," + "renavam=" + renavam + "," + "informacoesAdicionais="
				+ informacoesAdicionais + "]";
	}
}
