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
	
	/**
	 * Construtor padrao
	 */
	public TalkService(){
		this.dao = TalkDaoImpl.getInstance();
	}
	
	/**
	 * Construtor utilizado para o Mock
	 */
	public TalkService(TalkDao dao){
		this.dao = dao;
	}
	
	/**
	 * Verifica se o usuario existe.
	 * Caso nao exista salva o novo usuario
	 * @param novoUsuario
	 * @return
	 */
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
	
	/**
	 * Retorna um usuario
	 * @param login
	 * @return
	 */
	public Usuario getUsuarioPorLogin(String login){
		return dao.getUsuarioPorLogin(login);	
	}
		
	/**
	 * Retorna os contatos associados a um usuário
	 * @param usuario
	 * @return
	 */
	public Set<Contato> obterListaDeContatosPorUsuario(Usuario usuario) {				
		return dao.getMapaContatosAceitos().get(usuario);
	}	
	
	/**
	 * Retorna os contatos associados a um usuário
	 * @param login
	 * @return
	 */
	public Set<Contato> obterListaDeContatosPorUsuario(String login) {				
		return obterListaDeContatosPorUsuario(getUsuarioPorLogin(login));
	}
	
	/**
	 * Retorna o usuário de destino de uma mensagem.
	 * O usuário de destino é buscado na lista de contatos 
	 * do usuário de origem
	 * @param loginUsuarioOrigem
	 * @param loginUsuarioDestino
	 * @return
	 */
	public Contato obterContatoDestinoMensagem(String loginUsuarioOrigem, String loginUsuarioDestino) {				
		Set<Contato> contatos = obterListaDeContatosPorUsuario(loginUsuarioOrigem);		
		Contato contatoDestino = getContatoDestino(contatos, loginUsuarioDestino);
		return contatoDestino;
	}
	
	/**
	 * Retorna o contato de destino presente na lista de de contatos do usuário
	 * de origem 
	 * @param contatosDaOrigem
	 * @param loginContatoDestino
	 * @return
	 */
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
	
	/** 
	 * @param contato
	 * @param destino
	 * @return
	 */
	private boolean isContatoDestino(Contato contato, String destino) {
		return contato.getUsuario().getLogin().equals(destino);		
	}
	
	/**
	 * Salva o histórico de mensagens entre dois usuários
	 * @param userOrigem
	 * @param usuarioDestino
	 * @param msg
	 */
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
	
	/**
	 * Retorna a sessão do usuário
	 * @param usuario
	 * @return
	 */
	public Session obterSessaoUsuario(Usuario usuario){		
		return dao.getMapaSessoes().get(usuario);
	}
		
	/**
	 * Salva a sessão do usuário no momento em que é feito o login
	 * @param usuario
	 * @return
	 */
	public void salvarSessao(Usuario usuario, Session sessao){
		dao.getMapaSessoes().put(usuario, sessao);		
	}
	
	/**
	 * Remove a sessão de um usuário
	 * @param idSessao
	 */
	public void excluirSessao(String idSessao){
		Usuario usuarioParaRemover = null;
		for (Entry<Usuario, Session > map : dao.getMapaSessoes().entrySet()) {
			if(map.getValue().getId().equals(idSessao)){
				usuarioParaRemover = map.getKey();
			}
		}
		dao.getMapaSessoes().remove(usuarioParaRemover);
	}	
	
	/**
	 * Retorna todos os usuários cadastrados.
	 * @return
	 */
	public Set<Usuario> getUsuariosCadastrados(){
		return dao.getUsuariosCadastrados();
	}
	
	/**
	 * Retorna um usuário pelo apelido ou pelo login
	 * @param filtro
	 * @return
	 */
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
	
	/**
	 * Verifica se um usuário existe
	 * @param novoUsuario
	 * @param senha
	 * @return
	 */
	public boolean isUsuarioExiste(String novoUsuario, String senha) {
		for(Usuario usuarioCadastrado : dao.getUsuariosCadastrados()){
			if(usuarioCadastrado.getLogin().equals(novoUsuario) && usuarioCadastrado.getSenha().equals(senha)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Salva um contato na lista de contatos de um usuário
	 * @param userAtual
	 * @param userContato
	 */
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
	
	/**
	 * Retorna a lista de contatos de um usuário
	 * @param userAtual
	 * @return
	 */
	public Set<Contato> getListaContatosAceitos(Usuario userAtual) {
		Set<Contato> contatos = dao.getMapaContatosAceitos().get(userAtual);
		if(contatos == null){
			contatos = new HashSet<Contato>();
		}
		
		return contatos;
	}
	
	/**
	 * Retorna o histórico de mensagens entre dois usuários
	 * @param usuarioOrigem
	 * @param usuarioDestino
	 * @return
	 */
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
