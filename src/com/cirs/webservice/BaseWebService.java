package com.cirs.webservice;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;

import com.cirs.webservice.util.UserReaderWriter;

@ApplicationPath("/res")
public class BaseWebService extends ResourceConfig{

	public BaseWebService() {
		packages("com.cirs.webservice.util");
//		register(UserReaderWriter.class);
		 
	}
	
}
