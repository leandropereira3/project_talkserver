package br.com.talkserver.dao;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.websocket.Session;

import lombok.Getter;
import br.com.talkserver.model.Contato;
import br.com.talkserver.model.ConversaPK;
import br.com.talkserver.model.Usuario;


/***
 * Classe que faz a representação do banco de dados
 * 
 */
public class TalkDaoImpl implements TalkDao{
	
	private static TalkDaoImpl instance;
	@Getter
	private Set<Usuario> usuariosCadastrados;	
	@Getter
	private Map<Usuario, Session> mapaSessoes;
	@Getter
	private Map<Usuario, Set<Contato>> mapaContatosAceitos;
	@Getter
	private Map<Usuario, Set<Contato>> mapaContatosPendentes;	
	@Getter
	private Map<ConversaPK, LinkedList<String>> historicoConversa;	
	
	private TalkDaoImpl(){
		initDao();
	}
	
	public static TalkDaoImpl getInstance(){		
		if(instance == null){
			instance = new TalkDaoImpl();						
		}			
		
		return instance;		
	}

	private void initDao() {
		initUsuariosPadrao();		
		mapaSessoes = new HashMap<Usuario, Session>();		
		mapaContatosAceitos = new HashMap<Usuario, Set<Contato>>();
		mapaContatosPendentes = new HashMap<Usuario, Set<Contato>>();		
		historicoConversa = new HashMap<ConversaPK, LinkedList<String>>();
		
		Set<Contato> contatosUsuario1 = new HashSet<Contato>();
		contatosUsuario1.add(new Contato(getUsuarioPorId(2L)));
		mapaContatosAceitos.put(getUsuarioPorId(1L), contatosUsuario1);
		mapaContatosAceitos.get(getUsuarioPorId(1L)).add(new Contato(getUsuarioPorId(3L)));
		
		Set<Contato> contatosUsuario2 = new HashSet<Contato>();
		contatosUsuario2.add(new Contato(getUsuarioPorId(1L)));
		mapaContatosAceitos.put(getUsuarioPorId(2L), contatosUsuario2);
		
		Set<Contato> contatosUsuario3 = new HashSet<Contato>();
		contatosUsuario3.add(new Contato(getUsuarioPorId(1L)));
		mapaContatosAceitos.put(getUsuarioPorId(3L), contatosUsuario3);
						
	}

	private void initUsuariosPadrao() {
		usuariosCadastrados = new HashSet<Usuario>();
		usuariosCadastrados.add(new Usuario(1L, "user1", "123", "User 1"));
		usuariosCadastrados.add(new Usuario(2L, "user2", "123", "User 2"));
		usuariosCadastrados.add(new Usuario(3L, "user3", "123", "User 3"));		
	}
	
	public Usuario getUsuarioPorLogin(String login){
		for (Usuario usuario : usuariosCadastrados) {
			if(usuario.getLogin().equals(login)){
				return usuario;
			}
		}		
		return null;
	}
	
	public Usuario getUsuarioPorId(Long id){
		for (Usuario usuario : usuariosCadastrados) {
			if(usuario.getId() == id){
				return usuario;
			}
		}		
		return null;
	}
	
	public Long getMaxId(){
		return (long)usuariosCadastrados.size();
	}
	
	public void adicionarNovoUsuario(Usuario novoUsuario){
		usuariosCadastrados.add(novoUsuario);
	}
	
	public boolean isExisteRegistroEmHistoricoConversa(ConversaPK pk){
		for(Entry<ConversaPK, LinkedList<String>> entry : historicoConversa.entrySet()){
			if(entry.getKey().equals(pk)){
				return true;
			}
		}
		return false;
	}

	public Entry<ConversaPK, LinkedList<String>> getRegistroEmHistoricoConversa(ConversaPK pk){
		for(Entry<ConversaPK, LinkedList<String>> entry : historicoConversa.entrySet()){
			if(entry.getKey().equals(pk)){
				return entry;
			}
		}
		return null;
	}

}
