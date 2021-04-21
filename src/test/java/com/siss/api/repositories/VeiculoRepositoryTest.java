package com.siss.api.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.siss.api.entities.Usuario;
import com.siss.api.entities.Veiculo;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class VeiculoRepositoryTest {

	@Autowired
	VeiculoRepository veiculoRepository;
	@Autowired
	UsuarioRepository usuarioRepository;
	
	Veiculo veiculoTeste;
	Usuario usuarioTeste;
	
	private void CriarVeiculoTestes() throws ParseException {
		
		usuarioTeste = new Usuario();
		veiculoTeste = new Veiculo();
		
		usuarioTeste.setId(1);
		usuarioTeste.setUsuario("UsuarioLegal");
		usuarioTeste.setSenha("SenhaLegal");
		usuarioTeste.setDataCadastro(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
		usuarioTeste.setDataAlteracao(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
		
		veiculoTeste.setId(1);
		veiculoTeste.setMarca("BMW");
		veiculoTeste.setModelo("Algum Modelo ai");
		veiculoTeste.setPlaca("ABC12345");
		veiculoTeste.setRenavam("AIQOWE125");
		veiculoTeste.setDataCadastro(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
		veiculoTeste.setDataAlteracao(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
		veiculoTeste.setUsuario(usuarioTeste);
		
		List<Veiculo> veiculos = new ArrayList<Veiculo>();
		
		usuarioTeste.setVeiculos(veiculos);
	}
	@Before
	public void setUp() throws Exception {

		CriarVeiculoTestes();
		usuarioRepository.save(usuarioTeste);
		veiculoRepository.save(veiculoTeste);

	}

	@After
	public void tearDown() throws Exception {

		usuarioRepository.deleteAll();
		veiculoRepository.deleteAll();

	}
	@Test
	public void testfindByUsuarioId() {
		List<Veiculo> veiculos = veiculoRepository.findByUsuarioId(usuarioTeste.getId());
		assertTrue(!veiculos.isEmpty());
	}
}
