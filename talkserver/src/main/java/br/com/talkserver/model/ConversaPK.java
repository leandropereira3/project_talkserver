package br.com.talkserver.model;

import lombok.Getter;
import lombok.Setter;



public class ConversaPK {

	@Getter
	@Setter
	private Usuario usuarioA;
	
	@Getter
	@Setter
	private Usuario usuarioB;
	
	public ConversaPK(Usuario usuarioA, Usuario usuarioB) {
		this.usuarioA = usuarioA;
		this.usuarioB = usuarioB;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ConversaPK)){
			return false;
		}
		ConversaPK other = (ConversaPK)obj;
		if((usuarioA.equals(other.getUsuarioA()) && usuarioB.equals(other.getUsuarioB()))
				|| (usuarioB.equals(other.getUsuarioA()) && usuarioA.equals(other.getUsuarioB()))){
			return true;
		}
		
		return false;
	}
}
