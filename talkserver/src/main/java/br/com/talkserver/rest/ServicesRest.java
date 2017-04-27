package br.com.talkserver.rest;

import java.util.Collection;

import br.com.talkserver.service.TalkService;

import com.google.gson.Gson;

public abstract class ServicesRest {
	
	TalkService service = new TalkService();
	
	String convertToJson(Collection<?> list){
		Gson gson = new Gson();
		String json = gson.toJson(list);
		
		return json;
	}

}
