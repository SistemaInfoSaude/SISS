package com.siss.api.security.dtos;

import com.siss.api.entities.Usuario;

public class TokenDto {
	private String token;
	
	private String usuarioId;
	
	private String executante;
	
	private String pfExists;

	public TokenDto() {
	}

	public TokenDto(String token) {
		this.token = token;
	}
	
	public TokenDto(String token, Usuario usr, Boolean pfStatus) {
		this.token = token;
		this.usuarioId = Integer.toString(usr.getId());
		
		if(usr.getRegras().get(0).getNome().equals("ROLE_EXEC_USUARIO")) {
			this.executante = "1";
		}else {
			this.executante = "0";
		}
		
		if(pfStatus) {
			this.pfExists = "1";
		}else {
			this.pfExists = "0";
		}
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public String getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(String usuarioId) {
		this.usuarioId = usuarioId;
	}
	
	public String getExecutante() {
		return executante;
	}

	public void setExecutante(String executante) {
		this.executante = executante;
	}
	
	public String getPfExists() {
		return pfExists;
	}

	public void setPfExists(String pfExists) {
		this.pfExists = pfExists;
	}
}
