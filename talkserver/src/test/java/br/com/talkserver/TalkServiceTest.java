package br.com.talkserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.talkserver.dao.TalkDao;
import br.com.talkserver.model.Usuario;
import br.com.talkserver.service.TalkService;

public class TalkServiceTest {

	TalkService service;

	@Mock
	private TalkDao dao;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		service = new TalkService(dao);
	}

	@Test
	public void salvarUsuario() {
		Usuario usuario = createUsuario();

		when(dao.getUsuarioPorLogin(usuario.getLogin())).thenReturn(null);
		boolean isUsuarioSalvo = service.salvarUsuario(usuario);
		assertTrue(isUsuarioSalvo);
	}

	@Test
	public void salvarUsuarioDuplicado() {		
		Usuario usuario = createUsuario();

		when(dao.getUsuarioPorLogin(usuario.getLogin())).thenReturn(new Usuario());
		boolean isUsuarioSalvo = service.salvarUsuario(usuario);
		assertFalse(isUsuarioSalvo);
	}
	
	@Test
	public void retornaUsuarioPorLogin(){
		Usuario usuario = createUsuario();
		Set<Usuario> usuariosCadastrados = createListaUsuariosCadastrados();
		usuariosCadastrados.add(usuario);
		
		when(dao.getUsuariosCadastrados()).thenReturn(usuariosCadastrados);
		List<Usuario> retorno = service.getUsuarioPorApelidoOuLogin(usuario.getLogin());
		assertEquals(false, retorno.isEmpty());
	}
	
	@Test
	public void naoRetornaUsuarioPorLogin(){
		Usuario usuario = createUsuario();
		Set<Usuario> usuariosCadastrados = createListaUsuariosCadastrados();		
		
		when(dao.getUsuariosCadastrados()).thenReturn(usuariosCadastrados);
		List<Usuario> retorno = service.getUsuarioPorApelidoOuLogin(usuario.getLogin());
		assertEquals(true, retorno.isEmpty());
	}
	
	@Test
	public void verificaSeUsuarioExiste(){
		Usuario usuario = createUsuario();
		Set<Usuario> usuariosCadastrados = createListaUsuariosCadastrados();		
		usuariosCadastrados.add(usuario);
		
		when(dao.getUsuariosCadastrados()).thenReturn(usuariosCadastrados);
		boolean retorno = service.isUsuarioExiste(usuario.getLogin(), usuario.getSenha());
		assertEquals(true, retorno);
	}
	
	private Usuario createUsuario(){
		Long id = new Random().nextLong() * 3;
		String apelido = RandomStringUtils.randomAlphabetic(10);
		String login = RandomStringUtils.randomAlphabetic(10);
		String senha = RandomStringUtils.randomAlphanumeric(10);
		return new Usuario(id, login, senha, apelido);
	}
	
	private Set<Usuario> createListaUsuariosCadastrados(){
		Set<Usuario> usuariosCadastrados = new HashSet<Usuario>();
		usuariosCadastrados.add(createUsuario());
		usuariosCadastrados.add(createUsuario());
		usuariosCadastrados.add(createUsuario());
		usuariosCadastrados.add(createUsuario());
		
		return usuariosCadastrados;
	}
}
