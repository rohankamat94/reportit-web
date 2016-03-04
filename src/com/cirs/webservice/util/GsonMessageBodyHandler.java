package com.cirs.webservice.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
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

import com.cirs.entities.Admin;
import com.cirs.entities.User;
import com.cirs.entities.User.UserTO;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public final class GsonMessageBodyHandler implements MessageBodyWriter<Object>, MessageBodyReader<Object> {
	private static final String UTF_8 = "UTF-8";

	private Gson gson;

	private static class AdminExclusionStrategy implements ExclusionStrategy {
		@Override
		public boolean shouldSkipClass(Class<?> clazz) {
			return clazz.equals(Admin.class);
		}

		@Override
		public boolean shouldSkipField(FieldAttributes f) {
			return false;
		}
	}

	// Customize the gson behavior here
	private Gson getGson() {
		if (gson == null) {
			final GsonBuilder gsonBuilder = new GsonBuilder().setDateFormat("dd MMM yyyy HH:mm:ss")
					.addSerializationExclusionStrategy(new AdminExclusionStrategy());
			gson = gsonBuilder.disableHtmlEscaping().create();
		}
		return gson;
	}

	@Override
	public boolean isReadable(Class<?> type, Type genericType, java.lang.annotation.Annotation[] annotations,
			MediaType mediaType) {
		return true;
	}

	@Override
	public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream) {
		InputStreamReader streamReader = null;
		try {
			streamReader = new InputStreamReader(entityStream, UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		try {
			Type jsonType;
			if (type.equals(genericType)) {
				jsonType = type;
			} else {
				jsonType = genericType;
			}
			Gson gson = type.equals(User.class) || type.equals(UserTO.class) ? getUserGson() : getGson();
			return gson.fromJson(streamReader, jsonType);
		} finally {
			try {
				streamReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private Gson getUserGson() {
		return new GsonBuilder().setDateFormat("dd/MM/yyyy")
				.addSerializationExclusionStrategy(new AdminExclusionStrategy()).create();
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return true;
	}

	@Override
	public long getSize(Object object, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	@Override
	public void writeTo(Object object, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
					throws IOException, WebApplicationException {
		OutputStreamWriter writer = new OutputStreamWriter(entityStream, UTF_8);
		try {
			Type jsonType;
			if (type.equals(genericType)) {
				jsonType = type;
			} else {
				jsonType = genericType;
			}
			Gson gson = type.equals(User.class) || type.equals(UserTO.class) ? getUserGson() : getGson();
			gson.toJson(object, jsonType, writer);
		} finally {
			writer.close();
		}
	}
}