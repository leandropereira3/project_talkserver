package br.com.talkserver.websocket;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import br.com.talkserver.model.Contato;
import br.com.talkserver.model.Mensagem;
import br.com.talkserver.model.Usuario;
import br.com.talkserver.service.TalkService;

import com.google.gson.Gson;

@ServerEndpoint(value = "/chat", configurator = SessionConfigurator.class)
public class Chat {

	private HttpSession httpSession;
	private TalkService service;
	private Gson gson;

	public Chat(){
		service = new TalkService();
		gson = new Gson();
	}
	
	@OnOpen
	public void abrir(Session sessao, EndpointConfig config) {		
		httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
		Usuario usuario = service.getUsuarioPorLogin((String) httpSession.getAttribute("login"));
		service.salvarSessao(usuario, sessao);		
	}
	
	@OnClose
	public void fechar(Session userSession) {		
		service.excluirSessao(userSession.getId());
	}

	@OnMessage
	public void onMensagem(String mensagem, Session userSession) {		
		Mensagem msg = gson.fromJson(mensagem, Mensagem.class);
		Contato contatoDestino = service.obterContatoDestinoMensagem(msg.getOrigem(), msg.getDestino());
		enviarMensagem(msg, contatoDestino);						
	}
	
	private void enviarMensagem(Mensagem msg, Contato contatoDestino) {
		if(contatoDestino != null){
			Session sessionContatoDestino = service.obterSessaoUsuario(contatoDestino.getUsuario());
			Usuario userOrigem = service.getUsuarioPorLogin(msg.getOrigem());
			
			if(sessionContatoDestino != null && sessionContatoDestino.isOpen()){				
				service.salvarHistoricoConversa(userOrigem, contatoDestino.getUsuario(), msg);
				sessionContatoDestino.getAsyncRemote().sendText(msg.getMensagemData());				
			}
			else{				
				service.salvarHistoricoConversa(userOrigem, contatoDestino.getUsuario(), msg);				
			}
		}		
	}

	@OnError
	public void onError(Session session, Throwable t){
		//TODO IMPLEMENTAR
		t.printStackTrace();
	}
}
