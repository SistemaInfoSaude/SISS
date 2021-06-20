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
import com.siss.api.dtos.PessoaFisicaDto;
import com.siss.api.dtos.PessoaFisicaInfoDto;
import com.siss.api.dtos.RegistroValidacaoDto;
import com.siss.api.entities.PessoaFisica;
import com.siss.api.entities.Usuario;
import com.siss.api.entities.Veiculo;
import com.siss.api.exceptions.ConsistenciaException;
import com.siss.api.repositories.PessoaFisicaRepository;
import com.siss.api.repositories.UsuarioRepository;
import com.siss.api.services.PessoaFisicaService;
import com.siss.api.utils.ConversaoUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PessoaFisicaControllerTest {

	@Autowired
	private MockMvc mvc;
	@MockBean
	private PessoaFisicaService pessoaFisicaService;
	@MockBean
	private PessoaFisicaRepository pessoaFisicaRepository;	
	@MockBean
	private UsuarioRepository usuarioRepository;
	
	Usuario usuarioTeste;
	PessoaFisica pessoaFisicaTeste;
	Veiculo veiculoTeste;
	
	private PessoaFisica CriarPessoaFisicaTestes() throws ParseException {
		
		pessoaFisicaTeste = new PessoaFisica();
		usuarioTeste = new Usuario();
		veiculoTeste = new Veiculo();
		List<Veiculo> veiculos = new ArrayList<Veiculo>();

		pessoaFisicaTeste.setId(1);
		pessoaFisicaTeste.setRg("332291399");
		pessoaFisicaTeste.setCpf("443.150.880-56");
		pessoaFisicaTeste.setNome("Teste Teste");
		pessoaFisicaTeste.setTelefone("(71)2617-70995");
		pessoaFisicaTeste.setCelular("(71)99120-6541");
		pessoaFisicaTeste.setDataNascimento(new SimpleDateFormat("yyyy-MM-dd").parse("2001-04-05"));
		
		usuarioTeste.setId(1);
		usuarioTeste.setEmail("teste1234@email.com");
		usuarioTeste.setUsuario("UsuarioLegal");
		usuarioTeste.setSenha("SenhaLegal");
		usuarioTeste.setDataCadastro(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
		usuarioTeste.setDataAlteracao(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
		
		veiculoTeste.setId(1);
		veiculoTeste.setMarca("BMW");
		veiculoTeste.setModelo("BMW 320i");
		veiculoTeste.setPlaca("AAA9A99");
		veiculoTeste.setRenavam("22015976441");
		veiculoTeste.setCor("Branco");
		veiculos.add(veiculoTeste);
		
		pessoaFisicaTeste.setUsuario(usuarioTeste);
		pessoaFisicaTeste.setVeiculos(veiculos);
		
		return pessoaFisicaTeste;
	}
	
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testBuscarPorIdExistente() throws Exception {
		
		PessoaFisica pessoaFisica = CriarPessoaFisicaTestes();
		
		BDDMockito.given(pessoaFisicaService.buscarPorId(Mockito.anyInt())).willReturn(Optional.of(pessoaFisica));
		
		mvc.perform(MockMvcRequestBuilders.get("/api/pessoaFisica/1").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andDo(print())
		.andExpect(jsonPath("$.dados.id").value(pessoaFisica.getId()))
		.andExpect(jsonPath("$.dados.usuarioId").value(pessoaFisica.getUsuario().getId()))
		.andExpect(jsonPath("$.dados.cpf").value(pessoaFisica.getCpf()))
		.andExpect(jsonPath("$.dados.celular").value(pessoaFisica.getCelular()))
		.andExpect(jsonPath("$.dados.telefone").value(pessoaFisica.getTelefone()))
		.andExpect(jsonPath("$.erros").isEmpty());
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testBuscarPorIdSemSucesso()  throws Exception {
	
		BDDMockito.given(pessoaFisicaService.buscarPorId((Mockito.anyInt())))
				.willThrow(new ConsistenciaException("Teste inconsistência"));

		mvc.perform(MockMvcRequestBuilders.get("/api/pessoaFisica/1").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.erros").value("Teste inconsistência"));

	}	
	@Test
	@WithMockUser(roles = "EXEC_USUARIO")
	public void testBuscarPorCpfExistente() throws Exception {
		
		PessoaFisica pessoaFisica = CriarPessoaFisicaTestes();
		
		BDDMockito.given(pessoaFisicaService.buscarPorCpf(Mockito.anyString())).willReturn(Optional.of(pessoaFisica));
		
		mvc.perform(MockMvcRequestBuilders.get("/api/pessoaFisica/cpf/59842460026").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andDo(print())
		.andExpect(jsonPath("$.dados.id").value(pessoaFisica.getId()))
		.andExpect(jsonPath("$.dados.usuarioId").value(pessoaFisica.getUsuario().getId()))
		.andExpect(jsonPath("$.dados.cpf").value(pessoaFisica.getCpf()))
		.andExpect(jsonPath("$.dados.celular").value(pessoaFisica.getCelular()))
		.andExpect(jsonPath("$.dados.telefone").value(pessoaFisica.getTelefone()))
		.andExpect(jsonPath("$.erros").isEmpty());
	}
	@Test
	@WithMockUser(roles = "EXEC_USUARIO")
	public void testBuscarPorCpfSemSucesso()  throws Exception {
	
		BDDMockito.given(pessoaFisicaService.buscarPorCpf((Mockito.anyString())))
				.willThrow(new ConsistenciaException("Teste inconsistência"));

		mvc.perform(MockMvcRequestBuilders.get("/api/pessoaFisica/cpf/59842460026").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.erros").value("Teste inconsistência"));

	}
	@Test
	@WithMockUser(roles = "EXEC_USUARIO")
	public void testBuscarPorRgExistente() throws Exception {
		
		PessoaFisica pessoaFisica = CriarPessoaFisicaTestes();
		
		BDDMockito.given(pessoaFisicaService.buscarPorRg(Mockito.anyString())).willReturn(Optional.of(pessoaFisica));
		
		mvc.perform(MockMvcRequestBuilders.get("/api/pessoaFisica/rg/332291399").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andDo(print())
		.andExpect(jsonPath("$.dados.id").value(pessoaFisica.getId()))
		.andExpect(jsonPath("$.dados.usuarioId").value(pessoaFisica.getUsuario().getId()))
		.andExpect(jsonPath("$.dados.cpf").value(pessoaFisica.getCpf()))
		.andExpect(jsonPath("$.dados.celular").value(pessoaFisica.getCelular()))
		.andExpect(jsonPath("$.dados.telefone").value(pessoaFisica.getTelefone()))
		.andExpect(jsonPath("$.erros").isEmpty());
	}
	@Test
	@WithMockUser(roles = "EXEC_USUARIO")
	public void testBuscarPorRgSemSucesso()  throws Exception {
	
		BDDMockito.given(pessoaFisicaService.buscarPorRg((Mockito.anyString())))
				.willThrow(new ConsistenciaException("Teste inconsistência"));

		mvc.perform(MockMvcRequestBuilders.get("/api/pessoaFisica/rg/332291399").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.erros").value("Teste inconsistência"));

	}
	@Test
	@WithMockUser(roles = "EXEC_USUARIO")
	public void testBuscarPorPlacaVeiculoExistente() throws Exception {
		
		List<PessoaFisica> pessoasFisica = new ArrayList<PessoaFisica>();
		PessoaFisica pessoaFisica = CriarPessoaFisicaTestes();
		pessoasFisica.add(pessoaFisica);
		
		BDDMockito.given(pessoaFisicaService.buscarPorPlacaVeiculo(Mockito.anyString())).willReturn(Optional.of(pessoasFisica));
		
		mvc.perform(MockMvcRequestBuilders.get("/api/pessoaFisica/placaVeiculo/AAA9A99").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andDo(print())
		.andExpect(jsonPath("$.dados.[0].id").value(pessoaFisica.getId()))
		.andExpect(jsonPath("$.dados.[0].cpf").value(pessoaFisica.getCpf()))
		.andExpect(jsonPath("$.dados.[0].rg").value(pessoaFisica.getRg()))
		.andExpect(jsonPath("$.dados.[0].celular").value(pessoaFisica.getCelular()))
		.andExpect(jsonPath("$.dados.[0].telefone").value(pessoaFisica.getTelefone()))
		.andExpect(jsonPath("$.erros").isEmpty());
	}
	@Test
	@WithMockUser(roles = "EXEC_USUARIO")
	public void testBuscarPorPlacaVeiculoSemSucesso()  throws Exception {
	
		BDDMockito.given(pessoaFisicaService.buscarPorPlacaVeiculo((Mockito.anyString())))
				.willThrow(new ConsistenciaException("Teste inconsistência"));

		mvc.perform(MockMvcRequestBuilders.get("/api/pessoaFisica/placaVeiculo/AAA9A99").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.erros").value("Teste inconsistência"));

	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testBuscarPorUsuarioIdExistente() throws Exception {
		
		PessoaFisica pessoaFisica = CriarPessoaFisicaTestes();
		
		BDDMockito.given(pessoaFisicaService.buscarPorUsuarioId(Mockito.anyInt())).willReturn(Optional.of(pessoaFisica));
		
		mvc.perform(MockMvcRequestBuilders.get("/api/pessoaFisica//usuarioId/1").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andDo(print())
		.andExpect(jsonPath("$.dados.id").value(pessoaFisica.getId()))
		.andExpect(jsonPath("$.dados.usuarioId").value(pessoaFisica.getUsuario().getId()))
		.andExpect(jsonPath("$.dados.cpf").value(pessoaFisica.getCpf()))
		.andExpect(jsonPath("$.dados.celular").value(pessoaFisica.getCelular()))
		.andExpect(jsonPath("$.dados.telefone").value(pessoaFisica.getTelefone()))
		.andExpect(jsonPath("$.erros").isEmpty());
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testBuscarPorUsuarioIdSucesso()  throws Exception {
	
		BDDMockito.given(pessoaFisicaService.buscarPorUsuarioId((Mockito.anyInt())))
				.willThrow(new ConsistenciaException("Teste inconsistência"));

		mvc.perform(MockMvcRequestBuilders.get("/api/pessoaFisica/usuarioId/1").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.erros").value("Teste inconsistência"));

	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarComSucesso() throws Exception {
		PessoaFisica pessoaFisica = CriarPessoaFisicaTestes();
		PessoaFisicaInfoDto objEntrada = ConversaoUtils.Converter(pessoaFisica);
		objEntrada.setDataNascimento("2001-04-05");
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		BDDMockito.given(pessoaFisicaService.salvar(Mockito.any(PessoaFisica.class))).willReturn(pessoaFisica);

		mvc.perform(MockMvcRequestBuilders.post("/api/pessoaFisica").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.dados.id").value(pessoaFisica.getId()))
				.andExpect(jsonPath("$.dados.usuarioId").value(pessoaFisica.getUsuario().getId()))
				.andExpect(jsonPath("$.dados.cpf").value(pessoaFisica.getCpf()))
				.andExpect(jsonPath("$.dados.celular").value(pessoaFisica.getCelular()))
				.andExpect(jsonPath("$.dados.telefone").value(pessoaFisica.getTelefone()))
				.andExpect(jsonPath("$.erros").isEmpty());
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarSemSucesso() throws Exception {

		PessoaFisica pessoaFisica = CriarPessoaFisicaTestes();
		PessoaFisicaInfoDto objEntrada = ConversaoUtils.Converter(pessoaFisica);
		objEntrada.setDataNascimento("2001-04-05");
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		BDDMockito.given(pessoaFisicaService.salvar(Mockito.any(PessoaFisica.class)))
				.willThrow(new ConsistenciaException("Teste inconsistência."));

		mvc.perform(MockMvcRequestBuilders.post("/api/pessoaFisica").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros").value("Teste inconsistência."));
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarUsuarioIdIdEmBranco() throws Exception {
		
		PessoaFisicaDto objEntrada = new PessoaFisicaDto();

		objEntrada.setId("1");
		objEntrada.setUsuarioId("");
		objEntrada.setCpf("443.150.880-56");
		objEntrada.setRg("285785102");
		objEntrada.setNome("Nome Teste");
		objEntrada.setDataNascimento("2001-04-05");
		objEntrada.setTelefone("(71) 2617-7099");
		objEntrada.setCelular("(71) 99120-6541");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/pessoaFisica").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros.length()").value(3));
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarCpfEmBranco() throws Exception {
		
		PessoaFisicaDto objEntrada = new PessoaFisicaDto();

		objEntrada.setId("1");
		objEntrada.setUsuarioId("1");
		objEntrada.setCpf("");
		objEntrada.setRg("285785102");
		objEntrada.setNome("Nome Teste");
		objEntrada.setDataNascimento("2001-04-05");
		objEntrada.setTelefone("(71) 2617-7099");
		objEntrada.setCelular("(71) 99120-6541");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/pessoaFisica").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros.length()").value(2));
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarCpfInvalido() throws Exception {
		
		PessoaFisicaDto objEntrada = new PessoaFisicaDto();

		objEntrada.setId("1");
		objEntrada.setUsuarioId("1");
		objEntrada.setCpf("443.150.880-58");
		objEntrada.setRg("285785102");
		objEntrada.setNome("Nome Teste");
		objEntrada.setDataNascimento("2001-04-05");
		objEntrada.setTelefone("(71) 2617-7099");
		objEntrada.setCelular("(71) 99120-6541");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/pessoaFisica").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros").value("CPF inválido."));
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarRgEmBranco() throws Exception {
		
		PessoaFisicaDto objEntrada = new PessoaFisicaDto();

		objEntrada.setId("1");
		objEntrada.setUsuarioId("1");
		objEntrada.setCpf("443.150.880-56");
		objEntrada.setRg("");
		objEntrada.setNome("Nome Teste");
		objEntrada.setDataNascimento("2001-04-05");
		objEntrada.setTelefone("(71) 2617-7099");
		objEntrada.setCelular("(71) 99120-6541");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/pessoaFisica").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros.length()").value(2));
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarRgInsuficiente() throws Exception {
		
		PessoaFisicaDto objEntrada = new PessoaFisicaDto();

		objEntrada.setId("1");
		objEntrada.setUsuarioId("1");
		objEntrada.setCpf("443.150.880-56");
		objEntrada.setRg("28578510");
		objEntrada.setNome("Nome Teste");
		objEntrada.setDataNascimento("2001-04-05");
		objEntrada.setTelefone("(71) 2617-7099");
		objEntrada.setCelular("(71) 99120-6541");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/pessoaFisica").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros").value("RG deve conter 9 caracteres."));
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarNomeEmBranco() throws Exception {
		
		PessoaFisicaDto objEntrada = new PessoaFisicaDto();

		objEntrada.setId("1");
		objEntrada.setUsuarioId("1");
		objEntrada.setCpf("443.150.880-56");
		objEntrada.setRg("285785102");
		objEntrada.setNome("");
		objEntrada.setDataNascimento("2001-04-05");
		objEntrada.setTelefone("(71) 2617-7099");
		objEntrada.setCelular("(71) 99120-6541");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/pessoaFisica").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros.length()").value(2));
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarDataNascimentoEmBranco() throws Exception {
		
		PessoaFisicaDto objEntrada = new PessoaFisicaDto();

		objEntrada.setId("1");
		objEntrada.setUsuarioId("1");
		objEntrada.setCpf("443.150.880-56");
		objEntrada.setRg("285785102");
		objEntrada.setNome("Nome Teste");
		objEntrada.setDataNascimento("");
		objEntrada.setTelefone("(71) 2617-7099");
		objEntrada.setCelular("(71) 99120-6541");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/pessoaFisica").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros").value("dataNascimento não pode ser vazio."));
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarTelefoneInsuficiente() throws Exception {
		
		PessoaFisicaDto objEntrada = new PessoaFisicaDto();

		objEntrada.setId("1");
		objEntrada.setUsuarioId("1");
		objEntrada.setCpf("443.150.880-56");
		objEntrada.setRg("285785102");
		objEntrada.setNome("Nome Teste");
		objEntrada.setDataNascimento("2001-04-05");
		objEntrada.setTelefone("(71) 2617-709");
		objEntrada.setCelular("(71) 99120-6541");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/pessoaFisica").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros").value("telefone deve conter 15 caracteres."));
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarCelularInsuficiente() throws Exception {
		
		PessoaFisicaDto objEntrada = new PessoaFisicaDto();

		objEntrada.setId("1");
		objEntrada.setUsuarioId("1");
		objEntrada.setCpf("443.150.880-56");
		objEntrada.setRg("285785102");
		objEntrada.setNome("Nome Teste");
		objEntrada.setDataNascimento("2001-04-05");
		objEntrada.setTelefone("(71) 2617-7099");
		objEntrada.setCelular("(71) 99120-65");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/pessoaFisica").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros").value("celular deve conter 14-15 caracteres."));
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testCheckExistenteComSucesso() throws Exception {

		Usuario usuario = new Usuario();
		PessoaFisica pessoaFisica = new PessoaFisica();
		
		RegistroValidacaoDto objEntrada = new RegistroValidacaoDto();
		objEntrada.setUsername("Username Teste");
		objEntrada.setEmail("email@email.com");
		objEntrada.setRg("285785102");
		objEntrada.setCpf("443.150.880-56");
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		BDDMockito.given(usuarioRepository.findByUsuario(Mockito.anyString())).willReturn(new Usuario());
		BDDMockito.given(usuarioRepository.findByEmail(Mockito.anyString())).willReturn(Optional.of(usuario));
		BDDMockito.given(pessoaFisicaRepository.findByCpf(Mockito.anyString())).willReturn(Optional.of(pessoaFisica));
		BDDMockito.given(pessoaFisicaRepository.findByRg(Mockito.anyString())).willReturn(Optional.of(pessoaFisica));
		
		mvc.perform(MockMvcRequestBuilders.post("/api/pessoaFisica/checkExistente").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}
}