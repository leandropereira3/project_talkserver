package br.com.talkserver.model;

import javax.websocket.Session;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of="usuario", callSuper=false)
public class Contato {

	private Usuario usuario;	

	public Contato() {}
	
	public Contato(Usuario usuario) {
		super();		
		this.usuario = usuario;
	}
	
	public Contato(Session session, Usuario usuario) {
		super();
		//this.session = session;
		this.usuario = usuario;
	}
	
	public Contato(Session session, Usuario usuario, StatusContato status) {
		super();
		//this.session = session;
		this.usuario = usuario;
		//this.status = status;
	}
	
//	public boolean isOnline(){
//		return session != null && session.isOpen();
//	}	
}
