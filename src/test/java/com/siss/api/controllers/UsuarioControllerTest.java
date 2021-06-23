package com.siss.api.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.siss.api.dtos.CodigoEmailDto;
import com.siss.api.dtos.RedefinirSenhaDto;
import com.siss.api.dtos.SenhaDto;
import com.siss.api.dtos.UsuarioDto;
import com.siss.api.entities.PessoaFisica;
import com.siss.api.entities.Usuario;
import com.siss.api.exceptions.ConsistenciaException;
import com.siss.api.services.UsuarioService;
import com.siss.api.utils.ConversaoUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UsuarioControllerTest {

	@Autowired
	private MockMvc mvc;
	@MockBean
	private UsuarioService usuarioService;
	
	Usuario usuarioTeste;
	PessoaFisica pessoaFisicaTeste;
	
	private Usuario CriarUsuarioTestes() throws ParseException {
		
		usuarioTeste = new Usuario();
		
		usuarioTeste.setId(1);
		usuarioTeste.setEmail("teste123456@gmail.com");
		usuarioTeste.setUsuario("UsuarioLegal");
		usuarioTeste.setSenha("SenhaLegal");
		usuarioTeste.setDataCadastro(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
		usuarioTeste.setDataAlteracao(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
		usuarioTeste.setExecutante(false);

		return usuarioTeste;
	}
	
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testBuscarPorIdExistente() throws Exception {
		
		Usuario usuario = CriarUsuarioTestes();
		
		BDDMockito.given(usuarioService.buscarPorId(Mockito.anyInt())).willReturn(Optional.of(usuario));
		
		mvc.perform(MockMvcRequestBuilders.get("/api/usuario/1").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andDo(print())
		.andExpect(jsonPath("$.dados.id").value(usuario.getId()))
		.andExpect(jsonPath("$.dados.usuario").value(usuario.getUsuario()))
		.andExpect(jsonPath("$.dados.email").value(usuario.getEmail()))
		.andExpect(jsonPath("$.erros").isEmpty());
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testBuscarPorIdSemSucesso()  throws Exception {
	
		BDDMockito.given(usuarioService.buscarPorId((Mockito.anyInt())))
				.willThrow(new ConsistenciaException("Teste inconsistência"));

		mvc.perform(MockMvcRequestBuilders.get("/api/usuario/1").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.erros").value("Teste inconsistência"));

	}
	
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarComSucesso() throws Exception {
		Usuario usuario = CriarUsuarioTestes();
		UsuarioDto objEntrada = ConversaoUtils.Converter(usuario);
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		BDDMockito.given(usuarioService.salvar(Mockito.any(Usuario.class))).willReturn(usuario);

		mvc.perform(MockMvcRequestBuilders.post("/api/usuario/registrar").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.dados.id").value(usuario.getId()))
				.andExpect(jsonPath("$.dados.usuario").value(usuario.getUsuario()))
				.andExpect(jsonPath("$.dados.email").value(usuario.getEmail()))
				.andExpect(jsonPath("$.erros").isEmpty());
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarSemSucesso() throws Exception {

		Usuario usuario = CriarUsuarioTestes();
		UsuarioDto objEntrada = ConversaoUtils.Converter(usuario);

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		BDDMockito.given(usuarioService.salvar(Mockito.any(Usuario.class)))
				.willThrow(new ConsistenciaException("Teste inconsistência."));

		mvc.perform(MockMvcRequestBuilders.post("/api/usuario/registrar").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros").value("Teste inconsistência."));
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void testSalvarPjComSucesso() throws Exception {
		Usuario usuario = CriarUsuarioTestes();
		UsuarioDto objEntrada = ConversaoUtils.Converter(usuario);
		objEntrada.setExecutante("1");
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		BDDMockito.given(usuarioService.salvar(Mockito.any(Usuario.class))).willReturn(usuario);

		mvc.perform(MockMvcRequestBuilders.post("/api/usuario/registrarPj").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.dados.id").value(usuario.getId()))
				.andExpect(jsonPath("$.dados.usuario").value(usuario.getUsuario()))
				.andExpect(jsonPath("$.dados.email").value(usuario.getEmail()))
				.andExpect(jsonPath("$.erros").isEmpty());
	}
	@Test
	@WithMockUser(roles = "ADMIN")
	public void testSalvarPjSemSucesso() throws Exception {

		Usuario usuario = CriarUsuarioTestes();
		UsuarioDto objEntrada = ConversaoUtils.Converter(usuario);
		objEntrada.setExecutante("1");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		BDDMockito.given(usuarioService.salvar(Mockito.any(Usuario.class)))
				.willThrow(new ConsistenciaException("Teste inconsistência."));

		mvc.perform(MockMvcRequestBuilders.post("/api/usuario/registrarPj").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros").value("Teste inconsistência."));
	}
	
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testAlterarSenhaUsuarioComSucesso() throws Exception {
		
		SenhaDto objEntrada = new SenhaDto();
		objEntrada.setIdUsuario("1");
		objEntrada.setSenhaAtual("SenhaLegal");
		objEntrada.setNovaSenha("NovaSenha");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/usuario/alterarSenha").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.dados.idUsuario").value(objEntrada.getIdUsuario()))
				.andExpect(jsonPath("$.dados.senhaAtual").value(objEntrada.getSenhaAtual()))
				.andExpect(jsonPath("$.dados.novaSenha").value(objEntrada.getNovaSenha()))
				.andExpect(jsonPath("$.erros").isEmpty());
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testAlterarSenhaUsuarioSemSucesso() throws Exception {

		SenhaDto objEntrada = new SenhaDto();
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/usuario/alterarSenha").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testEnviarCodigoComSucesso() throws Exception {
		
		CodigoEmailDto objEntrada = new CodigoEmailDto();
		objEntrada.setIdUsuario("1");
		objEntrada.setCodigo("CODE1");
		objEntrada.setEmail("email@email.com");
		objEntrada.setMensagem("Mensagem");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/usuario/enviarCodigo").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testNovaSenhaUsuarioComSucesso() throws Exception {
		
		RedefinirSenhaDto objEntrada = new RedefinirSenhaDto();
		objEntrada.setIdUsuario("1");
		objEntrada.setCodigo("CODE1");
		objEntrada.setNovaSenha("NovaSenha");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/usuario/redefinirSenha").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.dados.idUsuario").value(objEntrada.getIdUsuario()))
				.andExpect(jsonPath("$.dados.codigo").value(objEntrada.getCodigo()))
				.andExpect(jsonPath("$.dados.novaSenha").value(objEntrada.getNovaSenha()))
				.andExpect(jsonPath("$.erros").isEmpty());
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testNovaSenhaUsuarioSemSucesso() throws Exception {

		RedefinirSenhaDto objEntrada = new RedefinirSenhaDto();
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/usuario/redefinirSenha").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}
}