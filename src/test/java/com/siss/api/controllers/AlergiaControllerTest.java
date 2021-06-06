package com.siss.api.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.ParseException;
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
import com.siss.api.dtos.AlergiaDto;
import com.siss.api.entities.CondicaoClinica;
import com.siss.api.entities.Alergia;
import com.siss.api.exceptions.ConsistenciaException;
import com.siss.api.services.AlergiaService;
import com.siss.api.utils.ConversaoUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AlergiaControllerTest {

	@Autowired
	private MockMvc mvc;
	@MockBean
	private AlergiaService alergiaService;
	
	Alergia alergiaTeste;
	CondicaoClinica condicaoClinicaTeste;
	
	private Alergia CriarAlergiaTestes() throws ParseException {
		
		alergiaTeste = new Alergia();
		condicaoClinicaTeste = new CondicaoClinica();
		
		condicaoClinicaTeste.setId(1);
		
		alergiaTeste.setId(1);
		alergiaTeste.setTipo("AlergiaTeste1");
		alergiaTeste.setCondicaoClinica(condicaoClinicaTeste);
		
		return alergiaTeste;
	}
	
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testBuscarPorIdExistente() throws Exception {
		
		Alergia alergia = CriarAlergiaTestes();
		
		BDDMockito.given(alergiaService.buscarPorId(Mockito.anyInt())).willReturn(Optional.of(alergia));
		
		mvc.perform(MockMvcRequestBuilders.get("/api/alergia/1").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andDo(print())
		.andExpect(jsonPath("$.dados.id").value(alergia.getId()))
		.andExpect(jsonPath("$.dados.condicaoClinicaId").value(alergia.getCondicaoClinica().getId()))
		.andExpect(jsonPath("$.dados.tipo").value(alergia.getTipo()))
		.andExpect(jsonPath("$.erros").isEmpty());
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testBuscarPorIdSemSucesso()  throws Exception {
	
		BDDMockito.given(alergiaService.buscarPorId((Mockito.anyInt())))
				.willThrow(new ConsistenciaException("Teste inconsistência"));

		mvc.perform(MockMvcRequestBuilders.get("/api/alergia/1").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.erros").value("Teste inconsistência"));

	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarComSucesso() throws Exception {
		Alergia alergia = CriarAlergiaTestes();
		AlergiaDto objEntrada = ConversaoUtils.Converter(alergia);
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		BDDMockito.given(alergiaService.salvar(Mockito.any(Alergia.class))).willReturn(alergia);

		mvc.perform(MockMvcRequestBuilders.post("/api/alergia").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.dados.id").value(alergia.getId()))
				.andExpect(jsonPath("$.dados.condicaoClinicaId").value(alergia.getCondicaoClinica().getId()))
				.andExpect(jsonPath("$.dados.tipo").value(alergia.getTipo()))
				.andExpect(jsonPath("$.erros").isEmpty());
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarSemSucesso() throws Exception {

		Alergia alergia = CriarAlergiaTestes();
		AlergiaDto objEntrada = ConversaoUtils.Converter(alergia);

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		BDDMockito.given(alergiaService.salvar(Mockito.any(Alergia.class)))
				.willThrow(new ConsistenciaException("Teste inconsistência."));

		mvc.perform(MockMvcRequestBuilders.post("/api/alergia").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros").value("Teste inconsistência."));
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarCondicaoClinicaIdEmBranco() throws Exception {
		
		AlergiaDto objEntrada = new AlergiaDto();

		objEntrada.setId("1");
		objEntrada.setTipo("AlergiaTeste1");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/alergia").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros.length()").value(2));
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarTipoEmBranco() throws Exception {
		
		AlergiaDto objEntrada = new AlergiaDto();

		objEntrada.setId("1");
		objEntrada.setCondicaoClinicaId("1");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/alergia").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros.length()").value(2));
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testExcluirPorIdComSucesso() throws Exception {
		
		Alergia alergia = CriarAlergiaTestes();
		AlergiaDto objEntrada = ConversaoUtils.Converter(alergia);
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.delete("/api/alergia/excluir/1").content(json).contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
		
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testExcluirPorIdSemSucesso() throws Exception {
		
		Alergia alergia = CriarAlergiaTestes();
		
		AlergiaDto objEntrada = ConversaoUtils.Converter(alergia);
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.delete("/api/alergia/excluir/J").content(json).contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
		
	}
}