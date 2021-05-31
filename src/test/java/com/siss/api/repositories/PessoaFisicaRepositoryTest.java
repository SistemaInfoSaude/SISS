package com.siss.api.repositories;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.siss.api.entities.PessoaFisica;
import com.siss.api.entities.Usuario;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class PessoaFisicaRepositoryTest {

	@Autowired
	PessoaFisicaRepository pessoaFisicaRepository;
	@Autowired
	UsuarioRepository usuarioRepository;
	
	PessoaFisica pessoaFisicaTeste;
	Usuario usuarioTeste;
	
	private void CriarPessoaFisicaTestes() throws ParseException {

		pessoaFisicaTeste = new PessoaFisica();
		usuarioTeste = new Usuario();

		pessoaFisicaTeste.setId(3);
		pessoaFisicaTeste.setRg("332291399");
		pessoaFisicaTeste.setCpf("59842460026");
		pessoaFisicaTeste.setNome("Teste Teste");
		pessoaFisicaTeste.setDataNascimento(new SimpleDateFormat("dd/MM/yyyy").parse("05/04/2001"));
		
		usuarioTeste.setId(1);
		usuarioTeste.setEmail("teste1234@email.com");
		usuarioTeste.setUsuario("UsuarioLegal");
		usuarioTeste.setSenha("SenhaLegal");
		usuarioTeste.setDataCadastro(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
		usuarioTeste.setDataAlteracao(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
		
		pessoaFisicaTeste.setUsuario(usuarioTeste);
		//usuarioTeste.setPessoaFisica(pessoaFisicaTeste);
	}

	@Before
	public void setUp() throws Exception {

		CriarPessoaFisicaTestes();
		usuarioRepository.save(usuarioTeste);
		pessoaFisicaRepository.save(pessoaFisicaTeste);

	}

	@After
	public void tearDown() throws Exception {

		pessoaFisicaRepository.deleteAll();
		usuarioRepository.deleteAll();

	}

	@Test
	public void testFindByUsuarioId() {
		
		Optional<PessoaFisica> pessoaFisica = pessoaFisicaRepository.findByUsuarioId(usuarioTeste.getId());
		assertEquals(pessoaFisicaTeste.getId(), pessoaFisica.get().getId());
	}
/*	
	@Test
	public void testFindByCpf() {
		
		Optional<PessoaFisica> pessoaFisica = pessoaFisicaRepository.findByCpf(pessoaFisicaTeste.getCpf());
		assertEquals(pessoaFisicaTeste.getCpf(), pessoaFisica.get().getCpf());
	}
*/
}
