package com.siss.api.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.siss.api.entities.Usuario;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UsuarioRepositoryTest {

	@Autowired
	UsuarioRepository usuarioRepository;
	
	Usuario usuarioTeste;
	
	private void CriarUsuarioTestes() throws ParseException {
		
		usuarioTeste = new Usuario();
		
		usuarioTeste.setId(1);
		usuarioTeste.setEmail("teste123456@gmail.com");
		usuarioTeste.setUsuario("UsuarioLegal");
		usuarioTeste.setSenha("SenhaLegal");
		usuarioTeste.setDataCadastro(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
		usuarioTeste.setDataAlteracao(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));

	}
	
	@Before
	public void setUp() throws Exception {

		CriarUsuarioTestes();
		usuarioRepository.save(usuarioTeste);
	}

	@After
	public void tearDown() throws Exception {
		usuarioRepository.deleteAll();
	}
	
	@Test
	public void testfindByUsuario() {
		Usuario usuario = usuarioRepository.findByUsuario(usuarioTeste.getUsuario());
		assertTrue(usuario != null);
	}
	@Test
	public void testAlterarSenhaUsuario() {
		String novasenha = "novasenha123";
		usuarioRepository.alterarSenhaUsuario(novasenha, usuarioTeste.getId());
	}
}
