package br.com.talkserver.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.com.talkserver.model.Usuario;

@Api
@Path("/usuarioService")
public class UsuarioResource extends ServicesRest{	
	
	@ApiOperation(value="Cria um novo usuário com os parâmetros informados.")
	@POST
	@Path("/inserirNovo/{apelido}/{login}/{senha}")
	@Produces(MediaType.TEXT_HTML)
	public Response inserirNovo(
			@PathParam("apelido") String apelido, 
			@PathParam("login") String login, 
			@PathParam("senha") String senha)
	{			
		Usuario novoUsuario = new Usuario(null, login, senha, apelido);
		boolean isUsuarioSalvo = service.salvarUsuario(novoUsuario);
		if(isUsuarioSalvo){
			return Response.ok().build();
		}
		else{
			return Response.status(Status.BAD_REQUEST).build();			
		}		
	}
	
	@ApiOperation(value="Retorna a lista de todos os usuários cadastrados.")
	@Path("/getUsuarios")
	@GET
    @Produces({MediaType.APPLICATION_JSON})
	public Response getUsuariosCadastrados() {			
		return Response.ok(convertToJson(service.getUsuariosCadastrados()),
				MediaType.APPLICATION_JSON).build();				
	}
	
	@ApiOperation(value="Retorna a lista de usuários de acordo com o filtro de login ou apelido.")
	@Path("/getUsuariosPorApelidoOuLogin/{filtro}")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response getUsuariosPorApelidoOuLogin(@PathParam("filtro") String filtro) {			
		return Response.ok(convertToJson(service.getUsuarioPorApelidoOuLogin(filtro)),
				MediaType.APPLICATION_JSON).build();				
	}	

}
