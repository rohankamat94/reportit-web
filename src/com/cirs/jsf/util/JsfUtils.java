package com.cirs.jsf.util;

import org.primefaces.context.RequestContext;

public class JsfUtils {

	public static void beginLoader() {
		RequestContext.getCurrentInstance().execute("showLoader();");
	}

	public static void endLoader() {
		RequestContext.getCurrentInstance().execute("hideLoader();");
	}

	public static void showSnackBar(String message) {
		RequestContext.getCurrentInstance().execute("showSnackBar('" + message + "');");

	}
}
