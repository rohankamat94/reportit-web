package com.cirs.jsf.util;

import java.io.IOException;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

	public static void executeJS(String script) {
		RequestContext.getCurrentInstance().execute(script);
	}

	public static HttpSession getHttpSession(boolean create) {
		return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(create);
	}

	public static ExternalContext getExternalContext() {
		return FacesContext.getCurrentInstance().getExternalContext();
	}

	public static void redirect(String redirectTo) {
		try {
			getExternalContext().redirect(redirectTo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public static String getCookieValue(HttpServletRequest request, String name) {
	    Cookie[] cookies = request.getCookies();
	    if (cookies != null) {
	        for (Cookie cookie : cookies) {
	            if (name.equals(cookie.getName())) {
	                return cookie.getValue();
	            }
	        }
	    }
	    return null;
	}

	public static HttpServletResponse getHttpServletResponse(){
		return ((HttpServletResponse) getExternalContext().getResponse());
	}
	
	public static void addCookie(HttpServletResponse resp,String name, String value, int maxAge) {
	    Cookie cookie = new Cookie(name, value);
	    cookie.setPath("/");
	    cookie.setMaxAge(maxAge);
	    resp.addCookie(cookie);
	}

	public static void removeCookie(HttpServletResponse response, String name) {
	    addCookie(response, name, null, 0);
	}

}
