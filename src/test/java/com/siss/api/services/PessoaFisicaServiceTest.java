package com.siss.api.services;

import static org.junit.Assert.*;

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

import com.siss.api.entities.PessoaFisica;
import com.siss.api.entities.Usuario;
import com.siss.api.exceptions.ConsistenciaException;
import com.siss.api.repositories.PessoaFisicaRepository;
import com.siss.api.repositories.UsuarioRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class PessoaFisicaServiceTest {


	@MockBean
	private PessoaFisicaRepository pessoaFisicaRepository;
	@MockBean
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PessoaFisicaService pessoaFisicaService;

	@Test
	public void testBuscarPorIdExistente() throws ConsistenciaException {

		BDDMockito.given(pessoaFisicaRepository.findById(Mockito.anyInt())).willReturn(Optional.of(new PessoaFisica()));

		Optional<PessoaFisica> resultado = pessoaFisicaService.buscarPorId(1);

		assertTrue(resultado.isPresent());
	}

	@Test(expected = ConsistenciaException.class)
	public void testBuscarPorIdNaoExistente() throws ConsistenciaException {

		BDDMockito.given(pessoaFisicaRepository.findById(Mockito.anyInt())).willReturn(Optional.empty());

		pessoaFisicaService.buscarPorId(1);
	}
	
	@Test
	public void testBuscarPorCpfExistente() throws ConsistenciaException {	
		
		BDDMockito.given(pessoaFisicaRepository.findByCpf(Mockito.anyString()))
			.willReturn(Optional.of(new PessoaFisica()));
		
		Optional<PessoaFisica> resultado = pessoaFisicaService.buscarPorCpf("99930632077");
		
		assertTrue(resultado.isPresent());
		
	}
	
	@Test(expected = ConsistenciaException.class)
	public void testBuscarPorCpfNaoExistente() throws ConsistenciaException {	
		
		BDDMockito.given(pessoaFisicaRepository.findByCpf(Mockito.anyString()))
		.willReturn(Optional.empty());
		
		pessoaFisicaService.buscarPorCpf("99930632077");
		
	}
	
	@Test
	public void testBuscarPorRGExistente() throws ConsistenciaException {	
		
		BDDMockito.given(pessoaFisicaRepository.findByRg(Mockito.anyString()))
			.willReturn(Optional.of(new PessoaFisica()));
		
		Optional<PessoaFisica> resultado = pessoaFisicaService.buscarPorRg("999306320");
		
		assertTrue(resultado.isPresent());
		
	}
	
	@Test(expected = ConsistenciaException.class)
	public void testBuscarPorRGNaoExistente() throws ConsistenciaException {	
		
		BDDMockito.given(pessoaFisicaRepository.findByRg(Mockito.anyString()))
		.willReturn(Optional.empty());
		
		pessoaFisicaService.buscarPorRg("999306320");
		
	}
	
	@Test
	public void testBuscarPorUsuarioIdExistente() throws ConsistenciaException {

		BDDMockito.given(pessoaFisicaRepository.findByUsuarioId(Mockito.anyInt())).willReturn(Optional.of(new PessoaFisica()));

		Optional<PessoaFisica> resultado = pessoaFisicaService.buscarPorUsuarioId(1);

		assertTrue(resultado.isPresent());
	}

	@Test(expected = ConsistenciaException.class)
	public void testBuscarPorUsuarioIdNaoExistente() throws ConsistenciaException {

		BDDMockito.given(pessoaFisicaRepository.findByUsuarioId(Mockito.anyInt())).willReturn(Optional.empty());

		pessoaFisicaService.buscarPorUsuarioId(1);
	}
	
	@Test
	public void testBuscarPorPlacaVeiculoExistente() throws ConsistenciaException {

		List<PessoaFisica> lstPessoaFisica = new ArrayList<PessoaFisica>();
		lstPessoaFisica.add(new PessoaFisica());

		BDDMockito.given(pessoaFisicaRepository.findByVeiculoPlaca(Mockito.anyString())).willReturn(Optional.of(lstPessoaFisica));

		Optional<List<PessoaFisica>> resultado = pessoaFisicaService.buscarPorPlacaVeiculo("ABC12345");

		assertTrue(resultado.isPresent());
	}
	@Test(expected = ConsistenciaException.class)
	public void testBuscarPorPlacaVeiculoNaoExistente() throws ConsistenciaException {
		
		BDDMockito.given(pessoaFisicaRepository.findByVeiculoPlaca(Mockito.anyString())).willReturn(Optional.empty());

		pessoaFisicaService.buscarPorPlacaVeiculo("ABC12345");
	}
	
	@Test
	public void testSalvarComSucesso() throws ConsistenciaException {
		
		PessoaFisica pessoaFisica = new PessoaFisica();
		Usuario usuario = new Usuario();
		pessoaFisica.setId(1);
		pessoaFisica.setUsuario(usuario);
		
		BDDMockito.given(pessoaFisicaRepository.findById(Mockito.anyInt())).willReturn(Optional.of(pessoaFisica));
		
		BDDMockito.given(usuarioRepository.findById(Mockito.anyInt())).willReturn(Optional.of(usuario));
		
		BDDMockito.given(pessoaFisicaRepository.save(Mockito.any(PessoaFisica.class))).willReturn(new PessoaFisica());
		
		PessoaFisica resultado = pessoaFisicaService.salvar(pessoaFisica);

		assertNotNull(resultado);
	}
	@Test(expected = ConsistenciaException.class)
	public void testSalvarSemSucesso() throws ConsistenciaException {
		
		PessoaFisica pessoaFisica = new PessoaFisica();
		Usuario usuario = new Usuario();
		pessoaFisica.setId(0);
		pessoaFisica.setUsuario(usuario);
		
		BDDMockito.given(pessoaFisicaRepository.findById(Mockito.anyInt())).willReturn(Optional.empty());
		
		BDDMockito.given(usuarioRepository.findById(Mockito.anyInt())).willReturn(Optional.empty());
		
		BDDMockito.given(usuarioRepository.save(Mockito.any(Usuario.class))).willReturn(new Usuario());
		
		pessoaFisicaService.salvar(pessoaFisica);
	}
}
