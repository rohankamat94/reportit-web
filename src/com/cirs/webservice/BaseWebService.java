package com.cirs.webservice;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.internal.inject.Providers;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import com.cirs.webservice.util.GsonMessageBodyHandler;

@ApplicationPath("/res")
public class BaseWebService extends ResourceConfig {

	public BaseWebService() {
		packages("com.cirs.webservice.util");
		register(GsonMessageBodyHandler.class);
		register(LoggingFilter.class);
		System.out.println("is registered " + isRegistered(GsonMessageBodyHandler.class));
	}
}
