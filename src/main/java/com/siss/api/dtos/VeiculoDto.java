package com.siss.api.dtos;

import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class VeiculoDto {
	private String id;

	@NotEmpty(message = "usuarioId não pode ser vazio.")
	@Length(min = 1, message = "usuarioId deve conter ao menos 1 caractere.")
	private String usuarioId;

	@NotEmpty(message = "marca não pode ser vazio.")
	@Length(min = 3, max = 45, message = "marca deve conter no máximo 45 caracteres.")
	private String marca;

	@NotEmpty(message = "modelo não pode ser vazio.")
	@Length(min = 3, max = 45, message = "modelo deve conter no máximo 45 caracteres.")
	private String modelo;

	@NotEmpty(message = "placa não pode ser vazio.")
	@Length(min = 8, max = 8, message = "placa deve conter 8 caracteres.")
	private String placa;

	@NotEmpty(message = "renavam não pode ser vazio.")
	@Length(min = 11, max = 11, message = "renavam deve conter 11 caracteres.")
	private String renavam;

	private String informacoesAdicionais;

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

	public String getInformacoesAdicionais() {
		return informacoesAdicionais;
	}

	public void setInformacoesAdicionais(String informacoesAdicionais) {
		this.informacoesAdicionais = informacoesAdicionais;
	}

	@Override
	public String toString() {
		return "Veiculo[id=" + id + "," + "usuarioId=" + usuarioId + "," + "marca=" + marca + "," + "modelo=" + modelo
				+ "," + "placa=" + placa + "," + "renavam=" + renavam + "," + "informacoesAdicionais="
				+ informacoesAdicionais + "]";
	}
}
