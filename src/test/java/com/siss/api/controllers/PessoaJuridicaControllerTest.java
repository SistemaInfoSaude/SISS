package com.siss.api.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.ParseException;
import java.text.SimpleDateFormat;

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
import com.siss.api.dtos.PessoaJuridicaDto;
import com.siss.api.entities.PessoaJuridica;
import com.siss.api.entities.Usuario;
import com.siss.api.exceptions.ConsistenciaException;
import com.siss.api.repositories.PessoaJuridicaRepository;
import com.siss.api.repositories.UsuarioRepository;
import com.siss.api.services.PessoaJuridicaService;
import com.siss.api.utils.ConversaoUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PessoaJuridicaControllerTest {

	@Autowired
	private MockMvc mvc;
	@MockBean
	private PessoaJuridicaService pessoaJuridicaService;
	@MockBean
	private PessoaJuridicaRepository pessoaJuridicaRepository;	
	@MockBean
	private UsuarioRepository usuarioRepository;
	
	Usuario usuarioTeste;
	PessoaJuridica pessoaJuridicaTeste;
	
	private PessoaJuridica CriarPessoaJuridicaTestes() throws ParseException {
		
		pessoaJuridicaTeste = new PessoaJuridica();
		usuarioTeste = new Usuario();

		pessoaJuridicaTeste.setId(1);
		pessoaJuridicaTeste.setCnpj("70989149000120");
		pessoaJuridicaTeste.setNomeFantasia("Teste teste");
		pessoaJuridicaTeste.setRazaoSocial("teste teste");
		
		usuarioTeste.setId(1);
		usuarioTeste.setEmail("teste1234@email.com");
		usuarioTeste.setUsuario("UsuarioLegal");
		usuarioTeste.setSenha("SenhaLegal");
		usuarioTeste.setDataCadastro(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
		usuarioTeste.setDataAlteracao(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
		
		pessoaJuridicaTeste.setUsuario(usuarioTeste);
		
		return pessoaJuridicaTeste;
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void testSalvarComSucesso() throws Exception {
		PessoaJuridica pessoaJuridica = CriarPessoaJuridicaTestes();
		PessoaJuridicaDto objEntrada = ConversaoUtils.Converter(pessoaJuridica);
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		BDDMockito.given(pessoaJuridicaService.salvar(Mockito.any(PessoaJuridica.class))).willReturn(pessoaJuridica);

		mvc.perform(MockMvcRequestBuilders.post("/api/pessoaJuridica").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.dados.id").value(pessoaJuridica.getId()))
				.andExpect(jsonPath("$.dados.usuarioId").value(pessoaJuridica.getUsuario().getId()))
				.andExpect(jsonPath("$.dados.cnpj").value(pessoaJuridica.getCnpj()))
				.andExpect(jsonPath("$.dados.razaoSocial").value(pessoaJuridica.getRazaoSocial()))
				.andExpect(jsonPath("$.dados.nomeFantasia").value(pessoaJuridica.getNomeFantasia()))
				.andExpect(jsonPath("$.erros").isEmpty());
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void testSalvarSemSucesso() throws Exception {

		PessoaJuridica pessoaJuridica = CriarPessoaJuridicaTestes();
		PessoaJuridicaDto objEntrada = ConversaoUtils.Converter(pessoaJuridica);
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		BDDMockito.given(pessoaJuridicaService.salvar(Mockito.any(PessoaJuridica.class)))
				.willThrow(new ConsistenciaException("Teste inconsistência."));

		mvc.perform(MockMvcRequestBuilders.post("/api/pessoaJuridica").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros").value("Teste inconsistência."));
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void testSalvarUsuarioIdIdEmBranco() throws Exception {
		
		PessoaJuridicaDto objEntrada = new PessoaJuridicaDto();

		objEntrada.setId("1");
		objEntrada.setUsuarioId("");
		objEntrada.setCnpj("70989149000120");
		objEntrada.setNomeFantasia("Teste teste");
		objEntrada.setRazaoSocial("teste teste");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/pessoaJuridica").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros.length()").value(3));
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void testSalvarCnpjEmBranco() throws Exception {
		
		PessoaJuridicaDto objEntrada = new PessoaJuridicaDto();

		objEntrada.setId("1");
		objEntrada.setUsuarioId("1");
		objEntrada.setCnpj("");
		objEntrada.setNomeFantasia("Teste teste");
		objEntrada.setRazaoSocial("teste teste");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/pessoaJuridica").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros.length()").value(2));
	}
	@Test
	@WithMockUser(roles = "ADMIN")
	public void testSalvarCpfInvalido() throws Exception {
		
		PessoaJuridicaDto objEntrada = new PessoaJuridicaDto();

		objEntrada.setId("1");
		objEntrada.setUsuarioId("1");
		objEntrada.setCnpj("70989149000121");
		objEntrada.setNomeFantasia("Teste teste");
		objEntrada.setRazaoSocial("teste teste");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/pessoaJuridica").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros").value("CNPJ inválido."));
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void testSalvarNomeFantasiaEmBranco() throws Exception {
		
		PessoaJuridicaDto objEntrada = new PessoaJuridicaDto();

		objEntrada.setId("1");
		objEntrada.setUsuarioId("1");
		objEntrada.setCnpj("70989149000120");
		objEntrada.setNomeFantasia("");
		objEntrada.setRazaoSocial("teste teste");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/pessoaJuridica").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros.length()").value(2));
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void testSalvarRazaoSocialEmBranco() throws Exception {
		
		PessoaJuridicaDto objEntrada = new PessoaJuridicaDto();

		objEntrada.setId("1");
		objEntrada.setUsuarioId("1");
		objEntrada.setCnpj("70989149000120");
		objEntrada.setNomeFantasia("Teste teste");
		objEntrada.setRazaoSocial("");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/pessoaJuridica").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros.length()").value(2));
	}
}