package br.com.talkserver.model;

import lombok.Data;

@Data
public class Mensagem {

	private String origem;
	private String destino;
	private String mensagem;
	private String data;
	
	public String getMensagemData(){
		return mensagem + "@" + data + "@" + origem;
	}
}
