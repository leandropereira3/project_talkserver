package br.com.talkserver.model;

import lombok.Getter;

public enum StatusContato {

	ACEITO(1),
	PENDENTE(2),
	BLOQUEADO(3);
	
	@Getter
	private Integer codigo;
	
	StatusContato(Integer codigo){
		this.codigo = codigo;
	}
}
