package br.com.talkserver;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.apache.commons.lang3.RandomStringUtils;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.talkserver.service.TalkService;

public class LoginResourceTest {

	TalkService service;
	// LoginResource
	HttpServer server;

	static final String URL = "http://localhost:8080/talkserver";
	static final String LOGIN_SERVICE = "/rest/loginService/login";

	@Before
	public void init() throws IOException {
//		ResourceConfig config = new ResourceConfig().packages("br.com.talkserver.rest").setApplicationName("talkserver");
//		URI uri = URI.create("http://localhost:8080/talkserver/rest");
//		
//		server = GrizzlyHttpServerFactory.createHttpServer(uri, config);
//		server.start();		
	}
	
	@After
	public void finish(){
		//server.shutdown();
	}

	@Test
	public void loginInvalido() {
		String login = RandomStringUtils.randomAlphabetic(10);
		String senha = RandomStringUtils.randomAlphanumeric(10);
		
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(URL);
		String resultado = target.path(LOGIN_SERVICE+"/"+login+"/"+senha).request().get(String.class);

		assertEquals("false", resultado);;

	}

}
