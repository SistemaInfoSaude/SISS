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
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "pessoaFisica")
public class PessoaFisica implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@JsonBackReference
	@OneToOne(fetch = FetchType.EAGER)
	private Usuario usuario;

	@JsonBackReference
	@OneToOne(fetch = FetchType.EAGER)
	private ConvenioMedico convenioMedico;

	@Column(name = "cpf", nullable = false, length = 11)
	private String cpf;

	@Column(name = "data_Nascimento", nullable = false)
	private Date dataNascimento;

	@Column(name = "telefone", nullable = true, length = 10)
	private String telefone;

	@Column(name = "celular", nullable = true, length = 11)
	private String celular;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public ConvenioMedico getConvenioMedico() {
		return convenioMedico;
	}

	public void setConvenioMedico(ConvenioMedico convenioMedico) {
		this.convenioMedico = convenioMedico;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
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

	@Override
	public String toString() {
		return "PessoaFisica[" + "id=" + id + "," + "idUsuario=" + usuario.getId() + "," + "cpf=" + cpf + ","
				+ "convenioMedicoId=" + convenioMedico.getId() + "," + "dataNascimento=" + dataNascimento + ","
				+ "celular=" + celular + "," + "telefone=" + telefone + "]";
	}
}
