package com.cirs.webservice.util;

import com.google.gson.Gson;

public class JsonUtils {
	private static final Gson gson=new Gson();
	
	public static String entityToJson(Object o){
		return gson.toJson(o);
	}
	
	public static String getResponseEntity(int statusCode, String message){
		return entityToJson(new ResponseEntity(statusCode, message));
	}
	private static class ResponseEntity{
		int statusCode;
		String message;
		public ResponseEntity(int statusCode, String message) {
			this.statusCode = statusCode;
			this.message = message;
		}
		
	}
	
	
}
