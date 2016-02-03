package com.cirs.jsf.controller;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.cirs.dao.remote.CategoryDao;
import com.cirs.entities.Category;
import com.cirs.exceptions.EntityNotCreatedException;

@ManagedBean
@ViewScoped
public class TestBean {

	@EJB(beanName = "categoryDao")
	CategoryDao dao;

	public String getTest() {
		Category c=new Category();
		c.setName("asdfssdafd");
		c.setActive(true);
		try {
			dao.create(c);
			return "success";
		} catch (EntityNotCreatedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "not created";
		}
	}

}
