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
import com.siss.api.dtos.DoencaDto;
import com.siss.api.entities.CondicaoClinica;
import com.siss.api.entities.Doenca;
import com.siss.api.exceptions.ConsistenciaException;
import com.siss.api.services.DoencaService;
import com.siss.api.utils.ConversaoUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DoencaControllerTest {

	@Autowired
	private MockMvc mvc;
	@MockBean
	private DoencaService doencaService;
	
	Doenca doencaTeste;
	CondicaoClinica condicaoClinicaTeste;
	
	private Doenca CriarDoencaTestes() throws ParseException {
		
		doencaTeste = new Doenca();
		condicaoClinicaTeste = new CondicaoClinica();
		
		condicaoClinicaTeste.setId(1);
		
		doencaTeste.setId(1);
		doencaTeste.setTipo("DoencaTeste1");
		doencaTeste.setCondicaoClinica(condicaoClinicaTeste);
		
		return doencaTeste;
	}
	
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testBuscarPorIdExistente() throws Exception {
		
		Doenca doenca = CriarDoencaTestes();
		
		BDDMockito.given(doencaService.buscarPorId(Mockito.anyInt())).willReturn(Optional.of(doenca));
		
		mvc.perform(MockMvcRequestBuilders.get("/api/doenca/1").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andDo(print())
		.andExpect(jsonPath("$.dados.id").value(doenca.getId()))
		.andExpect(jsonPath("$.dados.condicaoClinicaId").value(doenca.getCondicaoClinica().getId()))
		.andExpect(jsonPath("$.dados.tipo").value(doenca.getTipo()))
		.andExpect(jsonPath("$.erros").isEmpty());
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testBuscarPorIdSemSucesso()  throws Exception {
	
		BDDMockito.given(doencaService.buscarPorId((Mockito.anyInt())))
				.willThrow(new ConsistenciaException("Teste inconsistência"));

		mvc.perform(MockMvcRequestBuilders.get("/api/doenca/1").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.erros").value("Teste inconsistência"));

	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarComSucesso() throws Exception {
		Doenca doenca = CriarDoencaTestes();
		DoencaDto objEntrada = ConversaoUtils.Converter(doenca);
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		BDDMockito.given(doencaService.salvar(Mockito.any(Doenca.class))).willReturn(doenca);

		mvc.perform(MockMvcRequestBuilders.post("/api/doenca").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.dados.id").value(doenca.getId()))
				.andExpect(jsonPath("$.dados.condicaoClinicaId").value(doenca.getCondicaoClinica().getId()))
				.andExpect(jsonPath("$.dados.tipo").value(doenca.getTipo()))
				.andExpect(jsonPath("$.erros").isEmpty());
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarSemSucesso() throws Exception {

		Doenca doenca = CriarDoencaTestes();
		DoencaDto objEntrada = ConversaoUtils.Converter(doenca);

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		BDDMockito.given(doencaService.salvar(Mockito.any(Doenca.class)))
				.willThrow(new ConsistenciaException("Teste inconsistência."));

		mvc.perform(MockMvcRequestBuilders.post("/api/doenca").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros").value("Teste inconsistência."));
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarCondicaoClinicaIdEmBranco() throws Exception {
		
		DoencaDto objEntrada = new DoencaDto();

		objEntrada.setId("1");
		objEntrada.setTipo("DoencaTeste1");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/doenca").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros.length()").value(2));
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarCondicaoClinicaIdInsuficiente() throws Exception {
		
		DoencaDto objEntrada = new DoencaDto();

		objEntrada.setId("1");
		objEntrada.setCondicaoClinicaId("1");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/doenca").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros.length()").value(2));
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testExcluirPorIdComSucesso() throws Exception {
		
		Doenca doenca = CriarDoencaTestes();
		DoencaDto objEntrada = ConversaoUtils.Converter(doenca);
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.delete("/api/doenca/excluir/1").content(json).contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
		
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testExcluirPorIdSemSucesso() throws Exception {
		
		Doenca doenca = CriarDoencaTestes();
		
		DoencaDto objEntrada = ConversaoUtils.Converter(doenca);
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.delete("/api/doenca/excluir/J").content(json).contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
		
	}
}
