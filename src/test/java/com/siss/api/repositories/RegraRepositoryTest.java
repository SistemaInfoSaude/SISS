package com.siss.api.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.siss.api.entities.Regra;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class RegraRepositoryTest {

	@Autowired
	RegraRepository regraRepository;
	
	Regra regraTeste;
	
	private void CriarRegraTestes() throws ParseException {
		
		regraTeste = new Regra();
		
		regraTeste.setId(1);
		regraTeste.setDescricao("Alguma descricao muito interessante");
		regraTeste.setNome("Regra 1");
		regraTeste.setAtivo(true);
	}
	
	@Before
	public void setUp() throws Exception {

		CriarRegraTestes();
		regraRepository.save(regraTeste);
	}

	@After
	public void tearDown() throws Exception {
		regraRepository.deleteAll();
	}
	
	@Test
	public void testfindByNome() {
		Regra regra = regraRepository.findByNome(regraTeste.getNome());
		assertTrue(regra != null);
	}
}
