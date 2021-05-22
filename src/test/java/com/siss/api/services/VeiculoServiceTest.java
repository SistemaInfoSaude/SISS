package com.siss.api.services;

import static org.junit.Assert.assertNotNull;
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

import com.siss.api.entities.PessoaFisica;
import com.siss.api.entities.Veiculo;
import com.siss.api.exceptions.ConsistenciaException;
import com.siss.api.repositories.PessoaFisicaRepository;
import com.siss.api.repositories.VeiculoRepository;



@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class VeiculoServiceTest {
	
	@MockBean
	private VeiculoRepository veiculoRepository;
	@MockBean
	private PessoaFisicaRepository pessoaFisicaRepository;
	
	@Autowired
	private VeiculoService veiculoService;

	@Test
	public void testBuscarPorIdExistente() throws ConsistenciaException {
		
		BDDMockito.given(veiculoRepository.findById(Mockito.anyInt())).willReturn(Optional.of(new Veiculo()));

		Optional<Veiculo> resultado = veiculoService.buscarPorId(1);

		assertTrue(resultado.isPresent());
	}
	@Test(expected = ConsistenciaException.class)
	public void testBuscarPorIdInexistente() throws ConsistenciaException {

		BDDMockito.given(veiculoRepository.findById(Mockito.anyInt())).willReturn(Optional.empty());

		veiculoService.buscarPorId(1);
	}
	@Test
	public void testBuscarPorVeiculoIdComSucesso() throws ConsistenciaException {

		List<Veiculo> lstVeiculo = new ArrayList<Veiculo>();
		lstVeiculo.add(new Veiculo());

		BDDMockito.given(veiculoRepository.findByPessoaFisicaId(Mockito.anyInt())).willReturn((lstVeiculo));

		Optional<List<Veiculo>> resultado = veiculoService.buscarPorPessoaFisicaId(1);

		assertTrue(resultado.isPresent());
	}
	@Test(expected = ConsistenciaException.class)
	public void testBuscarPorVeiculoIdSemSucesso() throws ConsistenciaException {
		
		BDDMockito.given(veiculoRepository.findByPessoaFisicaId(Mockito.anyInt())).willReturn(null);

		veiculoService.buscarPorPessoaFisicaId(1);
	}
	@Test
	public void testSalvarComSucesso() throws ConsistenciaException {
		
		PessoaFisica pessoaFisica = new PessoaFisica();
		Veiculo veiculo = new Veiculo();
		pessoaFisica.setId(1);
		veiculo.setPessoaFisica(pessoaFisica);
		
		BDDMockito.given(pessoaFisicaRepository.findById(Mockito.anyInt())).willReturn(Optional.of(pessoaFisica));
		
		BDDMockito.given(veiculoRepository.save(Mockito.any(Veiculo.class))).willReturn(new Veiculo());
		
		Veiculo resultado = veiculoService.salvar(veiculo);

		assertNotNull(resultado);
	}
	@Test(expected = ConsistenciaException.class)
	public void testSalvarSemSucesso() throws ConsistenciaException {
		
		PessoaFisica pessoaFisica = new PessoaFisica();
		Veiculo veiculo = new Veiculo();
		pessoaFisica.setId(0);
		veiculo.setPessoaFisica(pessoaFisica);
		veiculo.setPlaca("12315645648947987897988789789");
		
		BDDMockito.given(pessoaFisicaRepository.findById(Mockito.anyInt())).willReturn(Optional.empty());
		
		BDDMockito.given(veiculoRepository.save(Mockito.any(Veiculo.class))).willReturn(new Veiculo());
		
		veiculoService.salvar(veiculo);
	}
}