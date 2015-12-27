package com.cirs.webservice.util;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.cirs.entities.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Provider
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class UserReaderWriter implements MessageBodyReader<User>, MessageBodyWriter<User> {

	private final Gson gson;

	public UserReaderWriter() {
		gson = new Gson();
		System.out.println("here in provider");
	}

	@Override
	public long getSize(User t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		System.out.println("here in is Writeable with type " + type.getSimpleName());
		return true;
	}

	@Override
	public void writeTo(User object, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
					throws IOException, WebApplicationException {
		System.out.println("here in is Writeable with type " + type.getSimpleName());
		PrintWriter printWriter = new PrintWriter(entityStream);
		try {
			String json = gson.toJson(object);
			printWriter.write(json);
			printWriter.flush();
		} finally {
			printWriter.close();
		}
	}

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return true;
	}

	@Override
	public User readFrom(Class<User> type, Type gnericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
					throws IOException, WebApplicationException {
		InputStreamReader reader = new InputStreamReader(entityStream, "UTF-8");
		try {
			return gson.fromJson(reader, type);
		} finally {
			reader.close();
		}
	}
}