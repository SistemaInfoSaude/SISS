package com.siss.api.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

//DTOS
import com.siss.api.dtos.UsuarioDto;
import com.siss.api.dtos.VeiculoDto;
import com.siss.api.dtos.RegraDto;
import com.siss.api.dtos.AlergiaDto;
import com.siss.api.dtos.CondicaoClinicaDto;
import com.siss.api.dtos.ContatoDto;
import com.siss.api.dtos.DoencaDto;
import com.siss.api.dtos.PessoaFisicaDto;

//Entities
import com.siss.api.entities.Usuario;
import com.siss.api.entities.Veiculo;
import com.siss.api.entities.Regra;
import com.siss.api.entities.TipoSanguineo;
import com.siss.api.entities.PessoaFisica;
import com.siss.api.entities.Alergia;
import com.siss.api.entities.CondicaoClinica;
import com.siss.api.entities.Contato;
import com.siss.api.entities.ConvenioMedico;
import com.siss.api.entities.Doenca;
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
	
	/* INICIO CONVERSÃO CONTATO */
	public static Contato Converter(ContatoDto contatoDto) {
		Contato contato = new Contato();
		Usuario usuario = new Usuario();

		if (contatoDto.getId() != null && contatoDto.getId() != "") {
			contato.setId(Integer.parseInt(contatoDto.getId()));
		}

		usuario.setId(Integer.parseInt(contatoDto.getUsuarioId()));
		
		contato.setUsuario(usuario);
		contato.setCelular(contatoDto.getCelular());
		contato.setTelefone(contatoDto.getTelefone());
		contato.setNome(contatoDto.getNome());
		
		return contato;
	}
	
	public static ContatoDto Converter(Contato contato) {
		ContatoDto contatoDto = new ContatoDto();

		contatoDto.setId(Integer.toString(contato.getId()));
		contatoDto.setUsuarioId(Integer.toString(contato.getUsuario().getId()));
		contatoDto.setCelular(contato.getCelular());
		contatoDto.setTelefone(contato.getTelefone());
		contatoDto.setNome(contato.getNome());
		
		return contatoDto;
	}
	/* FIM CONVERSÃO CONTATO */
	
	/* INICIO CONVERSÃO VEICULO */
	public static Veiculo Converter(VeiculoDto veiculoDto) {
		Veiculo veiculo= new Veiculo();
		Usuario usuario = new Usuario();

		if (veiculoDto.getId() != null && veiculoDto.getId() != "") {
			veiculo.setId(Integer.parseInt(veiculoDto.getId()));
		}

		usuario.setId(Integer.parseInt(veiculoDto.getUsuarioId()));
		
		veiculo.setUsuario(usuario);
		veiculo.setMarca(veiculoDto.getMarca());
		veiculo.setModelo(veiculoDto.getModelo());
		veiculo.setPlaca(veiculoDto.getPlaca());
		veiculo.setRenavam(veiculoDto.getRenavam());
		veiculo.setInformacoesAdicionais(veiculoDto.getInformacoesAdicionais());
		
		return veiculo;
	}
	
	public static VeiculoDto Converter(Veiculo veiculo) {
		VeiculoDto veiculoDto = new VeiculoDto();

		veiculoDto.setId(Integer.toString(veiculo.getId()));
		veiculoDto.setUsuarioId(Integer.toString(veiculo.getUsuario().getId()));
		veiculoDto.setMarca(veiculo.getMarca());
		veiculoDto.setModelo(veiculo.getModelo());
		veiculoDto.setPlaca(veiculo.getPlaca());
		veiculoDto.setRenavam(veiculo.getRenavam());
		veiculoDto.setInformacoesAdicionais(veiculo.getInformacoesAdicionais());
		
		return veiculoDto;
	}
	/* FIM CONVERSÃO VEICULO */
	
	/* INICIO CONVERSÃO CONDICAO CLINICA */
	public static CondicaoClinica Converter(CondicaoClinicaDto condicaoClinicaDto) {
		CondicaoClinica condicaoClinica = new CondicaoClinica();
		TipoSanguineo tipoSanguineo = new TipoSanguineo();
		Usuario usuario = new Usuario();

		if (condicaoClinicaDto.getId() != null && condicaoClinicaDto.getId() != "") {
			condicaoClinica.setId(Integer.parseInt(condicaoClinicaDto.getId()));
		}

		usuario.setId(Integer.parseInt(condicaoClinicaDto.getUsuarioId()));
		tipoSanguineo.setId(Integer.parseInt(condicaoClinicaDto.getTipoSanguineoId()));
		
		condicaoClinica.setUsuario(usuario);
		condicaoClinica.setTipoSanguineo(tipoSanguineo);
		condicaoClinica.setInformacaoAdicional(condicaoClinicaDto.getInformacaoAdicional());
		
		return condicaoClinica;
	}
	
	public static CondicaoClinicaDto Converter(CondicaoClinica condicaoClinica) {
		CondicaoClinicaDto condicaoClinicaDto = new CondicaoClinicaDto();

		condicaoClinicaDto.setId(Integer.toString(condicaoClinica.getId()));
		condicaoClinicaDto.setUsuarioId(Integer.toString(condicaoClinica.getUsuario().getId()));
		condicaoClinicaDto.setTipoSanguineoId(String.valueOf(condicaoClinica.getTipoSanguineo().getId()));
		condicaoClinicaDto.setInformacaoAdicional(condicaoClinica.geInformacaoAdicional());
		
		return condicaoClinicaDto;
	}
	/* FIM CONVERSÃO CONDICAO CLINICA */
	
	/* INCIO CONVERSÃO DOENCA */
	public static Doenca Converter(DoencaDto doencaDto) {
		Doenca doenca = new Doenca();
		CondicaoClinica condicaoClinica = new CondicaoClinica();

		if (doencaDto.getId() != null && doencaDto.getId() != "") {
			doenca.setId(Integer.parseInt(doencaDto.getId()));
		}

		condicaoClinica.setId(Integer.parseInt(doencaDto.getCondicaoClinicaId()));
		
		doenca.setCondicaoClinica(condicaoClinica);
		doenca.setTipo(doencaDto.getTipo());
		
		return doenca;
	}
	
	public static DoencaDto Converter(Doenca doenca) {
		DoencaDto doencaDto = new DoencaDto();

		doencaDto.setId(Integer.toString(doenca.getId()));
		doencaDto.setCondicaoClinicaId(String.valueOf(doenca.getCondicaoClinica().getId()));
		doencaDto.setTipo(doenca.getTipo());
		
		return doencaDto;
	}
	/* FIM CONVERSÃO DOENCA */
	
	/* INCIO CONVERSÃO ALERGIA */
	public static Alergia Converter(AlergiaDto alergiaDto) {
		Alergia alergia = new Alergia();
		CondicaoClinica condicaoClinica = new CondicaoClinica();

		if (alergiaDto.getId() != null && alergiaDto.getId() != "") {
			alergia.setId(Integer.parseInt(alergiaDto.getId()));
		}

		condicaoClinica.setId(Integer.parseInt(alergiaDto.getCondicaoClinicaId()));
		
		alergia.setCondicaoClinica(condicaoClinica);
		alergia.setTipo(alergiaDto.getTipo());
		
		return alergia;
	}
	
	public static AlergiaDto Converter(Alergia alergia) {
		AlergiaDto alergiaDto = new AlergiaDto();

		alergiaDto .setId(Integer.toString(alergia.getId()));
		alergiaDto .setCondicaoClinicaId(String.valueOf(alergia.getCondicaoClinica().getId()));
		alergiaDto .setTipo(alergia.getTipo());
		
		return alergiaDto;
	}
	/* FIM CONVERSÃO ALERGIA */
	
	public static Date parseDate(String dateValue) throws ParseException {
		return new SimpleDateFormat("yyyy-MM-dd").parse(dateValue);
	}
}
