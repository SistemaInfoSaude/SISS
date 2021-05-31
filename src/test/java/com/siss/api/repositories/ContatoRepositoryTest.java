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
import com.siss.api.entities.Contato;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ContatoRepositoryTest {

	@Autowired
	ContatoRepository contatoRepository;
	@Autowired
	PessoaFisicaRepository pessoaFisicaRepository;
	
	Contato contatoTeste;
	PessoaFisica pessoaFisicaTeste;
	
	private void CriarContatoTestes() throws ParseException {
		pessoaFisicaTeste = new PessoaFisica();
		contatoTeste = new Contato();
		
		pessoaFisicaTeste.setId(2);
		pessoaFisicaTeste.setRg("332291388");
		pessoaFisicaTeste.setCpf("59842469026");
		pessoaFisicaTeste.setNome("Teste Teste");
		pessoaFisicaTeste.setDataNascimento(new SimpleDateFormat("dd/MM/yyyy").parse("05/04/2001"));
		
		contatoTeste.setId(1);
		contatoTeste.setNome("Nome do Usuario");
		contatoTeste.setTelefone("3335846725");
		contatoTeste.setCelular("9995846725");
		contatoTeste.setDataCadastro(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
		contatoTeste.setDataAlteracao(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
		contatoTeste.setParentesco("pai");
		contatoTeste.setPessoaFisica(pessoaFisicaTeste);
		
		List<Contato> contatos = new ArrayList<Contato>();
		
		pessoaFisicaTeste.setContatos(contatos);
	}
	@Before
	public void setUp() throws Exception {

		CriarContatoTestes();
		pessoaFisicaRepository.save(pessoaFisicaTeste);
		contatoRepository.save(contatoTeste);

	}

	@After
	public void tearDown() throws Exception {

		pessoaFisicaRepository.deleteAll();
		contatoRepository.deleteAll();

	}
	@Test
	public void testFindByPessoaFisicaId() {
		List<Contato> contatos = contatoRepository.findByPessoaFisicaId(pessoaFisicaTeste.getId());
		assertTrue(!contatos.isEmpty());
	}
}
