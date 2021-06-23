package com.siss.api.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
import com.siss.api.dtos.PessoaFisicaInfoDto;
import com.siss.api.dtos.PessoaJuridicaDto;
//Entities
import com.siss.api.entities.Usuario;
import com.siss.api.entities.Veiculo;
import com.siss.api.entities.Regra;
import com.siss.api.entities.TipoSanguineo;
import com.siss.api.entities.PessoaFisica;
import com.siss.api.entities.PessoaJuridica;
import com.siss.api.entities.Alergia;
import com.siss.api.entities.CondicaoClinica;
import com.siss.api.entities.Contato;
import com.siss.api.entities.Doenca;
//Utils
import com.siss.api.security.utils.JwtTokenUtil;

public class ConversaoUtils {

	/* INICIO CONVERSÃO USUÁRIO */
	public static Usuario Converter(UsuarioDto usuarioDto) throws ParseException {
		Usuario usuario = new Usuario();

		if (usuarioDto.getId() != null && usuarioDto.getId() != "") {
			usuario.setId(Integer.parseInt(usuarioDto.getId()));
		}

		usuario.setUsuario(usuarioDto.getUsuario());
		usuario.setEmail(usuarioDto.getEmail());

		if (usuarioDto.getRegras() != null && usuarioDto.getRegras().size() > 0) {
			usuario.setRegras(new ArrayList<Regra>());
			for (RegraDto regraDto : usuarioDto.getRegras()) {
				Regra regra = new Regra();
				regra.setNome(regraDto.getNome());
				usuario.getRegras().add(regra);
			}
		}

		if (usuarioDto.getExecutante() != null && usuarioDto.getExecutante() != ""
				&& Integer.parseInt(usuarioDto.getExecutante()) == 1) {
			usuario.setExecutante(true);
		} else {
			usuario.setExecutante(false);
		}

		if (usuarioDto.getSenha() != null) {
			usuario.setSenha(usuarioDto.getSenha());
		}

		return usuario;
	}

	public static UsuarioDto Converter(Usuario usuario) {
		UsuarioDto usuarioDto = new UsuarioDto();
		Boolean regraExecutante = false;

		usuarioDto.setId(Integer.toString(usuario.getId()));
		usuarioDto.setUsuario(usuario.getUsuario());
		usuarioDto.setEmail(usuario.getEmail());

		if (usuario.getRegras() != null) {
			usuarioDto.setRegras(new ArrayList<RegraDto>());

			for (int i = 0; i < usuario.getRegras().size(); i++) {
				if (usuario.getRegras().get(i).getNome() == "ROLE_EXEC_USUARIO") {
					regraExecutante = true;
				}
				RegraDto regraDto = new RegraDto();
				regraDto.setNome(usuario.getRegras().get(i).getNome());
				regraDto.setDescricao(usuario.getRegras().get(i).getDescricao());
				regraDto.setAtivo(usuario.getRegras().get(i).getAtivo());
				usuarioDto.getRegras().add(regraDto);
			}
		}

		if (regraExecutante) {
			usuarioDto.setExecutante("1");
		}else {
			usuarioDto.setExecutante("0");
		}

		return usuarioDto;
	}
	/* FIM CONVERSÃO USUÁRIO */

