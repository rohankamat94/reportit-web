package com.cirs.webservice;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.cirs.webservice.util.UserReaderWriter;

@ApplicationPath("/res")
public class BaseWebService extends Application {

	public BaseWebService() {
		
	}
	
	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes=new HashSet<>();
		classes.add(UserReaderWriter.class);
		return super.getClasses();
	}
	
}
