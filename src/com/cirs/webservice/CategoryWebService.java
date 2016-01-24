package com.cirs.webservice;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.cirs.dao.remote.CategoryDao;
import com.cirs.entities.Category;

@Path("/cat")
public class CategoryWebService {
	@EJB(beanName = "categoryDao")
	private CategoryDao dao;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Category> findAll(@QueryParam("activeOnly") @DefaultValue("true") boolean activeOnly) {
		if (activeOnly) {
			return dao.findAllActive();
		}
		return dao.findAll();
	}
}