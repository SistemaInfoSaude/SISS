package com.siss.api.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class RegistroValidacaoDto {
	
	private String username;
	private String email;
	private String cpf;
	private String rg;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

}
