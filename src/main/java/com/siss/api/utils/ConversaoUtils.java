package com.siss.api.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

//DTOS
import com.siss.api.dtos.UsuarioDto;
import com.siss.api.dtos.RegraDto;
import com.siss.api.dtos.PessoaFisicaDto;

//Entities
import com.siss.api.entities.Usuario;
import com.siss.api.entities.Regra;
import com.siss.api.entities.PessoaFisica;
import com.siss.api.entities.ConvenioMedico;

//Utils
import com.siss.api.security.utils.JwtTokenUtil;

public class ConversaoUtils {

	/* INICIO CONVERSÃO USUÁRIO */

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

		if (usuarioDto.getSenha() != null) {
			usuario.setSenha(usuarioDto.getSenha());
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
	/* FIM CONVERSÃO USUÁRIO */

	/* INICIO CONVERSÃO PESSOA FÍSICA */
	public static PessoaFisica Converter(PessoaFisicaDto pessoaFisicaDto) throws ParseException {
		PessoaFisica pessoaFisica = new PessoaFisica();
		Usuario usuario = new Usuario();
		ConvenioMedico convenioMedico = new ConvenioMedico();

		if (pessoaFisicaDto.getId() != null && pessoaFisicaDto.getId() != "") {
			pessoaFisica.setId(Integer.parseInt(pessoaFisicaDto.getId()));
		}

		usuario.setId(Integer.parseInt(pessoaFisicaDto.getUsuarioId()));
		convenioMedico.setId(Integer.parseInt(pessoaFisicaDto.getConvenioMedicoId()));
		
		pessoaFisica.setUsuario(usuario);
		pessoaFisica.setConvenioMedico(convenioMedico);
		pessoaFisica.setDataNascimento(parseDate(pessoaFisicaDto.getDataNascimento()));
		pessoaFisica.setCpf(pessoaFisicaDto.getCpf());
		pessoaFisica.setCelular(pessoaFisicaDto.getCelular());
		pessoaFisica.setTelefone(pessoaFisicaDto.getTelefone());

		return pessoaFisica;
	}

	public static PessoaFisicaDto Converter(PessoaFisica pessoaFisica) {
		PessoaFisicaDto pessoaFisicaDto = new PessoaFisicaDto();

		pessoaFisicaDto.setId(Integer.toString(pessoaFisica.getId()));
		pessoaFisicaDto.setUsuarioId(Integer.toString(pessoaFisica.getUsuario().getId()));
		pessoaFisicaDto.setConvenioMedicoId(Integer.toString(pessoaFisica.getConvenioMedico().getId()));
		pessoaFisicaDto.setCpf(pessoaFisica.getCpf());
		pessoaFisicaDto.setDataNascimento(pessoaFisica.getDataNascimento().toString());
		pessoaFisicaDto.setTelefone(pessoaFisica.getTelefone());
		pessoaFisicaDto.setCelular(pessoaFisica.getCelular());
		
		return pessoaFisicaDto;
	}
	/* FIM CONVERSÃO PESSOA FÍSICA */
	
	public static Date parseDate(String dateValue) throws ParseException {
		return new SimpleDateFormat("yyyy-MM-dd").parse(dateValue);
	}
}
