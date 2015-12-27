package com.cirs.jsf.util;

import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

public class JsfUtils {

	public static void beginLoader() {
		RequestContext.getCurrentInstance().execute("showLoader();");
	}

	public static void endLoader() {
		RequestContext.getCurrentInstance().execute("hideLoader();");
	}

	public static void showSnackBar(String message) {
		FacesContext.getCurrentInstance().validationFailed();
		RequestContext.getCurrentInstance().execute("showSnackBar('" + message + "');");
	}
	
	public static void executeJS(String script){
		RequestContext.getCurrentInstance().execute(script);
	}
}
