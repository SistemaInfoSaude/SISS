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
import com.siss.api.entities.Doenca;
import com.siss.api.exceptions.ConsistenciaException;
import com.siss.api.repositories.CondicaoClinicaRepository;
import com.siss.api.repositories.DoencaRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class DoencaServiceTest {

	@MockBean
	private DoencaRepository doencaRepository;
	@MockBean
	private CondicaoClinicaRepository condicaoClinicaRepository;

	@Autowired
	private DoencaService doencaService;

	@Test
	public void testBuscarPorIdExistente() throws ConsistenciaException {

		BDDMockito.given(doencaRepository.findById(Mockito.anyInt())).willReturn(Optional.of(new Doenca()));

		Optional<Doenca> resultado = doencaService.buscarPorId(1);

		assertTrue(resultado.isPresent());
	}

	@Test(expected = ConsistenciaException.class)
	public void testBuscarPorIdNaoExistente() throws ConsistenciaException {

		BDDMockito.given(doencaRepository.findById(Mockito.anyInt())).willReturn(Optional.empty());

		doencaService.buscarPorId(1);
	}

	@Test
	public void testBuscarPorCondicaoClinicaExistente() throws ConsistenciaException {

		List<Doenca> lstDoenca = new ArrayList<Doenca>();
		lstDoenca.add(new Doenca());

		BDDMockito.given(doencaRepository.findByCondicaoClinicaId(Mockito.anyInt())).willReturn((lstDoenca));

		Optional<List<Doenca>> resultado = doencaService.buscarPorCondicaoClinicaId(1);


		assertTrue(resultado.isPresent());
	}
 
	
 

 
	@Test(expected = ConsistenciaException.class)
	public void testBuscarPorCondicaoClinicaNaoExistente() throws ConsistenciaException {

		BDDMockito.given(doencaRepository.findByCondicaoClinicaId(Mockito.anyInt())).willReturn(null);

		doencaService.buscarPorCondicaoClinicaId(1);
	}
	
	@Test
	public void testSalvarComSucesso() throws ConsistenciaException {
		
		CondicaoClinica condicaoClinica = new CondicaoClinica();
		Doenca doenca = new Doenca();
		condicaoClinica.setId(1);
		doenca.setCondicaoClinica(condicaoClinica);
		
		BDDMockito.given(condicaoClinicaRepository.findById(Mockito.anyInt())).willReturn(Optional.of(condicaoClinica));
		
		BDDMockito.given(doencaRepository.save(Mockito.any(Doenca.class))).willReturn(new Doenca());
		
		Doenca resultado = doencaService.salvar(doenca);

		assertNotNull(resultado);
	}
	
	@Test(expected = ConsistenciaException.class)
	public void testSalvarSemSucesso() throws ConsistenciaException, ParseException {
		
		CondicaoClinica condicaoClinica = new CondicaoClinica();
		Doenca doenca = new Doenca();
		condicaoClinica.setId(0);
		doenca.setCondicaoClinica(condicaoClinica);
		doenca.setTipo("Lorem ipsum dolor sit amet");
		doenca.setDataCadastro(new SimpleDateFormat("dd/MM/yyyy").parse("12/12/2021"));
		doenca.setDataAlteracao(new SimpleDateFormat("dd/MM/yyyy").parse("12/12/2021"));
		
		BDDMockito.given(condicaoClinicaRepository.findById(Mockito.anyInt())).willReturn(Optional.empty());
		
		BDDMockito.given(doencaRepository.save(Mockito.any(Doenca.class))).willReturn(new Doenca());
		
		doencaService.salvar(doenca);
	}

 

}
