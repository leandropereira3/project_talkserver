package br.com.talkserver.dao;

import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.websocket.Session;

import br.com.talkserver.model.Contato;
import br.com.talkserver.model.ConversaPK;
import br.com.talkserver.model.Usuario;

public interface TalkDao {

	Usuario getUsuarioPorLogin(String login);
	
	Usuario getUsuarioPorId(Long id);
	
	Long getMaxId();
	
	void adicionarNovoUsuario(Usuario novoUsuario);
	
	boolean isExisteRegistroEmHistoricoConversa(ConversaPK pk);
	
	Entry<ConversaPK, LinkedList<String>> getRegistroEmHistoricoConversa(ConversaPK pk);
	
	Set<Usuario> getUsuariosCadastrados();	
	
	Map<Usuario, Session> getMapaSessoes();
	
	Map<Usuario, Set<Contato>> getMapaContatosAceitos();
	
	Map<Usuario, Set<Contato>> getMapaContatosPendentes();	
	
	Map<ConversaPK, LinkedList<String>> getHistoricoConversa();	
}
