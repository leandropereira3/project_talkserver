package br.com.talkserver.rest;

import io.swagger.annotations.Api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api
@Path("/loginService")
public class LoginResource extends ServicesRest{
	
	@Path("/login/{user}/{pass}")
	@GET
    @Produces({MediaType.TEXT_PLAIN})
	public Response login(@Context HttpServletRequest request, @PathParam("user") String usuario, @PathParam("pass") String senha) {
		boolean loginEfetuado = service.isUsuarioExiste(usuario, senha);
		if(loginEfetuado){
			HttpSession sessao = request.getSession();
			sessao.setAttribute("login", usuario);			
		}
		
		return Response.ok(loginEfetuado).build();		
	}	
}
