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

import com.siss.api.entities.PessoaFisica;
import com.siss.api.entities.Veiculo;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class VeiculoRepositoryTest {

	@Autowired
	VeiculoRepository veiculoRepository;
	@Autowired
	PessoaFisicaRepository pessoaFisicaRepository;
	
	Veiculo veiculoTeste;
	PessoaFisica pessoaFisicaTeste;
	
	private void CriarVeiculoTestes() throws ParseException {
		
		pessoaFisicaTeste = new PessoaFisica();
		veiculoTeste = new Veiculo();
		
		
		pessoaFisicaTeste.setId(1);
		pessoaFisicaTeste.setRg("332291388");
		pessoaFisicaTeste.setCpf("59842469026");
		pessoaFisicaTeste.setNome("Teste Teste");
		pessoaFisicaTeste.setDataNascimento(new SimpleDateFormat("dd/MM/yyyy").parse("05/04/2001"));
		
		veiculoTeste.setId(1);
		veiculoTeste.setMarca("BMW");
		veiculoTeste.setModelo("Algum Modelo ai");
		veiculoTeste.setPlaca("ABC12345");
		veiculoTeste.setRenavam("AIQOWE125");
		veiculoTeste.setDataCadastro(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
		veiculoTeste.setDataAlteracao(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
		veiculoTeste.setPessoaFisica(pessoaFisicaTeste);
		
		List<Veiculo> veiculos = new ArrayList<Veiculo>();
		
		pessoaFisicaTeste.setVeiculos(veiculos);
	}
	@Before
	public void setUp() throws Exception {

		CriarVeiculoTestes();
		pessoaFisicaRepository.save(pessoaFisicaTeste);
		veiculoRepository.save(veiculoTeste);

	}

	@After
	public void tearDown() throws Exception {

		pessoaFisicaRepository.deleteAll();
		veiculoRepository.deleteAll();

	}
	@Test
	public void testfindByPessoaFisicaId() {
		List<Veiculo> veiculos = veiculoRepository.findByPessoaFisicaId(pessoaFisicaTeste.getId());
		assertTrue(!veiculos.isEmpty());
	}
}
