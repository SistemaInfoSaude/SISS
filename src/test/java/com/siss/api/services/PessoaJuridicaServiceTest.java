package com.siss.api.services;

import static org.junit.Assert.*;

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

import com.siss.api.entities.PessoaJuridica;
import com.siss.api.entities.Usuario;
import com.siss.api.exceptions.ConsistenciaException;
import com.siss.api.repositories.PessoaJuridicaRepository;
import com.siss.api.repositories.UsuarioRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class PessoaJuridicaServiceTest {


	@MockBean
	private PessoaJuridicaRepository pessoaJuridicaRepository;
	@MockBean
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PessoaJuridicaService pessoaJuridicaService;

	@Test
	public void testBuscarPorIdExistente() throws ConsistenciaException {

		BDDMockito.given(pessoaJuridicaRepository.findById(Mockito.anyInt())).willReturn(Optional.of(new PessoaJuridica()));

		Optional<PessoaJuridica> resultado = pessoaJuridicaService.buscarPorId(1);

		assertTrue(resultado.isPresent());
	}

	@Test(expected = ConsistenciaException.class)
	public void testBuscarPorIdNaoExistente() throws ConsistenciaException {

		BDDMockito.given(pessoaJuridicaRepository.findById(Mockito.anyInt())).willReturn(Optional.empty());

		pessoaJuridicaService.buscarPorId(1);
	}
	
	@Test
	public void testSalvarComSucesso() throws ConsistenciaException {
		
		PessoaJuridica pessoaJuridica= new PessoaJuridica();
		Usuario usuario = new Usuario();
		pessoaJuridica.setId(1);
		pessoaJuridica.setUsuario(usuario);
		
		BDDMockito.given(pessoaJuridicaRepository.findById(Mockito.anyInt())).willReturn(Optional.of(pessoaJuridica));
		
		BDDMockito.given(usuarioRepository.findById(Mockito.anyInt())).willReturn(Optional.of(usuario));
		
		BDDMockito.given(pessoaJuridicaRepository.save(Mockito.any(PessoaJuridica.class))).willReturn(new PessoaJuridica());
		
		PessoaJuridica resultado = pessoaJuridicaService.salvar(pessoaJuridica);

		assertNotNull(resultado);
	}
	
	@Test(expected = ConsistenciaException.class)
	public void testSalvarSemSucesso() throws ConsistenciaException {
		
		PessoaJuridica pessoaJuridica = new PessoaJuridica();
		Usuario usuario = new Usuario();
		pessoaJuridica.setId(0);
		pessoaJuridica.setUsuario(usuario);
		
		BDDMockito.given(pessoaJuridicaRepository.findById(Mockito.anyInt())).willReturn(Optional.empty());
		
		BDDMockito.given(usuarioRepository.findById(Mockito.anyInt())).willReturn(Optional.empty());
		
		BDDMockito.given(usuarioRepository.save(Mockito.any(Usuario.class))).willReturn(new Usuario());
		
		pessoaJuridicaService.salvar(pessoaJuridica);
	}
}