	/* INICIO CONVERSÃO PESSOA FÍSICA */
	public static PessoaFisica Converter(PessoaFisicaInfoDto pessoaFisicaInfoDto) throws ParseException {
		PessoaFisica pessoaFisica = new PessoaFisica();
		Usuario usuario = new Usuario();

		if (pessoaFisicaInfoDto.getId() != null && pessoaFisicaInfoDto.getId() != "") {
			pessoaFisica.setId(Integer.parseInt(pessoaFisicaInfoDto.getId()));
		}

		if (pessoaFisicaInfoDto.getCondicaoClinica() != null
				&& pessoaFisicaInfoDto.getCondicaoClinica().getId() != null) {
			pessoaFisica.setCondicaoClinica(Converter(pessoaFisicaInfoDto.getCondicaoClinica()));
		}

		if (pessoaFisicaInfoDto.getContatos() != null && pessoaFisicaInfoDto.getContatos().size() > 0) {
			pessoaFisica.setContatos(new ArrayList<Contato>());
			for (ContatoDto contatoDto : pessoaFisicaInfoDto.getContatos()) {
				Contato contato = new Contato();
				contato.setCelular(contatoDto.getCelular());
				contato.setTelefone(contatoDto.getTelefone());
				contato.setNome(contatoDto.getNome());
				pessoaFisica.getContatos().add(contato);
			}
		}

		if (pessoaFisicaInfoDto.getVeiculos() != null && pessoaFisicaInfoDto.getVeiculos().size() > 0) {
			pessoaFisica.setVeiculos(new ArrayList<Veiculo>());
			for (VeiculoDto veiculoDto : pessoaFisicaInfoDto.getVeiculos()) {
				Veiculo veiculo = new Veiculo();
				veiculo.setMarca(veiculoDto.getMarca());
				veiculo.setModelo(veiculoDto.getModelo());
				veiculo.setPlaca(veiculoDto.getPlaca());
				veiculo.setRenavam(veiculoDto.getRenavam());
				veiculo.setCor(veiculoDto.getCor());
				veiculo.setInformacoesAdicionais(veiculoDto.getInformacoesAdicionais());
				pessoaFisica.getVeiculos().add(veiculo);
			}
		}

		usuario.setId(Integer.parseInt(pessoaFisicaInfoDto.getUsuarioId()));

		Date dataNascimento = new SimpleDateFormat("yyyy-MM-dd").parse(pessoaFisicaInfoDto.getDataNascimento());
		
		pessoaFisica.setUsuario(usuario);
		pessoaFisica.setNome(pessoaFisicaInfoDto.getNome());
		pessoaFisica.setDataNascimento(dataNascimento);
		pessoaFisica.setCpf(pessoaFisicaInfoDto.getCpf());
		pessoaFisica.setRg(pessoaFisicaInfoDto.getRg());
		pessoaFisica.setCelular(pessoaFisicaInfoDto.getCelular());
		pessoaFisica.setTelefone(pessoaFisicaInfoDto.getTelefone());

		return pessoaFisica;
	}

	public static PessoaFisica Converter(PessoaFisicaDto pessoaFisicaDto) throws ParseException {
		PessoaFisica pessoaFisica = new PessoaFisica();
		Usuario usuario = new Usuario();

		if (pessoaFisicaDto.getId() != null && pessoaFisicaDto.getId() != "") {
			pessoaFisica.setId(Integer.parseInt(pessoaFisicaDto.getId()));
		}

		usuario.setId(Integer.parseInt(pessoaFisicaDto.getUsuarioId()));
		pessoaFisica.setUsuario(usuario);
		
		Date dataNascimento = new SimpleDateFormat("yyyy-MM-dd").parse(pessoaFisicaDto.getDataNascimento());

		pessoaFisica.setNome(pessoaFisicaDto.getNome());
		pessoaFisica.setDataNascimento(dataNascimento);
		pessoaFisica.setCpf(pessoaFisicaDto.getCpf());
		pessoaFisica.setRg(pessoaFisicaDto.getRg());
		pessoaFisica.setCelular(pessoaFisicaDto.getCelular());
		pessoaFisica.setTelefone(pessoaFisicaDto.getTelefone());

		return pessoaFisica;
	}

