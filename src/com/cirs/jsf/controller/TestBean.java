package com.cirs.jsf.controller;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.cirs.dao.remote.AdminDao;

@ManagedBean
@ViewScoped
public class TestBean {

	@EJB
	AdminDao dao;

	public String getTest() {
		return dao.verifyAdmin("wadmin", "1234") + "";
	}

}
