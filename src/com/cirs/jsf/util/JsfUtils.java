package com.cirs.jsf.util;

import org.primefaces.context.RequestContext;

public class JsfUtils {

	public static void beginLoader() {
		RequestContext.getCurrentInstance().execute("PF('loadDlg').show();");
	}

	public static void endLoader() {
		RequestContext.getCurrentInstance().execute("PF('loadDlg').hide();");
	}

	public static void showSnackBar(String message) {
		RequestContext.getCurrentInstance().execute("showSnackBar('" + message + "');");

	}
}
