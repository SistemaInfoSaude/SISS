package com.siss.api.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

//DTOS
import com.siss.api.dtos.UsuarioDto;
import com.siss.api.dtos.RegraDto;

//Entities
import com.siss.api.entities.Usuario;
import com.siss.api.entities.Regra;

public class ConversaoUtils {

	public static Usuario Converter(UsuarioDto usuarioDto) {
		Usuario usuario = new Usuario();
		
		if (usuarioDto.getId() != null && usuarioDto.getId() != "") {
			usuario.setId(Integer.parseInt(usuarioDto.getId()));
		}
		
		usuario.setUsuario(usuarioDto.getUsuario());
		if (usuarioDto.getRegras() != null && usuarioDto.getRegras().size() > 0) {
			usuario.setRegras(new ArrayList<Regra>());
			for (RegraDto regraDto : usuarioDto.getRegras()) {
				Regra regra = new Regra();
				regra.setNome(regraDto.getNome());
				usuario.getRegras().add(regra);
			}
		}
		
		return usuario;
	}

	public static UsuarioDto Converter(Usuario usuario) {
		UsuarioDto usuarioDto = new UsuarioDto();
		
		usuarioDto.setId(Integer.toString(usuario.getId()));
		usuarioDto.setUsuario(usuario.getUsuario());
		
		if (usuario.getRegras() != null) {
			usuarioDto.setRegras(new ArrayList<RegraDto>());
			
			for (int i = 0; i < usuario.getRegras().size(); i++) {
				RegraDto regraDto = new RegraDto();
				regraDto.setNome(usuario.getRegras().get(i).getNome());
				regraDto.setDescricao(usuario.getRegras().get(i).getDescricao());
				regraDto.setAtivo(usuario.getRegras().get(i).getAtivo());
				usuarioDto.getRegras().add(regraDto);
			}
		}
		return usuarioDto;
	}
}
