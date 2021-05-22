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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "contato")
public class Contato implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@JsonBackReference
	@ManyToOne(fetch = FetchType.EAGER)
	private PessoaFisica pessoaFisica;

	@Column(name = "nome", nullable = false, length = 100)
	private String nome;

	@Column(name = "parentesco", nullable = false)
	private String parentesco;

	@Column(name = "telefone", nullable = false, length = 10)
	private String telefone;

	@Column(name = "celular", nullable = false, length = 11)
	private String celular;

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

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getParentesco() {
		return parentesco;
	}

	public void setParentesco(String parentesco) {
		this.parentesco = parentesco;
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
		return "Contato[" + "id=" + id + "," + "idPessoaFisica=" + pessoaFisica.getId() + "," + "nome=" + nome + ","
				+ "telefone=" + telefone + "," + "celular=" + celular + "," + "dataCadastro=" + dataCadastro + ","
				+ "dataAlteracao=" + dataAlteracao + "]";
	}
}
