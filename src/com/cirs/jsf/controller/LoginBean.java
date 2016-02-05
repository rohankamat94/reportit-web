package com.cirs.jsf.controller;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.cirs.core.CIRSConstants.*;

import java.io.IOException;

import com.cirs.dao.remote.AdminDao;
import com.cirs.jsf.util.JsfUtils;

@ManagedBean
@SessionScoped
public class LoginBean {

	@EJB(beanName = "adminDao")
	private AdminDao dao;

	private String username;
	private String password;
	private boolean remember;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void login() throws IOException {
		System.out.println("Logging in with" + username + " " + password);
		if (dao.verifyAdmin(username, password)) {
			ExternalContext context = JsfUtils.getExternalContext();
			if (context != null) {
				context.getSessionMap().put(LOGIN_ATTRIBUTE_KEY, username);
			}
			if(remember){
				JsfUtils.addCookie((HttpServletResponse) context.getResponse(), LOGIN_ATTRIBUTE_KEY, username, 60*60*24*7*52);
			}
			String complaintPath = context.getRequestContextPath()
					+ "/faces/pages/complaints/index.xhtml";
			System.out.println(complaintPath);
			context.redirect(complaintPath);
		}
	}

	public String logout() {
		System.out.println("Logging out");
		ExternalContext context = JsfUtils.getExternalContext();
		if (context != null) {
			context.getSessionMap().remove(LOGIN_ATTRIBUTE_KEY);
			JsfUtils.removeCookie(JsfUtils.getHttpServletResponse(), LOGIN_ATTRIBUTE_KEY);
		}
		return JsfUtils.getExternalContext().getRequestContextPath() + "/faces/login.xhtml";
	}

	public boolean getRemember() {
		return remember;
	}

	public void setRemember(boolean remember) {
		this.remember = remember;
	}
}
