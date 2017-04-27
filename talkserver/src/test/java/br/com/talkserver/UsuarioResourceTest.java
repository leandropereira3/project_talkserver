package br.com.talkserver;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.junit.Assert;
import org.junit.Test;

public class UsuarioResourceTest {

	static final String URL = "http://localhost:8080/talkserver";
	static final String METODO_INSERIR_NOVO = "/rest/usuarioService/inserirNovo";
	static final String METODO_GET_USUARIOS = "/rest/usuarioService/getUsuarios";
	
	
	@Test
	public void getUsuarios() {		
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(URL);
		String resultado = target.path(METODO_GET_USUARIOS).request().get(String.class);
		
		Assert.assertNotNull(resultado);

	}
}
