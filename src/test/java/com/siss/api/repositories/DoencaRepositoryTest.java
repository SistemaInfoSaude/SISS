package com.siss.api.repositories;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.siss.api.entities.CondicaoClinica;
import com.siss.api.entities.Doenca;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class DoencaRepositoryTest {

	@Autowired
	DoencaRepository doencaRepository;
	@Autowired
	CondicaoClinicaRepository condicaoClinicaRepository;
	
	Doenca doencaTeste;
	CondicaoClinica condicaoClinicaTeste;
	
	private void CriarDoencaTestes() throws ParseException {
		
		doencaTeste = new Doenca();
		condicaoClinicaTeste = new CondicaoClinica();
		
		condicaoClinicaTeste.setId(1);
		
		doencaTeste.setId(1);
		doencaTeste.setDataCadastro(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
		doencaTeste.setDataAlteracao(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
		doencaTeste.setCondicaoClinica(condicaoClinicaTeste);
		
	}
	@Before
	public void setUp() throws Exception {

		CriarDoencaTestes();
		condicaoClinicaRepository.save(condicaoClinicaTeste);
		doencaRepository.save(doencaTeste);

	}

	@After
	public void tearDown() throws Exception {

		doencaRepository.deleteAll();

	}
	@Test
	public void testFindByCondicaoClinicaId() {
		List<Doenca> doencas = doencaRepository.findByCondicaoClinicaId(condicaoClinicaTeste.getId());
		assertTrue(!doencas.isEmpty());
	}

}
