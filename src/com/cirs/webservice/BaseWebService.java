package com.cirs.webservice;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;

import com.cirs.webservice.util.GsonMessageBodyHandler;
import com.cirs.webservice.util.UserBodyHandler;

@ApplicationPath("/res")
public class BaseWebService extends ResourceConfig {

	public BaseWebService() {
		packages("com.cirs.webservice.util");
		register(GsonMessageBodyHandler.class);
		register(UserBodyHandler.class);
		register(LoggingFilter.class);
		property(CommonProperties.MOXY_JSON_FEATURE_DISABLE, true);
		System.out.println("is registered " + isRegistered(GsonMessageBodyHandler.class));
	}
}
