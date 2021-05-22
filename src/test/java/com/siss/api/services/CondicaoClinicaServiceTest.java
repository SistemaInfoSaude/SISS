package com.siss.api.services;

import static org.junit.Assert.*;

import java.text.ParseException;
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

import com.siss.api.entities.Alergia;
import com.siss.api.entities.CondicaoClinica;
import com.siss.api.entities.Doenca;
import com.siss.api.entities.PessoaFisica;
import com.siss.api.exceptions.ConsistenciaException;
import com.siss.api.repositories.CondicaoClinicaRepository;
import com.siss.api.repositories.PessoaFisicaRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class CondicaoClinicaServiceTest {

	@MockBean
	private CondicaoClinicaRepository condicaoClinicaRepository;
	@MockBean
	private PessoaFisicaRepository pessoaFisicaRepository;

	@Autowired
	private CondicaoClinicaService condicaoClinicaService;

	@Test
	public void testBuscarPorIdExistente() throws ConsistenciaException {

		BDDMockito.given(condicaoClinicaRepository.findById(Mockito.anyInt())).willReturn(Optional.of(new CondicaoClinica()));

		Optional<CondicaoClinica> resultado = condicaoClinicaService.buscarPorId(1);

		assertTrue(resultado.isPresent());
	}

	@Test(expected = ConsistenciaException.class)
	public void testBuscarPorIdNaoExistente() throws ConsistenciaException {

		BDDMockito.given(condicaoClinicaRepository.findById(Mockito.anyInt())).willReturn(Optional.empty());

		condicaoClinicaService.buscarPorId(1);
	}
	
	@Test
	public void testSalvarComSucesso() throws ConsistenciaException {
		
		PessoaFisica pessoaFisica = new PessoaFisica();
		CondicaoClinica condicaoClinica = new CondicaoClinica();
		pessoaFisica.setId(1);
		condicaoClinica.setPessoaFisica(pessoaFisica);
		
		BDDMockito.given(pessoaFisicaRepository.findById(Mockito.anyInt())).willReturn(Optional.of(pessoaFisica));
		
		BDDMockito.given(condicaoClinicaRepository.save(Mockito.any(CondicaoClinica.class))).willReturn(new CondicaoClinica());
		
		CondicaoClinica resultado = condicaoClinicaService.salvar(condicaoClinica);

		assertNotNull(resultado);
	}
	
	@Test(expected = ConsistenciaException.class)
	public void testSalvarSemSucesso() throws ConsistenciaException, ParseException {
		
		PessoaFisica pessoaFisica = new PessoaFisica();
		CondicaoClinica condicaoClinica = new CondicaoClinica();
		List<Alergia> alergias = new ArrayList<Alergia>();
		List<Doenca> doencas = new ArrayList<Doenca>();
		pessoaFisica.setId(0);
		condicaoClinica.setPessoaFisica(pessoaFisica);
		condicaoClinica.setAlergias(alergias);
		condicaoClinica.setDoencas(doencas);
		
		BDDMockito.given(pessoaFisicaRepository.findById(Mockito.anyInt())).willReturn(Optional.empty());
		
		BDDMockito.given(condicaoClinicaRepository.save(Mockito.any(CondicaoClinica.class))).willReturn(new CondicaoClinica());
		
		condicaoClinicaService.salvar(condicaoClinica);
	}
}
