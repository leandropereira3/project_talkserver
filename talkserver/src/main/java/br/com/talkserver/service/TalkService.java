package br.com.talkserver.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import javax.websocket.Session;

import br.com.talkserver.dao.TalkDao;
import br.com.talkserver.dao.TalkDaoImpl;
import br.com.talkserver.model.Contato;
import br.com.talkserver.model.ConversaPK;
import br.com.talkserver.model.Mensagem;
import br.com.talkserver.model.StatusContato;
import br.com.talkserver.model.Usuario;

public class TalkService {

	TalkDao dao;
	
	public TalkService(){
		this.dao = TalkDaoImpl.getInstance();
	}
	
	public TalkService(TalkDao dao){
		this.dao = dao;
	}
	
	public boolean salvarUsuario(Usuario novoUsuario){		
		if(dao.getUsuarioPorLogin(novoUsuario.getLogin()) != null){
			return Boolean.FALSE;
		}
		else{
			Long id = dao.getMaxId() + 1;
			novoUsuario.setId(id);
			dao.adicionarNovoUsuario(novoUsuario);
			return Boolean.TRUE;
		}		
	}
	
	public Usuario getUsuarioPorLogin(String login){
		return dao.getUsuarioPorLogin(login);	
	}
		
	public Set<Contato> obterListaDeContatosPorUsuario(Usuario usuario) {				
		return dao.getMapaContatosAceitos().get(usuario);
	}	
	
	public Set<Contato> obterListaDeContatosPorUsuario(String login) {				
		return obterListaDeContatosPorUsuario(getUsuarioPorLogin(login));
	}
	
	public Contato obterContatoDestinoMensagem(String loginUsuarioOrigem, String loginUsuarioDestino) {				
		Set<Contato> contatos = obterListaDeContatosPorUsuario(loginUsuarioOrigem);		
		Contato contatoDestino = getContatoDestino(contatos, loginUsuarioDestino);
		return contatoDestino;
	}
	
	private Contato getContatoDestino(Set<Contato> contatosDaOrigem, String loginContatoDestino) {
		if(contatosDaOrigem != null){
			for (Contato contato : contatosDaOrigem) {
				if(isContatoDestino(contato, loginContatoDestino)){					
					return contato;
				}
			}	
		}
		return null;
	}
	
	private boolean isContatoDestino(Contato contato, String destino) {
		return contato.getUsuario().getLogin().equals(destino);		
	}
			
	public void salvarHistoricoConversa(Usuario userOrigem, Usuario usuarioDestino, Mensagem msg) {
		ConversaPK pk = new ConversaPK(userOrigem, usuarioDestino);
		
		if(dao.isExisteRegistroEmHistoricoConversa(pk)){
			dao.getRegistroEmHistoricoConversa(pk).getValue().add(msg.getMensagemData());							
		}
		else{
			dao.getHistoricoConversa().put(pk, new LinkedList<String>());
			dao.getRegistroEmHistoricoConversa(pk).getValue().add(msg.getMensagemData());
		}		
	}
	
	public Session obterSessaoUsuario(Usuario usuario){		
		return dao.getMapaSessoes().get(usuario);
	}
		
	public void salvarSessao(Usuario usuario, Session sessao){
		dao.getMapaSessoes().put(usuario, sessao);		
	}
		
	public void excluirSessao(String idSessao){
		Usuario usuarioParaRemover = null;
		for (Entry<Usuario, Session > map : dao.getMapaSessoes().entrySet()) {
			if(map.getValue().getId().equals(idSessao)){
				usuarioParaRemover = map.getKey();
			}
		}
		dao.getMapaSessoes().remove(usuarioParaRemover);
	}	
	
	public Set<Usuario> getUsuariosCadastrados(){
		return dao.getUsuariosCadastrados();
	}
	
	public List<Usuario> getUsuarioPorApelidoOuLogin(String filtro){
		List<Usuario> usuarios = new ArrayList<Usuario>();
		for (Usuario usuario : dao.getUsuariosCadastrados()) {
			if(usuario.getLogin().contains(filtro)){
				usuarios.add(usuario);
			}
			else if(usuario.getApelido().contains(filtro)){
				usuarios.add(usuario);
			}
		}	
		return usuarios;
	}
	
	public boolean isUsuarioExiste(String novoUsuario, String senha) {
		for(Usuario usuarioCadastrado : dao.getUsuariosCadastrados()){
			if(usuarioCadastrado.getLogin().equals(novoUsuario) && usuarioCadastrado.getSenha().equals(senha)){
				return true;
			}
		}
		return false;
	}
	
	public void salvarContato(Usuario userAtual, Usuario userContato) {
		Session sessaoUsuarioContato = dao.getMapaSessoes().get(userContato);
		
		if(dao.getMapaContatosAceitos().containsKey(userAtual)){
			dao.getMapaContatosAceitos().get(userAtual).add(new Contato(sessaoUsuarioContato, userContato, StatusContato.ACEITO));
		}
		else{
			dao.getMapaContatosAceitos().put(userAtual, new HashSet<Contato>());
			dao.getMapaContatosAceitos().get(userAtual).add(new Contato(sessaoUsuarioContato, userContato, StatusContato.ACEITO));
		}
	}
	
	public Set<Contato> getListaContatosAceitos(Usuario userAtual) {
		Set<Contato> contatos = dao.getMapaContatosAceitos().get(userAtual);
		if(contatos == null){
			contatos = new HashSet<Contato>();
		}
		
		return contatos;
	}
	
	public LinkedList<String> obterHistoricoConversa(Usuario usuarioOrigem, Usuario usuarioDestino){
		LinkedList<String> historico = new LinkedList<String>();
		ConversaPK pk = new ConversaPK(usuarioOrigem, usuarioDestino);
		for(Entry<ConversaPK, LinkedList<String>> entry : dao.getHistoricoConversa().entrySet()){
			if(entry.getKey().equals(pk)){
				historico = entry.getValue();
			}
		}	
		
		return historico;
	}
	
}
