package com.siss.api.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.JoinColumn;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "usuario", nullable = false, length = 100)
	private String usuario;

	@Column(name = "email", nullable = false, length = 150)
	private String email;

	@Column(name = "senha", nullable = false, length = 255)
	private String senha;

	@Column(name = "data_Cadastro", nullable = false)
	private Date dataCadastro;

	@Column(name = "data_Alteracao", nullable = false)
	private Date dataAlteracao;

	@Transient
	private Boolean executante;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "Usuario_Regra", joinColumns = { @JoinColumn(name = "usuario_id") }, inverseJoinColumns = {
			@JoinColumn(name = "regra_id") })
	private List<Regra> regras;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public List<Regra> getRegras() {
		return regras;
	}

	public void setRegras(List<Regra> regras) {
		this.regras = regras;
	}

	public Boolean getExecutante() {
		return executante;
	}

	public void setExecutante(Boolean executante) {
		this.executante = executante;
	}

	@PreUpdate
	public void preUpdate() {
		dataCadastro = new Date();
	}

	@PrePersist
	public void prePersist() {
		dataAlteracao = new Date();
	}

	@Override
	public String toString() {
		return "Usuario[" + "id=" + id + "," + "usuario=" + usuario + "," + "email=" + email + "," + "dataCadastro="
				+ dataCadastro + "," + "dataAlteracao=" + dataAlteracao + "]";
	}
}
