package br.com.talkserver.model;

import lombok.Getter;

public enum TipoMensagem {

	SESSAO_NOVA("NV"),
	SESSAO_ANTIGA("AN");	
	
	@Getter
	private String codigo;
	
	TipoMensagem(String codigo){
		this.codigo = codigo;
	}
}