	public static PessoaFisicaInfoDto Converter(PessoaFisica pessoaFisica) throws ParseException {
		PessoaFisicaInfoDto pessoaFisicaDto = new PessoaFisicaInfoDto();

		if (pessoaFisica.getCondicaoClinica() != null && pessoaFisica.getCondicaoClinica().getId() > 0) {
			pessoaFisicaDto.setCondicaoClinica(Converter(pessoaFisica.getCondicaoClinica()));
		}

		if (pessoaFisica.getContatos() != null && pessoaFisica.getContatos().size() > 0) {
			pessoaFisicaDto.setContatos(new ArrayList<ContatoDto>());
			for (int i = 0; i < pessoaFisica.getContatos().size(); i++) {
				ContatoDto contatoDto = new ContatoDto();
				contatoDto.setId(Integer.toString(pessoaFisica.getContatos().get(i).getId()));
				contatoDto.setCelular(pessoaFisica.getContatos().get(i).getCelular());
				contatoDto.setTelefone(pessoaFisica.getContatos().get(i).getTelefone());
				contatoDto.setNome(pessoaFisica.getContatos().get(i).getNome());
				contatoDto.setParentesco(pessoaFisica.getContatos().get(i).getParentesco());
				pessoaFisicaDto.getContatos().add(contatoDto);
			}
		}

		if (pessoaFisica.getVeiculos() != null && pessoaFisica.getVeiculos().size() > 0) {
			pessoaFisicaDto.setVeiculos(new ArrayList<VeiculoDto>());
			for (int i = 0; i < pessoaFisica.getVeiculos().size(); i++) {
				VeiculoDto veiculoDto = new VeiculoDto();
				veiculoDto.setId(Integer.toString(pessoaFisica.getVeiculos().get(i).getId()));
				veiculoDto.setMarca(pessoaFisica.getVeiculos().get(i).getMarca());
				veiculoDto.setModelo(pessoaFisica.getVeiculos().get(i).getModelo());
				veiculoDto.setPlaca(pessoaFisica.getVeiculos().get(i).getPlaca());
				veiculoDto.setRenavam(pessoaFisica.getVeiculos().get(i).getRenavam());
				veiculoDto.setCor(pessoaFisica.getVeiculos().get(i).getCor());
				veiculoDto.setInformacoesAdicionais(pessoaFisica.getVeiculos().get(i).getInformacoesAdicionais());
				pessoaFisicaDto.getVeiculos().add(veiculoDto);
			}
		}

		pessoaFisicaDto.setId(Integer.toString(pessoaFisica.getId()));
		pessoaFisicaDto.setUsuarioId(Integer.toString(pessoaFisica.getUsuario().getId()));
		pessoaFisicaDto.setNome(pessoaFisica.getNome());
		pessoaFisicaDto.setCpf(pessoaFisica.getCpf());
		pessoaFisicaDto.setRg(pessoaFisica.getRg());
		pessoaFisicaDto.setDataNascimento(pessoaFisica.getDataNascimento().toString());
		pessoaFisicaDto.setTelefone(pessoaFisica.getTelefone());
		pessoaFisicaDto.setCelular(pessoaFisica.getCelular());

		return pessoaFisicaDto;
	}

	/* FIM CONVERSÃO PESSOA FÍSICA */

	/* INICIO CONVERSÃO PESSOA JRUDICA */
	public static PessoaJuridica Converter(PessoaJuridicaDto pessoaJuridicaDto) throws ParseException {
		PessoaJuridica pessoaJuridica = new PessoaJuridica();
		Usuario usuario = new Usuario();

		if (pessoaJuridicaDto.getId() != null && pessoaJuridicaDto.getId() != "") {
			pessoaJuridica.setId(Integer.parseInt(pessoaJuridicaDto.getId()));
		}

		usuario.setId(Integer.parseInt(pessoaJuridicaDto.getUsuarioId()));

		pessoaJuridica.setUsuario(usuario);
		pessoaJuridica.setCnpj(pessoaJuridicaDto.getCnpj());
		pessoaJuridica.setRazaoSocial(pessoaJuridicaDto.getRazaoSocial());
		pessoaJuridica.setNomeFantasia(pessoaJuridicaDto.getNomeFantasia());

		return pessoaJuridica;
	}

	public static PessoaJuridicaDto Converter(PessoaJuridica pessoaJuridica) {
		PessoaJuridicaDto pessoaJuridicaDto = new PessoaJuridicaDto();

		pessoaJuridicaDto.setId(Integer.toString(pessoaJuridica.getId()));
		pessoaJuridicaDto.setUsuarioId(Integer.toString(pessoaJuridica.getUsuario().getId()));
		pessoaJuridicaDto.setCnpj(pessoaJuridica.getCnpj());
		pessoaJuridicaDto.setRazaoSocial(pessoaJuridica.getRazaoSocial());
		pessoaJuridicaDto.setNomeFantasia(pessoaJuridica.getNomeFantasia());

		return pessoaJuridicaDto;
	}
	/* FIM CONVERSÃO PESSOA JURIDICA */

