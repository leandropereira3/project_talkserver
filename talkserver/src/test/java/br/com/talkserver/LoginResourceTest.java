package br.com.talkserver;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.apache.commons.lang3.RandomStringUtils;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LoginResourceTest {

	HttpServer server;

	static final String URL = "http://localhost:8080/talkserver/rest";
	static final String LOGIN_SERVICE = "/loginService/login";

	//inicia o servidor a cada teste
	@Before
	public void init() throws IOException {
		ResourceConfig config = new ResourceConfig().packages("br.com.talkserver.rest");
		URI uri = URI.create(URL);
		
		server = GrizzlyHttpServerFactory.createHttpServer(uri, config);
		server.start();		
	}
	
	//derruba o servidor a cada teste
	@After
	public void finish(){
		server.shutdown();
	}

	@Test
	public void loginInvalido() {
		String login = RandomStringUtils.randomAlphabetic(10);
		String senha = RandomStringUtils.randomAlphanumeric(10);
		
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(URL);
		String resultado = target.path(LOGIN_SERVICE+"/"+login+"/"+senha).request().get(String.class);

		assertEquals("false", resultado);

	}

}
