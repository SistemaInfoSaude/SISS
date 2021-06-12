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
import com.siss.api.dtos.VeiculoDto;
import com.siss.api.entities.PessoaFisica;
import com.siss.api.entities.Veiculo;
import com.siss.api.exceptions.ConsistenciaException;
import com.siss.api.services.VeiculoService;
import com.siss.api.utils.ConversaoUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class VeiculoControllerTest {

	@Autowired
	private MockMvc mvc;
	@MockBean
	private VeiculoService veiculoService;
	
	Veiculo veiculoTeste;
	PessoaFisica pessoaFisicaTeste;
	
	private Veiculo CriarVeiculoTestes() throws ParseException {
		
		pessoaFisicaTeste = new PessoaFisica();
		veiculoTeste = new Veiculo();
		
		
		pessoaFisicaTeste.setId(1);
		pessoaFisicaTeste.setRg("332291388");
		pessoaFisicaTeste.setCpf("59842469026");
		pessoaFisicaTeste.setNome("Teste Teste");
		pessoaFisicaTeste.setDataNascimento(new SimpleDateFormat("dd/MM/yyyy").parse("05/04/2001"));
		
		veiculoTeste.setId(1);
		veiculoTeste.setMarca("BMW");
		veiculoTeste.setModelo("BMW 320i");
		veiculoTeste.setPlaca("AAA9A99");
		veiculoTeste.setRenavam("22015976441");
		veiculoTeste.setCor("Branco");
		veiculoTeste.setPessoaFisica(pessoaFisicaTeste);
		
		List<Veiculo> veiculos = new ArrayList<Veiculo>();
		
		pessoaFisicaTeste.setVeiculos(veiculos);
		
		return veiculoTeste;
	}
	
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testBuscarPorIdExistente() throws Exception {
		
		Veiculo veiculo = CriarVeiculoTestes();
		
		BDDMockito.given(veiculoService.buscarPorId(Mockito.anyInt())).willReturn(Optional.of(veiculo));
		
		mvc.perform(MockMvcRequestBuilders.get("/api/veiculo/1").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andDo(print())
		.andExpect(jsonPath("$.dados.id").value(veiculo.getId()))
		.andExpect(jsonPath("$.dados.pessoaFisicaId").value(veiculo.getPessoaFisica().getId()))
		.andExpect(jsonPath("$.dados.marca").value(veiculo.getMarca()))
		.andExpect(jsonPath("$.dados.modelo").value(veiculo.getModelo()))
		.andExpect(jsonPath("$.dados.placa").value(veiculo.getPlaca()))
		.andExpect(jsonPath("$.dados.renavam").value(veiculo.getRenavam()))
		.andExpect(jsonPath("$.dados.informacoesAdicionais").value(veiculo.getInformacoesAdicionais()))
		.andExpect(jsonPath("$.erros").isEmpty());
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testBuscarPorIdSemSucesso()  throws Exception {
	
		BDDMockito.given(veiculoService.buscarPorId((Mockito.anyInt())))
				.willThrow(new ConsistenciaException("Teste inconsistência"));

		mvc.perform(MockMvcRequestBuilders.get("/api/veiculo/1").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.erros").value("Teste inconsistência"));

	}
/*	@Test
	@WithMockUser(roles = "USUARIO")
	public void testBuscarPorPessoaFisicaIdExistente() throws Exception {
		
		veiculoTeste = CriarVeiculoTestes();
		List<Veiculo> veiculos = new ArrayList<Veiculo>();
		veiculos.add(veiculoTeste);
		
		BDDMockito.given(veiculoService.buscarPorPessoaFisicaId(Mockito.anyInt())).willReturn(Optional.of(veiculos));
		
		mvc.perform(MockMvcRequestBuilders.get("/api/veiculo/pessoaFisicaId/1").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andDo(print())
		.andExpect(jsonPath("$.dados.[0].id").value(veiculoTeste.getId()))
		.andExpect(jsonPath("$.dados.[0].pessoaFisicaId").value(veiculoTeste.getPessoaFisica().getId()))
		.andExpect(jsonPath("$.dados.[0].marca").value(veiculoTeste.getMarca()))
		.andExpect(jsonPath("$.dados.[0].modelo").value(veiculoTeste.getModelo()))
		.andExpect(jsonPath("$.dados.[0].placa").value(veiculoTeste.getPlaca()))
		.andExpect(jsonPath("$.dados.[0].renavam").value(veiculoTeste.getRenavam()))
		.andExpect(jsonPath("$.dados.[0].informacoesAdicionais").value(veiculoTeste.getInformacoesAdicionais()))
		.andExpect(jsonPath("$.erros").isEmpty());
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testBuscarPorPessoaFisicaIdSemSucesso()  throws Exception {
	
		BDDMockito.given(veiculoService.buscarPorPessoaFisicaId((Mockito.anyInt())))
				.willThrow(new ConsistenciaException("Teste inconsistência"));

		mvc.perform(MockMvcRequestBuilders.get("/api/veiculo/pessoaFisicaId/1").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.erros").value("Teste inconsistência"));

	}*/
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarComSucesso() throws Exception {
		Veiculo veiculo = CriarVeiculoTestes();
		VeiculoDto objEntrada = ConversaoUtils.Converter(veiculo);
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		BDDMockito.given(veiculoService.salvar(Mockito.any(Veiculo.class))).willReturn(veiculo);

		mvc.perform(MockMvcRequestBuilders.post("/api/veiculo").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.dados.id").value(veiculo.getId()))
				.andExpect(jsonPath("$.dados.pessoaFisicaId").value(veiculo.getPessoaFisica().getId()))
				.andExpect(jsonPath("$.dados.marca").value(veiculo.getMarca()))
				.andExpect(jsonPath("$.dados.modelo").value(veiculo.getModelo()))
				.andExpect(jsonPath("$.dados.placa").value(veiculo.getPlaca()))
				.andExpect(jsonPath("$.dados.renavam").value(veiculo.getRenavam()))
				.andExpect(jsonPath("$.dados.informacoesAdicionais").value(veiculo.getInformacoesAdicionais()))
				.andExpect(jsonPath("$.erros").isEmpty());
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarSemSucesso() throws Exception {

		Veiculo veiculo = CriarVeiculoTestes();
		VeiculoDto objEntrada = ConversaoUtils.Converter(veiculo);

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		BDDMockito.given(veiculoService.salvar(Mockito.any(Veiculo.class)))
				.willThrow(new ConsistenciaException("Teste inconsistência."));

		mvc.perform(MockMvcRequestBuilders.post("/api/veiculo").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros").value("Teste inconsistência."));
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarPessoaFisicaIdEmBranco() throws Exception {
		
		VeiculoDto objEntrada = new VeiculoDto();

		objEntrada.setId("1");
		objEntrada.setMarca("BMW");
		objEntrada.setModelo("BMW 320i");
		objEntrada.setPlaca("AAA9A99");
		objEntrada.setRenavam("22015976441");
		objEntrada.setCor("Branco");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/veiculo").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros.length()").value(2));
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarPessoaFisicaIdInsuficiente() throws Exception {
		
		VeiculoDto objEntrada = new VeiculoDto();

		objEntrada.setId("1");
		objEntrada.setMarca("BMW");
		objEntrada.setModelo("BMW 320i");
		objEntrada.setPlaca("AAA9A99");
		objEntrada.setRenavam("22015976441");
		objEntrada.setCor("Branco");
		objEntrada.setPessoaFisicaId("");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/veiculo").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros.length()").value(3));
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarMarcaEmBranco() throws Exception {
		
		VeiculoDto objEntrada = new VeiculoDto();

		objEntrada.setId("1");
		objEntrada.setModelo("BMW 320i");
		objEntrada.setPlaca("AAA9A99");
		objEntrada.setRenavam("22015976441");
		objEntrada.setCor("Branco");
		objEntrada.setPessoaFisicaId("1");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/veiculo").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros.length()").value(1));
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarMarcaInsuficiente() throws Exception {
		
		VeiculoDto objEntrada = new VeiculoDto();

		objEntrada.setId("1");
		objEntrada.setMarca("BMW");
		objEntrada.setModelo("");
		objEntrada.setPlaca("AAA9A99");
		objEntrada.setRenavam("22015976441");
		objEntrada.setCor("Branco");
		objEntrada.setPessoaFisicaId("1");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/veiculo").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros.length()").value(2));
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarModeloEmBranco() throws Exception {
		
		VeiculoDto objEntrada = new VeiculoDto();

		objEntrada.setId("1");
		objEntrada.setMarca("BMW");
		objEntrada.setPlaca("AAA9A99");
		objEntrada.setRenavam("22015976441");
		objEntrada.setCor("Branco");
		objEntrada.setPessoaFisicaId("1");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/veiculo").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros.length()").value(1));
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarPlacaInsuficiente() throws Exception {
		
		VeiculoDto objEntrada = new VeiculoDto();

		objEntrada.setId("1");
		objEntrada.setMarca("BMW");
		objEntrada.setModelo("BMW 320i");
		objEntrada.setPlaca("AAA");
		objEntrada.setRenavam("22015976441");
		objEntrada.setCor("Branco");
		objEntrada.setPessoaFisicaId("1");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/veiculo").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros.length()").value(1));
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarPlacaEmBranco() throws Exception {
		
		VeiculoDto objEntrada = new VeiculoDto();

		objEntrada.setId("1");
		objEntrada.setMarca("BMW");
		objEntrada.setModelo("BMW 320i");
		objEntrada.setRenavam("22015976441");
		objEntrada.setCor("Branco");
		objEntrada.setPessoaFisicaId("1");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/veiculo").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros.length()").value(1));
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarRenavamInsuficiente() throws Exception {
		
		VeiculoDto objEntrada = new VeiculoDto();

		objEntrada.setId("1");
		objEntrada.setMarca("BMW");
		objEntrada.setModelo("BMW 320i");
		objEntrada.setPlaca("AAA9A99");
		objEntrada.setRenavam("22015976");
		objEntrada.setCor("Branco");
		objEntrada.setPessoaFisicaId("1");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/veiculo").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros.length()").value(1));
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarRenavamEmBranco() throws Exception {
		
		VeiculoDto objEntrada = new VeiculoDto();

		objEntrada.setId("1");
		objEntrada.setMarca("BMW");
		objEntrada.setModelo("BMW 320i");
		objEntrada.setPlaca("AAA9A99");
		objEntrada.setCor("Branco");
		objEntrada.setPessoaFisicaId("1");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/veiculo").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros.length()").value(1));
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarCorInsuficiente() throws Exception {
		
		VeiculoDto objEntrada = new VeiculoDto();

		objEntrada.setId("1");
		objEntrada.setMarca("BMW");
		objEntrada.setModelo("BMW 320i");
		objEntrada.setPlaca("AAA9A99");
		objEntrada.setRenavam("22015976441");
		objEntrada.setCor("Br");
		objEntrada.setPessoaFisicaId("1");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/veiculo").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros.length()").value(1));
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarCorEmBranco() throws Exception {
		
		VeiculoDto objEntrada = new VeiculoDto();

		objEntrada.setId("1");
		objEntrada.setMarca("BMW");
		objEntrada.setModelo("BMW 320i");
		objEntrada.setPlaca("AAA9A99");
		objEntrada.setRenavam("22015976441");
		objEntrada.setPessoaFisicaId("1");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/veiculo").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros.length()").value(1));
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testSalvarModeloInsuficiente() throws Exception {
		
		VeiculoDto objEntrada = new VeiculoDto();

		objEntrada.setId("1");
		objEntrada.setMarca("");
		objEntrada.setModelo("BMW 320i");
		objEntrada.setPlaca("AAA9A99");
		objEntrada.setRenavam("22015976441");
		objEntrada.setCor("Branco");
		objEntrada.setPessoaFisicaId("1");

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/veiculo").content(json).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros.length()").value(2));
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testExcluirPorIdComSucesso() throws Exception {
		
		Veiculo veiculo = CriarVeiculoTestes();
		VeiculoDto objEntrada = ConversaoUtils.Converter(veiculo);
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.delete("/api/veiculo/excluir/1").content(json).contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
		
	}
	@Test
	@WithMockUser(roles = "USUARIO")
	public void testExcluirPorIdSemSucesso() throws Exception {
		
		Veiculo veiculo = CriarVeiculoTestes();
		
		VeiculoDto objEntrada = ConversaoUtils.Converter(veiculo);
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.delete("/api/veiculo/excluir/J").content(json).contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
		
	}
}