	/* INICIO CONVERSÃO CONTATO */
	public static Contato Converter(ContatoDto contatoDto) {
		Contato contato = new Contato();
		PessoaFisica pessoaFisica = new PessoaFisica();

		if (contatoDto.getId() != null && contatoDto.getId() != "") {
			contato.setId(Integer.parseInt(contatoDto.getId()));
		}

		pessoaFisica.setId(Integer.parseInt(contatoDto.getPessoaFisicaId()));

		contato.setPessoaFisica(pessoaFisica);
		contato.setCelular(contatoDto.getCelular());
		contato.setTelefone(contatoDto.getTelefone());
		contato.setNome(contatoDto.getNome());
		contato.setParentesco(contatoDto.getParentesco());

		return contato;
	}

	public static ContatoDto Converter(Contato contato) {
		ContatoDto contatoDto = new ContatoDto();

		contatoDto.setId(Integer.toString(contato.getId()));
		contatoDto.setPessoaFisicaId(Integer.toString(contato.getPessoaFisica().getId()));
		contatoDto.setCelular(contato.getCelular());
		contatoDto.setTelefone(contato.getTelefone());
		contatoDto.setNome(contato.getNome());
		contatoDto.setParentesco(contato.getParentesco());

		return contatoDto;
	}
	/* FIM CONVERSÃO CONTATO */

	/* INICIO CONVERSÃO VEICULO */
	public static Veiculo Converter(VeiculoDto veiculoDto) {
		Veiculo veiculo = new Veiculo();
		PessoaFisica pessoaFisica = new PessoaFisica();

		if (veiculoDto.getId() != null && veiculoDto.getId() != "") {
			veiculo.setId(Integer.parseInt(veiculoDto.getId()));
		}

		pessoaFisica.setId(Integer.parseInt(veiculoDto.getPessoaFisicaId()));

		veiculo.setPessoaFisica(pessoaFisica);
		veiculo.setMarca(veiculoDto.getMarca());
		veiculo.setModelo(veiculoDto.getModelo());
		veiculo.setPlaca(veiculoDto.getPlaca());
		veiculo.setRenavam(veiculoDto.getRenavam());
		veiculo.setCor(veiculoDto.getCor());
		veiculo.setInformacoesAdicionais(veiculoDto.getInformacoesAdicionais());

		return veiculo;
	}

	public static VeiculoDto Converter(Veiculo veiculo) {
		VeiculoDto veiculoDto = new VeiculoDto();

		veiculoDto.setId(Integer.toString(veiculo.getId()));
		veiculoDto.setPessoaFisicaId(Integer.toString(veiculo.getPessoaFisica().getId()));
		veiculoDto.setMarca(veiculo.getMarca());
		veiculoDto.setModelo(veiculo.getModelo());
		veiculoDto.setPlaca(veiculo.getPlaca());
		veiculoDto.setRenavam(veiculo.getRenavam());
		veiculoDto.setCor(veiculo.getCor());
		veiculoDto.setInformacoesAdicionais(veiculo.getInformacoesAdicionais());

		return veiculoDto;
	}
	
	public static List<VeiculoDto> ConverterLista(List<Veiculo> lista) {

		List<VeiculoDto> lst = new ArrayList<VeiculoDto>(lista.size());

		for (Veiculo veiculo: lista) {
			lst.add(Converter(veiculo));
		}

		return lst;
	}
	
	/* FIM CONVERSÃO VEICULO */

	/* INICIO CONVERSÃO CONDICAO CLINICA */
	public static CondicaoClinica Converter(CondicaoClinicaDto condicaoClinicaDto) {
		CondicaoClinica condicaoClinica = new CondicaoClinica();
		PessoaFisica pessoaFisica = new PessoaFisica();

		if (condicaoClinicaDto.getId() != null && condicaoClinicaDto.getId() != "") {
			condicaoClinica.setId(Integer.parseInt(condicaoClinicaDto.getId()));
		}

		if (condicaoClinicaDto.getDoencas() != null && condicaoClinicaDto.getDoencas().size() > 0) {
			condicaoClinica.setDoencas(new ArrayList<Doenca>());
			for (DoencaDto doencaDto : condicaoClinicaDto.getDoencas()) {
				Doenca doenca = new Doenca();
				doenca.setTipo(doencaDto.getTipo());
				condicaoClinica.getDoencas().add(doenca);
			}
		}

		if (condicaoClinicaDto.getAlergias() != null && condicaoClinicaDto.getAlergias().size() > 0) {
			condicaoClinica.setAlergias(new ArrayList<Alergia>());
			for (AlergiaDto alergiaDto : condicaoClinicaDto.getAlergias()) {
				Alergia alergia = new Alergia();
				alergia.setTipo(alergiaDto.getTipo());
				condicaoClinica.getAlergias().add(alergia);
			}
		}

		pessoaFisica.setId(Integer.parseInt(condicaoClinicaDto.getPessoaFisicaId()));
		condicaoClinica.setPessoaFisica(pessoaFisica);
		condicaoClinica.setConvenioMedico(condicaoClinicaDto.getConvenioMedico());
		condicaoClinica.setTipoSanguineo(condicaoClinicaDto.getTipoSanguineo());

		return condicaoClinica;
	}

