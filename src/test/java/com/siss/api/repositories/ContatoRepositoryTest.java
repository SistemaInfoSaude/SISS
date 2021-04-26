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
import com.siss.api.entities.Contato;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ContatoRepositoryTest {

	@Autowired
	ContatoRepository contatoRepository;
	@Autowired
	UsuarioRepository usuarioRepository;
	
	Contato contatoTeste;
	Usuario usuarioTeste;
	
	private void CriarContatoTestes() throws ParseException {
		
		usuarioTeste = new Usuario();
		contatoTeste = new Contato();
		
		usuarioTeste.setId(1);
		usuarioTeste.setUsuario("UsuarioLegal");
		usuarioTeste.setSenha("SenhaLegal");
		usuarioTeste.setDataCadastro(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
		usuarioTeste.setDataAlteracao(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
		
		contatoTeste.setId(1);
		contatoTeste.setNome("Nome do Usuario");
		contatoTeste.setTelefone("3335846725");
		contatoTeste.setCelular("9995846725");
		contatoTeste.setDataCadastro(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
		contatoTeste.setDataAlteracao(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
		contatoTeste.setUsuario(usuarioTeste);
		
		List<Contato> contatos = new ArrayList<Contato>();
		
		usuarioTeste.setContatos(contatos);
	}
	@Before
	public void setUp() throws Exception {

		CriarContatoTestes();
		usuarioRepository.save(usuarioTeste);
		contatoRepository.save(contatoTeste);

	}

	@After
	public void tearDown() throws Exception {

		usuarioRepository.deleteAll();
		contatoRepository.deleteAll();

	}
	@Test
	public void testfindByUsuarioId() {
		List<Contato> contatos = contatoRepository.findByUsuarioId(usuarioTeste.getId());
		assertTrue(!contatos.isEmpty());
	}
}
