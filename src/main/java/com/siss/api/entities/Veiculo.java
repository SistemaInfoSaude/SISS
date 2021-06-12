package com.siss.api.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "veiculo")
public class Veiculo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@JsonBackReference
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "pessoa_fisica_id", nullable = false, updatable = false, insertable = true)
	private PessoaFisica pessoaFisica;

	@Column(name = "Marca", nullable = false, length = 45)
	private String marca;

	@Column(name = "modelo", nullable = false, length = 45)
	private String modelo;

	@Column(name = "placa", nullable = false, length = 8)
	private String placa;

	@Column(name = "renavam", nullable = false, length = 11)
	private String renavam;
	
	@Column(name = "cor", nullable = false, length = 45)
	private String cor;

	@Column(name = "informacoes_Adicionais", nullable = true)
	private String informacoesAdicionais;

	@Column(name = "data_Cadastro", nullable = false)
	private Date dataCadastro;

	@Column(name = "data_Alteracao", nullable = false)
	private Date dataAlteracao;

	public PessoaFisica getPessoaFisica() {
		return pessoaFisica;
	}

	public void setPessoaFisica(PessoaFisica pessoaFisica) {
		this.pessoaFisica = pessoaFisica;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	@PreUpdate
	public void preUpdate() {
		this.setDataAlteracao(new Date());
	}

	@PrePersist
	public void prePersist() {
		this.setDataCadastro(new Date());
		this.setDataAlteracao(new Date());
	}

	@Override
	public String toString() {
		return "Veiculo[" + "idPessoaFisica=" + pessoaFisica.getId() + "," + "marca=" + marca + "," + "modelo=" + modelo
				+ "," + "placa=" + placa + "," + "renavam=" + renavam + "," + "informacoesAdicionais="
				+ informacoesAdicionais + "," + "dataCadastro=" + dataCadastro + "," + "dataAlteracao=" + dataAlteracao
				+ "]";
	}
}
