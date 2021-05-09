package com.siss.api.services;

import static org.junit.Assert.*;

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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.siss.api.entities.Usuario;
import com.siss.api.entities.Contato;
import com.siss.api.exceptions.ConsistenciaException;
import com.siss.api.repositories.UsuarioRepository;
import com.siss.api.repositories.ContatoRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ContatoServiceTest {

	@MockBean
	private ContatoRepository contatoRepository;
	@MockBean
	private UsuarioRepository usuarioRepository;

	@Autowired
	private ContatoService contatoService;

	@Test
	public void testBuscarPorIdExistente() throws ConsistenciaException {

		BDDMockito.given(contatoRepository.findById(Mockito.anyInt())).willReturn(Optional.of(new Contato()));

		Optional<Contato> resultado = contatoService.buscarPorId(1);

		assertTrue(resultado.isPresent());
	}

	@Test(expected = ConsistenciaException.class)
	public void testBuscarPorIdNaoExistente() throws ConsistenciaException {

		BDDMockito.given(contatoRepository.findById(Mockito.anyInt())).willReturn(Optional.empty());

		contatoService.buscarPorId(1);
	}

	@Test
	public void testBuscarPorUsuarioIdExistente() throws ConsistenciaException {

		List<Contato> lstContato = new ArrayList<Contato>();
		lstContato.add(new Contato());

		BDDMockito.given(contatoRepository.findByUsuarioId(Mockito.anyInt())).willReturn((lstContato));

		Optional<List<Contato>> resultado = contatoService.buscarPorUsuarioId(1);


		assertTrue(resultado.isPresent());
	}
 
	
 

 
	@Test(expected = ConsistenciaException.class)
	public void testBuscarPorUsuarioIdNaoExistente() throws ConsistenciaException {

		BDDMockito.given(contatoRepository.findByUsuarioId(Mockito.anyInt())).willReturn(null);

		contatoService.buscarPorUsuarioId(1);
	}
	
	@Test
	public void testSalvarComSucesso() throws ConsistenciaException {
		
		Usuario usuario = new Usuario();
		Contato contato = new Contato();
		usuario.setId(1);
		contato.setUsuario(usuario);
		
		BDDMockito.given(usuarioRepository.findById(Mockito.anyInt())).willReturn(Optional.of(usuario));
		
		BDDMockito.given(contatoRepository.save(Mockito.any(Contato.class))).willReturn(new Contato());
		
		Contato resultado = contatoService.salvar(contato);

		assertNotNull(resultado);
	}
	
	@Test(expected = ConsistenciaException.class)
	public void testSalvarSemSucesso() throws ConsistenciaException, ParseException {
		
		Usuario usuario = new Usuario();
		Contato contato = new Contato();
		usuario.setId(0);
		contato.setUsuario(usuario);
		contato.setNome("Lorem ipsum dolor sit amet");
		contato.setCelular("99956548722");
		contato.setTelefone("3356548722");
		contato.setDataCadastro(new SimpleDateFormat("dd/MM/yyyy").parse("12/12/2021"));
		contato.setDataAlteracao(new SimpleDateFormat("dd/MM/yyyy").parse("12/12/2021"));
		
		BDDMockito.given(usuarioRepository.findById(Mockito.anyInt())).willReturn(Optional.empty());
		
		BDDMockito.given(contatoRepository.save(Mockito.any(Contato.class))).willReturn(new Contato());
		
		contatoService.salvar(contato);
	}
}
