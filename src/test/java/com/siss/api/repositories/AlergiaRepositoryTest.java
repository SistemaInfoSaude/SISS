package com.siss.api.repositories;

import static org.junit.jupiter.api.Assertions.*;

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
import com.siss.api.entities.Alergia;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AlergiaRepositoryTest {

	@Autowired
	AlergiaRepository alergiaRepository;
	@Autowired
	CondicaoClinicaRepository condicaoClinicaRepository;
	
	Alergia alergiaTeste;
	CondicaoClinica condicaoClinicaTeste;
	
	private void CriarAlergiaTestes() throws ParseException {
		
		alergiaTeste = new Alergia();
		condicaoClinicaTeste = new CondicaoClinica();
		
		condicaoClinicaTeste.setId(1);
		
		alergiaTeste.setId(1);
		alergiaTeste.setDataCadastro(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
		alergiaTeste.setDataAlteracao(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
		alergiaTeste.setCondicaoClinica(condicaoClinicaTeste);
		
	}
	
	@Before
	public void setUp() throws Exception {

		CriarAlergiaTestes();
		condicaoClinicaRepository.save(condicaoClinicaTeste);
		alergiaRepository.save(alergiaTeste);

	}

	@After
	public void tearDown() throws Exception {

		alergiaRepository.deleteAll();

	}
	
	@Test
	public void testFindByCondicaoClinicaId() {
		List<Alergia> alergias = alergiaRepository.findByCondicaoClinicaId(condicaoClinicaTeste.getId());
		assertTrue(!alergias.isEmpty());
	}

}
