package br.com.talkserver.rest;

import io.swagger.annotations.Api;

import java.util.LinkedList;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.talkserver.model.Contato;
import br.com.talkserver.model.Usuario;

@Api
@Path("/chatService")
public class ChatResource extends ServicesRest{
	
	@POST
	@Path("/adicionarContato/{loginUser}/{loginUsuarioContato}")
	@Produces(MediaType.TEXT_HTML)	
	public Response adicionarContato(@PathParam("loginUser") String usuarioLogado, @PathParam("loginUsuarioContato") String loginUsuarioContato){		
		Usuario userAtual = service.getUsuarioPorLogin(usuarioLogado);
		Usuario userContato = service.getUsuarioPorLogin(loginUsuarioContato);
		service.salvarContato(userAtual, userContato);
		service.salvarContato(userContato, userAtual);
		return Response.ok().build();
	}

	@Path("/getContatosAceitos/{loginUser}")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response getContatosAceitos(@PathParam("loginUser") String usuarioLogado) {
		Usuario userAtual = service.getUsuarioPorLogin(usuarioLogado);		
		Set<Contato> contatos = service.getListaContatosAceitos(userAtual);
						
		return Response.ok(convertToJson(contatos),	MediaType.APPLICATION_JSON).build();				
	}		

	@Path("/getHistoricoConversa/{loginUserA}/{loginUserB}")
	@GET
    @Produces({MediaType.APPLICATION_JSON})
	public Response getHistoricoConversa(@PathParam("loginUserA") String usuarioA, @PathParam("loginUserB") String usuarioB) {
		Usuario usuarioOrigem = service.getUsuarioPorLogin(usuarioA);
		Usuario usuarioDestino = service.getUsuarioPorLogin(usuarioB);		
		LinkedList<String> historico = service.obterHistoricoConversa(usuarioOrigem, usuarioDestino);
		
		return Response.ok(convertToJson(historico), MediaType.APPLICATION_JSON).build();				
	}
}
