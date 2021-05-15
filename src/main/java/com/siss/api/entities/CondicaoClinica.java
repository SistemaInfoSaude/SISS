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
@Table(name = "condicaoClinica")
public class CondicaoClinica implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@JsonBackReference
	@OneToOne(fetch = FetchType.EAGER)
	private PessoaFisica pessoaFisica;

	@JsonManagedReference
	@OneToMany(mappedBy = "condicaoClinica", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Doenca> doencas;

	@JsonManagedReference
	@OneToMany(mappedBy = "condicaoClinica", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Alergia> alergias;

	@Column(name = "tipo_Sanguineo", nullable = true)
	private String tipoSanguineo;
	
	@Column(name = "informacao_Adicional", nullable = true)
	private String informacaoAdicional;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public PessoaFisica getPessoaFisica() {
		return pessoaFisica;
	}

	public void setPessoaFisica(PessoaFisica pessoaFisica) {
		this.pessoaFisica = pessoaFisica;
	}

	public String getTipoSanguineo() {
		return tipoSanguineo;
	}

	public void setTipoSanguineo(String tipoSanguineo) {
		this.tipoSanguineo = tipoSanguineo;
	}

	public List<Doenca> getDoencas() {
		return doencas;
	}

	public void setDoencas(List<Doenca> doencas) {
		this.doencas = doencas;
	}

	public List<Alergia> getAlergias() {
		return alergias;
	}

	public void setAlergias(List<Alergia> alergias) {
		this.alergias = alergias;
	}

	public String geInformacaoAdicional() {
		return informacaoAdicional;
	}

	public void setInformacaoAdicional(String informacaoAdicional) {
		this.informacaoAdicional = informacaoAdicional;
	}

	@Override
	public String toString() {
		return "CondicaoClinica[id=" + id + "idPf=" + pessoaFisica.getId() + "," + "tipoSanguineo="
				+ tipoSanguineo + "," + "informacaoAdicional=" + informacaoAdicional + "]";
	}
}
