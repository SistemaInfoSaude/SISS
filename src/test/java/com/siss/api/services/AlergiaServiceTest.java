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

import com.siss.api.entities.CondicaoClinica;
import com.siss.api.entities.Alergia;
import com.siss.api.exceptions.ConsistenciaException;
import com.siss.api.repositories.CondicaoClinicaRepository;
import com.siss.api.repositories.AlergiaRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AlergiaServiceTest {

	@MockBean
	private AlergiaRepository alergiaRepository;
	@MockBean
	private CondicaoClinicaRepository condicaoClinicaRepository;

	@Autowired
	private AlergiaService alergiaService;

	@Test
	public void testBuscarPorIdExistente() throws ConsistenciaException {

		BDDMockito.given(alergiaRepository.findById(Mockito.anyInt())).willReturn(Optional.of(new Alergia()));

		Optional<Alergia> resultado = alergiaService.buscarPorId(1);

		assertTrue(resultado.isPresent());
	}

	@Test(expected = ConsistenciaException.class)
	public void testBuscarPorIdNaoExistente() throws ConsistenciaException {

		BDDMockito.given(alergiaRepository.findById(Mockito.anyInt())).willReturn(Optional.empty());

		alergiaService.buscarPorId(1);
	}

	@Test
	public void testBuscarPorCondicaoClinicaExistente() throws ConsistenciaException {

		List<Alergia> lstAlergia = new ArrayList<Alergia>();
		lstAlergia.add(new Alergia());

		BDDMockito.given(alergiaRepository.findByCondicaoClinicaId(Mockito.anyInt())).willReturn((lstAlergia));

		Optional<List<Alergia>> resultado = alergiaService.buscarPorCondicaoClinicaId(1);


		assertTrue(resultado.isPresent());
	}
 
	
 

 
	@Test(expected = ConsistenciaException.class)
	public void testBuscarPorCondicaoClinicaNaoExistente() throws ConsistenciaException {

		BDDMockito.given(alergiaRepository.findByCondicaoClinicaId(Mockito.anyInt())).willReturn(null);

		alergiaService.buscarPorCondicaoClinicaId(1);
	}
	
	@Test
	public void testSalvarComSucesso() throws ConsistenciaException {
		
		CondicaoClinica condicaoClinica = new CondicaoClinica();
		Alergia alergia = new Alergia();
		condicaoClinica.setId(1);
		alergia.setCondicaoClinica(condicaoClinica);
		
		BDDMockito.given(condicaoClinicaRepository.findById(Mockito.anyInt())).willReturn(Optional.of(condicaoClinica));
		
		BDDMockito.given(alergiaRepository.save(Mockito.any(Alergia.class))).willReturn(new Alergia());
		
		Alergia resultado = alergiaService.salvar(alergia);

		assertNotNull(resultado);
	}
	
	@Test(expected = ConsistenciaException.class)
	public void testSalvarSemSucesso() throws ConsistenciaException, ParseException {
		
		CondicaoClinica condicaoClinica = new CondicaoClinica();
		Alergia alergia = new Alergia();
		condicaoClinica.setId(0);
		alergia.setCondicaoClinica(condicaoClinica);
		alergia.setTipo("Lorem ipsum dolor sit amet");
		alergia.setDataCadastro(new SimpleDateFormat("dd/MM/yyyy").parse("12/12/2021"));
		alergia.setDataAlteracao(new SimpleDateFormat("dd/MM/yyyy").parse("12/12/2021"));
		
		BDDMockito.given(condicaoClinicaRepository.findById(Mockito.anyInt())).willReturn(Optional.empty());
		
		BDDMockito.given(alergiaRepository.save(Mockito.any(Alergia.class))).willReturn(new Alergia());
		
		alergiaService.salvar(alergia);
	}
}
