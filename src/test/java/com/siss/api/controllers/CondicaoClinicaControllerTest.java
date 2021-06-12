package com.siss.api.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siss.api.dtos.CondicaoClinicaDto;
import com.siss.api.entities.Alergia;
import com.siss.api.entities.CondicaoClinica;
import com.siss.api.entities.Doenca;
import com.siss.api.entities.PessoaFisica;
import com.siss.api.exceptions.ConsistenciaException;
import com.siss.api.services.CondicaoClinicaService;
import com.siss.api.utils.ConversaoUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CondicaoClinicaControllerTest {

	@Autowired
	private MockMvc mvc;
	@MockBean
	private CondicaoClinicaService condicaoClinicaService;
	
	CondicaoClinica condicaoClinicaTeste;
	PessoaFisica pessoaFisicaTeste;
	Alergia alergiaTeste;
	Doenca doencaTeste;
	
	private CondicaoClinica CriarCondicaoClinicaTestes() throws ParseException {
		
		condicaoClinicaTeste = new CondicaoClinica();
		pessoaFisicaTeste = new PessoaFisica();
		alergiaTeste = new Alergia();
		doencaTeste = new Doenca();
		List<Alergia> alergias = new ArrayList<Alergia>();
		List<Doenca> doencas = new ArrayList<Doenca>();
		
		alergiaTeste.setId(1);
		alergiaTeste.setDataCadastro(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
		alergiaTeste.setDataAlteracao(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
		alergiaTeste.setCondicaoClinica(condicaoClinicaTeste);
		alergias.add(alergiaTeste);
		
		doencaTeste.setId(1);
		doencaTeste.setDataCadastro(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
		doencaTeste.setDataAlteracao(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
		doencaTeste.setCondicaoClinica(condicaoClinicaTeste);
		doencas.add(doencaTeste);
		
		condicaoClinicaTeste.setId(1);
		condicaoClinicaTeste.setAlergias(alergias);
		condicaoClinicaTeste.setDoencas(doencas);
		condicaoClinicaTeste.setConvenioMedico("ConvenioMedicoTeste1");
		condicaoClinicaTeste.setTipoSanguineo("TipoSanguineoTeste1");
		condicaoClinicaTeste.setPessoaFisica(pessoaFisicaTeste);
		
		return condicaoClinicaTeste;
	}
	
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testBuscarPorIdExistente() throws Exception {
		
		CondicaoClinica condicaoClinica = CriarCondicaoClinicaTestes();
		
		BDDMockito.given(condicaoClinicaService.buscarPorId(Mockito.anyInt())).willReturn(Optional.of(condicaoClinica));
		
		mvc.perform(MockMvcRequestBuilders.get("/api/condicaoClinica/1").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andDo(print())
		.andExpect(jsonPath("$.dados.id").value(condicaoClinica.getId()))
		.andExpect(jsonPath("$.dados.pessoaFisicaId").value(condicaoClinica.getPessoaFisica().getId()))
		.andExpect(jsonPath("$.dados.tipoSanguineo").value(condicaoClinica.getTipoSanguineo()))
		.andExpect(jsonPath("$.dados.convenioMedico").value(condicaoClinica.getConvenioMedico()))
		.andExpect(jsonPath("$.erros").isEmpty());
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testBuscarPorIdSemSucesso()  throws Exception {
	
		BDDMockito.given(condicaoClinicaService.buscarPorId((Mockito.anyInt())))
				.willThrow(new ConsistenciaException("Teste inconsistência"));

		mvc.perform(MockMvcRequestBuilders.get("/api/condicaoClinica/1").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.erros").value("Teste inconsistência"));

	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarComSucesso() throws Exception {
		CondicaoClinica condicaoClinica = CriarCondicaoClinicaTestes();
		CondicaoClinicaDto objEntrada = ConversaoUtils.Converter(condicaoClinica);
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		BDDMockito.given(condicaoClinicaService.salvar(Mockito.any(CondicaoClinica.class))).willReturn(condicaoClinica);

		mvc.perform(MockMvcRequestBuilders.post("/api/condicaoClinica").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.dados.id").value(condicaoClinica.getId()))
				.andExpect(jsonPath("$.dados.tipoSanguineo").value(condicaoClinica.getTipoSanguineo()))
				.andExpect(jsonPath("$.dados.convenioMedico").value(condicaoClinica.getConvenioMedico()))
				.andExpect(jsonPath("$.erros").isEmpty());
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarSemSucesso() throws Exception {

		CondicaoClinica condicaoClinica = CriarCondicaoClinicaTestes();
		CondicaoClinicaDto objEntrada = ConversaoUtils.Converter(condicaoClinica);

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		BDDMockito.given(condicaoClinicaService.salvar(Mockito.any(CondicaoClinica.class)))
				.willThrow(new ConsistenciaException("Teste inconsistência."));

		mvc.perform(MockMvcRequestBuilders.post("/api/condicaoClinica").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros").value("Teste inconsistência."));
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarPessoaFisicaIdEmBranco() throws Exception {
		
		CondicaoClinicaDto objEntrada = new CondicaoClinicaDto();

		objEntrada.setId("1");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/condicaoClinica").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros.length()").value(4));
	}
	
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarPessoaFisicaIdInsuficiente() throws Exception {
		
		CondicaoClinicaDto objEntrada = new CondicaoClinicaDto();

		objEntrada.setId("1");
		objEntrada.setPessoaFisicaId("");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/condicaoClinica").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros.length()").value(5));
	}
}
