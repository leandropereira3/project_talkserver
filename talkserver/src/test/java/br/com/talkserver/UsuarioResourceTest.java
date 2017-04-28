package br.com.talkserver;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.apache.commons.lang3.RandomStringUtils;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.talkserver.model.Usuario;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class UsuarioResourceTest {

	static final String URL = "http://localhost:8080/talkserver/rest";
	static final String METODO_RECUPERA_POR_LOGIN = "/usuarioService/getUsuariosPorApelidoOuLogin/";
	static final String METODO_GET_USUARIOS = "/usuarioService/getUsuarios";
	HttpServer server;
	Gson gson = new Gson();
	
	@Before
	public void init() throws IOException {
		ResourceConfig config = new ResourceConfig().packages("br.com.talkserver.rest");
		URI uri = URI.create(URL);
		
		server = GrizzlyHttpServerFactory.createHttpServer(uri, config);
		server.start();		
	}
	
	@After
	public void finish(){
		server.shutdown();
	}
	
	@Test
	public void getUsuarios() {		
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(URL);
		String resultado = target.path(METODO_GET_USUARIOS).request().get(String.class);
		
		Assert.assertNotNull(resultado);

	}
	
	@Test
	public void naoRetornaUsuarioPorLogin() {		
		String login = RandomStringUtils.randomAlphanumeric(10);;
		
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(URL);
		String resultado = target.path(METODO_RECUPERA_POR_LOGIN+login).request().get(String.class);
		List<Usuario> usuarios = gson.fromJson(resultado, new TypeToken<ArrayList<Usuario>>(){}.getType());
		
		Assert.assertEquals(0, usuarios.size());
	}
	
	@Test
	public void retornaUsuarioPorLogin() {		
		String login = "user1";
		
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(URL);
		String resultado = target.path(METODO_RECUPERA_POR_LOGIN+login).request().get(String.class);
		List<Usuario> usuarios = gson.fromJson(resultado, new TypeToken<ArrayList<Usuario>>(){}.getType());
		
		Assert.assertEquals(login, usuarios.get(0).getLogin());
	}
}
