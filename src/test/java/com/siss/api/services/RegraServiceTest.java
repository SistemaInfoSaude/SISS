package com.siss.api.services;

import static org.junit.Assert.assertTrue;

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

import com.siss.api.entities.Regra;
import com.siss.api.exceptions.ConsistenciaException;
import com.siss.api.repositories.RegraRepository;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class RegraServiceTest {

	@MockBean
	private RegraRepository regraRepository;

	@Autowired
	private RegraService regraService;

	@Test
	public void testBuscarPorIdExistente() throws ConsistenciaException {

		BDDMockito.given(regraRepository.findById(Mockito.anyInt())).willReturn(Optional.of(new Regra()));

		Optional<Regra> resultado = regraService.buscarPorId(1);

		assertTrue(resultado.isPresent());
	}

	@Test(expected = ConsistenciaException.class)
	public void testBuscarPorIdNaoExistente() throws ConsistenciaException {

		BDDMockito.given(regraRepository.findById(Mockito.anyInt())).willReturn(Optional.empty());

		regraService.buscarPorId(1);
	}

	@Test
	public void testBuscarPorNomeExistente() throws ConsistenciaException {

		BDDMockito.given(regraRepository.findByNome(Mockito.anyString())).willReturn(new Regra());

		Optional<Regra> resultado = regraService.buscarPorNome("Luizão");

		assertTrue(resultado.isPresent());
	}
 
	
 

 
	@Test(expected = ConsistenciaException.class)
	public void testBuscarPorNomeNaoExistente() throws ConsistenciaException {

		BDDMockito.given(regraRepository.findByNome(Mockito.anyString())).willReturn(null);

		regraService.buscarPorNome("Godofredo");
	}
 
	
 

 
	@Test
	public void testBuscarTodasRegras() throws ConsistenciaException{

		List<Regra> lstRegra = new ArrayList<Regra>();
		lstRegra.add(new Regra());

		BDDMockito.given(regraRepository.findAll()).willReturn((lstRegra));

		Optional<List<Regra>> resultado = regraService.buscarTodasAsRegras();

		assertTrue(resultado.isPresent());
	}
	@Test(expected = ConsistenciaException.class)
	public void testBuscarTodasRegrasNaoEncontrado() throws ConsistenciaException{

		BDDMockito.given(regraRepository.findAll()).willReturn((null));

		regraService.buscarTodasAsRegras();

	}
 
}
 

