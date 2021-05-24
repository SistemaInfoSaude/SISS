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

import com.siss.api.dtos.CodigoEmailDto;
import com.siss.api.entities.Regra;
import com.siss.api.entities.Usuario;
import com.siss.api.exceptions.ConsistenciaException;
import com.siss.api.repositories.RegraRepository;
import com.siss.api.repositories.UsuarioRepository;
import com.siss.api.security.utils.JwtTokenUtil;
import com.siss.api.utils.SenhaUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UsuarioServiceTest {
	
	@MockBean
	private UsuarioRepository usuarioRepository;
	@MockBean
	private RegraRepository regraRepository;

	@MockBean
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	UsuarioService usuarioService;
	
	@Test
	public void testBuscarPorIdExistente() throws ConsistenciaException {
		
		BDDMockito.given(usuarioRepository.findById(Mockito.anyInt())).willReturn(Optional.of(new Usuario()));

		Optional<Usuario> resultado = usuarioService.buscarPorId(1);

		assertTrue(resultado.isPresent());
	}
	@Test(expected = ConsistenciaException.class)
	public void testBuscarPorIdInexistente() throws ConsistenciaException {

		BDDMockito.given(usuarioRepository.findById(Mockito.anyInt())).willReturn(Optional.empty());

		usuarioService.buscarPorId(1);
	}
	
	@Test
	public void testBuscarPorEmailExistente() throws ConsistenciaException {
		
		BDDMockito.given(usuarioRepository.findByEmail(Mockito.anyString())).willReturn(Optional.of(new Usuario()));

		Optional<Usuario> resultado = usuarioService.buscarPorEmail("emailtest@email.com");

		assertTrue(resultado.isPresent());
	}
	@Test(expected = ConsistenciaException.class)
	public void testBuscarPorEmailInexistente() throws ConsistenciaException {

		BDDMockito.given(usuarioRepository.findByEmail(Mockito.anyString())).willReturn(Optional.empty());

		usuarioService.buscarPorEmail("emailtest@email.com");
	}
	
	@Test
	public void testVerificarCredenciaisValida() throws ConsistenciaException {
		
		Usuario usuario = new Usuario();
		Regra regraTeste = new Regra();
		List<Regra> regras = new ArrayList<Regra>();
		regraTeste.setId(1);
		regraTeste.setAtivo(true);
		regras.add(regraTeste);
		usuario.setRegras(regras);
		
		BDDMockito.given(usuarioRepository.findByUsuario(Mockito.anyString())).willReturn(usuario);
		
		Optional<Usuario> resultado = usuarioService.verificarCredenciais("Usuario Teste");
		
		assertTrue(resultado.isPresent());
	}
	@Test(expected = ConsistenciaException.class)
	public void testVerificarCredenciaisInvalida() throws ConsistenciaException {
		
		BDDMockito.given(usuarioRepository.findByUsuario(Mockito.anyString())).willReturn(null);
		
		usuarioService.verificarCredenciais("Usuario Teste");
	}
	
	@Test
	public void testSalvarAlteracaoComSucesso() throws ConsistenciaException {
		
		Usuario usuario = new Usuario();
		Regra regraTeste = new Regra();
		usuario.setId(1);
		usuario.setSenha("SenharLegal123");
		List<Regra> regras = new ArrayList<Regra>();
		regraTeste.setId(1);
		regraTeste.setNome("RegraTeste");
		regraTeste.setAtivo(true);
		regras.add(regraTeste);
		usuario.setRegras(regras);
		
		BDDMockito.given(usuarioRepository.findById(Mockito.anyInt())).willReturn(Optional.of(usuario));
		
		BDDMockito.given(regraRepository.findByNome(Mockito.anyString())).willReturn(regraTeste);
		
		BDDMockito.given(usuarioRepository.save(Mockito.any(Usuario.class))).willReturn(new Usuario());
		
		Usuario resultado = usuarioService.salvar(usuario);
		
		assertNotNull(resultado);
	}
	@Test(expected = ConsistenciaException.class)
	public void testSalvarAlteracaoSemSucesso() throws ConsistenciaException {
		
		Usuario usuario = new Usuario();
		Regra regraTeste = new Regra();
		usuario.setId(1);
		usuario.setSenha("SenharLegal123");
		List<Regra> regras = new ArrayList<Regra>();
		regraTeste.setId(1);
		regraTeste.setNome("RegraTeste");
		regraTeste.setAtivo(true);
		regras.add(regraTeste);
		usuario.setRegras(regras);
		
		BDDMockito.given(usuarioRepository.findById(Mockito.anyInt())).willReturn(Optional.empty());
		
		BDDMockito.given(regraRepository.findByNome(Mockito.anyString())).willReturn(regraTeste);
		
		BDDMockito.given(usuarioRepository.save(Mockito.any(Usuario.class))).willReturn(new Usuario());
		
		usuarioService.salvar(usuario);
	}
	@Test
	public void testSalvarComSucesso() throws ConsistenciaException {
		
		Usuario usuario = new Usuario();
		usuario.setSenha("SenharLegal123");
		usuario.setExecutante(true);
	
		
		BDDMockito.given(regraRepository.findByNome(Mockito.anyString())).willReturn(new Regra());
		
		BDDMockito.given(usuarioRepository.save(Mockito.any(Usuario.class))).willReturn(new Usuario());
		
		Usuario resultado = usuarioService.salvar(usuario);
		
		assertNotNull(resultado);
	}
	@Test(expected = ConsistenciaException.class)
	public void testSalvarSemSucesso() throws ConsistenciaException {
		
		Usuario usuario = new Usuario();
		usuario.setSenha("SenharLegal123");
		usuario.setExecutante(true);
	
		BDDMockito.given(regraRepository.findByNome(Mockito.anyString())).willReturn(null);
		
		BDDMockito.given(usuarioRepository.save(Mockito.any(Usuario.class))).willReturn(new Usuario());
		
		usuarioService.salvar(usuario);
	}
	@Test
	public void testEnviarCodigoAlteracaoSenhaVerdadeiro() throws ConsistenciaException {
		
		Usuario usuario = new Usuario();
		usuario.setId(1);
		usuario.setEmail("emailtest@email.com");
		
		BDDMockito.given(usuarioRepository.findByEmail(Mockito.anyString())).willReturn(Optional.of(usuario));
		
		BDDMockito.given(usuarioRepository.save(Mockito.any(Usuario.class))).willReturn(new Usuario());
		
		CodigoEmailDto resultado = usuarioService.enviarCodigoAlteracaoSenha("emailtest@email.com");
		
		assertNotNull(resultado);
	}
	@Test(expected = ConsistenciaException.class)
	public void testEnviarCodigoAlteracaoSenhaFalso() throws ConsistenciaException {
		
		BDDMockito.given(usuarioRepository.findByEmail(Mockito.anyString())).willReturn(Optional.empty());
		
		BDDMockito.given(usuarioRepository.save(Mockito.any(Usuario.class))).willReturn(new Usuario());
		
		usuarioService.enviarCodigoAlteracaoSenha("emailtest@email.com");		
	}
	@Test
	public void testAlterarSenhaUsuarioComSucesso() throws ConsistenciaException {

		Usuario usuario = new Usuario();
		usuario.setId(1);
		usuario.setUsuario("99930632077");
		usuario.setSenha(SenhaUtils.gerarHash("Senha123"));
		String token = usuario.getUsuario();
		
		BDDMockito.given(jwtTokenUtil.getUsernameFromToken(Mockito.any())).willReturn(token);
		
		BDDMockito.given(usuarioRepository.findById(Mockito.anyInt())).willReturn(Optional.of(usuario));

		usuarioService.alterarSenhaUsuario("Senha123", "SenhaLegal123", 1);
	}
	@Test(expected = ConsistenciaException.class)
	public void testAlterarSenhaUsuarioSemSucesso() throws ConsistenciaException {
		
		BDDMockito.given(usuarioRepository.findById(Mockito.anyInt())).willReturn(Optional.empty());

		usuarioService.alterarSenhaUsuario("Senha123", "SenhaLegal123", 1);
	}	
	@Test
	public void testRedefinirSenhaUsuarioComSucesso() throws ConsistenciaException {
		
		Usuario usuario = new Usuario();
		usuario.setId(1);
		usuario.setHashCode("12345abc");
		
		BDDMockito.given(usuarioRepository.findById(Mockito.anyInt())).willReturn(Optional.of(usuario));
		
		usuarioService.redefinirSenhaUsuario(usuario.getHashCode(), "NovaSenha123", usuario.getId());
	}
	@Test(expected = ConsistenciaException.class)
	public void testRedefinirSenhaUsuarioSemSucesso() throws ConsistenciaException {
		
		Usuario usuario = new Usuario();
		usuario.setId(1);
		usuario.setHashCode("12345abc");
		
		BDDMockito.given(usuarioRepository.findById(Mockito.anyInt())).willReturn(Optional.empty());

		usuarioService.redefinirSenhaUsuario(usuario.getHashCode(), "NovaSenha123", usuario.getId());
	}
}
