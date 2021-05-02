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

import com.siss.api.entities.Usuario;
import com.siss.api.entities.Veiculo;
import com.siss.api.exceptions.ConsistenciaException;
import com.siss.api.repositories.UsuarioRepository;
import com.siss.api.repositories.VeiculoRepository;



@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class VeiculoServiceTest {
	
	@MockBean
	private VeiculoRepository veiculoRepository;
	@MockBean
	private UsuarioRepository usuarioRepository;
	
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

		BDDMockito.given(veiculoRepository.findByUsuarioId(Mockito.anyInt())).willReturn((lstVeiculo));

		Optional<List<Veiculo>> resultado = veiculoService.buscarPorUsuarioId(1);

		assertTrue(resultado.isPresent());
	}
	@Test(expected = ConsistenciaException.class)
	public void testBuscarPorVeiculoIdSemSucesso() throws ConsistenciaException {
		
		BDDMockito.given(veiculoRepository.findByUsuarioId(Mockito.anyInt())).willReturn(null);

		veiculoService.buscarPorUsuarioId(1);
	}
	@Test
	public void testSalvarComSucesso() throws ConsistenciaException {
		
		Usuario usuario = new Usuario();
		Veiculo veiculo = new Veiculo();
		usuario.setId(1);
		veiculo.setUsuario(usuario);
		
		BDDMockito.given(usuarioRepository.findById(Mockito.anyInt())).willReturn(Optional.of(usuario));
		
		BDDMockito.given(veiculoRepository.save(Mockito.any(Veiculo.class))).willReturn(new Veiculo());
		
		Veiculo resultado = veiculoService.salvar(veiculo);

		assertNotNull(resultado);
	}
	@Test(expected = ConsistenciaException.class)
	public void testSalvarSemSucesso() throws ConsistenciaException {
		
		Usuario usuario = new Usuario();
		Veiculo veiculo = new Veiculo();
		usuario.setId(0);
		veiculo.setUsuario(usuario);
		veiculo.setPlaca("12315645648947987897988789789");
		
		BDDMockito.given(usuarioRepository.findById(Mockito.anyInt())).willReturn(Optional.empty());
		
		BDDMockito.given(veiculoRepository.save(Mockito.any(Veiculo.class))).willReturn(new Veiculo());
		
		veiculoService.salvar(veiculo);
	}
}