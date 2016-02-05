package com.cirs.jsf.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.cirs.jsf.util.JsfUtils;

@SessionScoped
@ManagedBean(name = "navBean")
public class NavBean {

	public String getNavUrl(String string) {
		StringBuilder base = new StringBuilder(JsfUtils.getExternalContext().getRequestContextPath() + "/faces/pages/");
		switch (string) {
		case "users":
			base.append("users");
			break;
		case "complaints":
			base.append("complaints");
			break;
		case "categories":
			base.append("categories");
			break;
		default:
			throw new IllegalArgumentException(string + " is not a valid argument");
		}
		return base.append("/index.xhtml").toString();

	}
}