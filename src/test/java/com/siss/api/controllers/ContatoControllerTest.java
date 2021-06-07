package com.siss.api.controllers;

import org.junit.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import com.siss.api.dtos.ContatoDto;
import com.siss.api.entities.PessoaFisica;
import com.siss.api.entities.Contato;
import com.siss.api.exceptions.ConsistenciaException;
import com.siss.api.services.ContatoService;
import com.siss.api.utils.ConversaoUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ContatoControllerTest {

	@Autowired
	private MockMvc mvc;
	@MockBean
	private ContatoService contatoService;
	
	Contato contatoTeste;
	PessoaFisica pessoaFisicaTeste;
	
	private Contato CriarContatoTestes() throws ParseException {
		
		contatoTeste = new Contato();
		pessoaFisicaTeste = new PessoaFisica();
		
		pessoaFisicaTeste.setId(1);
		
		contatoTeste.setId(1);
		contatoTeste.setNome("ContatoTeste1");
		contatoTeste.setParentesco("ParentescoTeste1");
		contatoTeste.setCelular("99879546321");
		contatoTeste.setTelefone("3879546321");
		contatoTeste.setDataAlteracao(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
		contatoTeste.setDataCadastro(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
		contatoTeste.setPessoaFisica(pessoaFisicaTeste);
		
		return contatoTeste;
	}
	
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testBuscarPorIdExistente() throws Exception {
		
		Contato contato = CriarContatoTestes();
		
		BDDMockito.given(contatoService.buscarPorId(Mockito.anyInt())).willReturn(Optional.of(contato));
		
		mvc.perform(MockMvcRequestBuilders.get("/api/contato/1").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andDo(print())
		.andExpect(jsonPath("$.dados.id").value(contato.getId()))
		.andExpect(jsonPath("$.dados.pessoaFisicaId").value(contato.getPessoaFisica().getId()))
		.andExpect(jsonPath("$.dados.nome").value(contato.getNome()))
		.andExpect(jsonPath("$.dados.telefone").value(contato.getTelefone()))
		.andExpect(jsonPath("$.dados.celular").value(contato.getCelular()))
		.andExpect(jsonPath("$.erros").isEmpty());
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testBuscarPorIdSemSucesso()  throws Exception {
	
		BDDMockito.given(contatoService.buscarPorId((Mockito.anyInt())))
				.willThrow(new ConsistenciaException("Teste inconsistência"));

		mvc.perform(MockMvcRequestBuilders.get("/api/contato/1").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.erros").value("Teste inconsistência"));

	}
/*	@Test
	@WithMockUser(roles = "USUARIO")
	public void testBuscarPorPessoaFisicaIdExistente() throws Exception {
		
		contatoTeste = CriarContatoTestes();
		List<Contato> contatos = new ArrayList<Contato>();
		contatos.add(contatoTeste);
		
		BDDMockito.given(contatoService.buscarPorPessoaFisicaId(Mockito.anyInt())).willReturn(Optional.of(contatos));
		
		mvc.perform(MockMvcRequestBuilders.get("/api/contato/pessoaFisicaId/1").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andDo(print())
		.andExpect(jsonPath("$.dados.[0].id").value(contatoTeste.getId()))
		.andExpect(jsonPath("$.dados.[0].pessoaFisicaId").value(contatoTeste.getPessoaFisica().getId()))
		.andExpect(jsonPath("$.dados.[0].nome").value(contatoTeste.getNome()))
		.andExpect(jsonPath("$.dados.[0].telefone").value(contatoTeste.getTelefone()))
		.andExpect(jsonPath("$.dados.[0].celular").value(contatoTeste.getCelular()))
		.andExpect(jsonPath("$.erros").isEmpty());
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testBuscarPorPessoaFisicaIdSemSucesso()  throws Exception {
	
		BDDMockito.given(contatoService.buscarPorPessoaFisicaId((Mockito.anyInt())))
				.willThrow(new ConsistenciaException("Teste inconsistência"));

		mvc.perform(MockMvcRequestBuilders.get("/api/contato/pessoaFisicaId/1").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.erros").value("Teste inconsistência"));

	}*/
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarComSucesso() throws Exception {
		Contato contato = CriarContatoTestes();
		ContatoDto objEntrada = ConversaoUtils.Converter(contato);
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		BDDMockito.given(contatoService.salvar(Mockito.any(Contato.class))).willReturn(contato);

		mvc.perform(MockMvcRequestBuilders.post("/api/contato").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.dados.id").value(contato.getId()))
				.andExpect(jsonPath("$.dados.pessoaFisicaId").value(contato.getPessoaFisica().getId()))
				.andExpect(jsonPath("$.dados.nome").value(contato.getNome()))
				.andExpect(jsonPath("$.dados.telefone").value(contato.getTelefone()))
				.andExpect(jsonPath("$.dados.celular").value(contato.getCelular()))
				.andExpect(jsonPath("$.erros").isEmpty());
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarSemSucesso() throws Exception {

		Contato contato = CriarContatoTestes();
		ContatoDto objEntrada = ConversaoUtils.Converter(contato);

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		BDDMockito.given(contatoService.salvar(Mockito.any(Contato.class)))
				.willThrow(new ConsistenciaException("Teste inconsistência."));

		mvc.perform(MockMvcRequestBuilders.post("/api/contato").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros").value("Teste inconsistência."));
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarPessoaFisicaIdEmBranco() throws Exception {
		
		ContatoDto objEntrada = new ContatoDto();

		objEntrada.setId("1");
		objEntrada.setNome("ContatoTeste1");
		objEntrada.setParentesco("ParentescoTeste1");
		objEntrada.setCelular("99879546321");
		objEntrada.setTelefone("3879546321");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/contato").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros.length()").value(2));
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarPessoaFisicaIdInsuficiente() throws Exception {
		
		ContatoDto objEntrada = new ContatoDto();

		objEntrada.setId("1");
		objEntrada.setNome("ContatoTeste1");
		objEntrada.setParentesco("ParentescoTeste1");
		objEntrada.setCelular("99879546321");
		objEntrada.setTelefone("3879546321");
		objEntrada.setPessoaFisicaId("");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/contato").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros.length()").value(3));
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testExcluirPorIdComSucesso() throws Exception {
		
		Contato contato = CriarContatoTestes();
		ContatoDto objEntrada = ConversaoUtils.Converter(contato);
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.delete("/api/contato/excluir/1").content(json).contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
		
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testExcluirPorIdSemSucesso() throws Exception {
		
		Contato contato = CriarContatoTestes();
		
		ContatoDto objEntrada = ConversaoUtils.Converter(contato);
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.delete("/api/contato/excluir/J").content(json).contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
		
	}
}