	public static CondicaoClinicaDto Converter(CondicaoClinica condicaoClinica) throws ParseException {
		CondicaoClinicaDto condicaoClinicaDto = new CondicaoClinicaDto();

		if (condicaoClinica.getDoencas() != null && condicaoClinica.getDoencas().size() > 0) {
			condicaoClinicaDto.setDoencas(new ArrayList<DoencaDto>());
			for (int i = 0; i < condicaoClinica.getDoencas().size(); i++) {
				DoencaDto doencaDto = new DoencaDto();
				doencaDto.setId(String.valueOf(condicaoClinica.getDoencas().get(i).getId()));
				doencaDto.setCondicaoClinicaId(String.valueOf(condicaoClinica.getId()));
				doencaDto.setTipo(condicaoClinica.getDoencas().get(i).getTipo());
				doencaDto.setDataCadastro(
						formatDate(condicaoClinica.getDoencas().get(i).getDataCadastro().toString()).toString());
				doencaDto.setDataAtualizacao(
						formatDate(condicaoClinica.getDoencas().get(i).getDataAlteracao().toString()).toString());
				condicaoClinicaDto.getDoencas().add(doencaDto);
			}
		}

		if (condicaoClinica.getAlergias() != null && condicaoClinica.getAlergias().size() > 0) {
			condicaoClinicaDto.setAlergias(new ArrayList<AlergiaDto>());
			for (int i = 0; i < condicaoClinica.getAlergias().size(); i++) {
				AlergiaDto alergiaDto = new AlergiaDto();
				alergiaDto.setId(String.valueOf(condicaoClinica.getAlergias().get(i).getId()));
				alergiaDto.setCondicaoClinicaId(String.valueOf(condicaoClinica.getId()));
				alergiaDto.setTipo(condicaoClinica.getAlergias().get(i).getTipo());
				alergiaDto.setDataCadastro(
						formatDate(condicaoClinica.getAlergias().get(i).getDataCadastro().toString()).toString());
				alergiaDto.setDataAtualizacao(
						formatDate(condicaoClinica.getAlergias().get(i).getDataAlteracao().toString()).toString());
				condicaoClinicaDto.getAlergias().add(alergiaDto);
			}
		}

		condicaoClinicaDto.setId(Integer.toString(condicaoClinica.getId()));
		condicaoClinicaDto.setPessoaFisicaId(Integer.toString(condicaoClinica.getPessoaFisica().getId()));
		condicaoClinicaDto.setTipoSanguineo(condicaoClinica.getTipoSanguineo());
		condicaoClinicaDto.setConvenioMedico(condicaoClinica.getConvenioMedico());

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

	public static DoencaDto Converter(Doenca doenca) throws ParseException {
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

	public static AlergiaDto Converter(Alergia alergia) throws ParseException {
		AlergiaDto alergiaDto = new AlergiaDto();

		alergiaDto.setId(Integer.toString(alergia.getId()));
		alergiaDto.setCondicaoClinicaId(String.valueOf(alergia.getCondicaoClinica().getId()));
		alergiaDto.setTipo(alergia.getTipo());

		return alergiaDto;
	}
	/* FIM CONVERSÃO ALERGIA */

	public static boolean isDateValidFormat(String date) 
	{
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
	}
	
	public static Date parseDate(String dateValue) throws ParseException {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateValue);
	}

	public static String formatDate(String dateValue) throws ParseException {
		
		if(!isDateValidFormat(dateValue)) {
			return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
					.format(new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy", Locale.US).parse(dateValue));
		}
		
		return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
				.format(parseDate(dateValue));
	}
}
