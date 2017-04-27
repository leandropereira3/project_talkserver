package br.com.talkserver.model;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(of="id", callSuper=false)
public class Usuario {

	private Long id;
	private String login;
	private String senha;
	private String apelido;
	
	public Usuario(Long id, String login, String senha, String apelido) {
		super();
		this.id = id;
		this.login = login;
		this.senha = senha;
		this.apelido = apelido;
	}
	
	public Usuario() {}
	
}
