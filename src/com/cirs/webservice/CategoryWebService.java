package com.cirs.webservice;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import com.cirs.dao.remote.CategoryDao;
import com.cirs.entities.Category;

@Path("/cat")
public class CategoryWebService {
	@EJB(beanName = "categoryDao")
	private CategoryDao dao;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAll(@QueryParam("adminId") Long adminId,
			@QueryParam("activeOnly") @DefaultValue("true") boolean activeOnly) {
		if (adminId == null) {
			return Response.status(Status.BAD_REQUEST).entity("{\"message\":\"Cannot have null admin id\"}").build();
		}
		List<Category> result = dao.findAll(adminId);
		ResponseBuilder response = Response.status(200);

		if (activeOnly) {
			result = result.stream().filter(c -> c.getActive() == true).collect(Collectors.toList());
		}
		return response.entity(result).build();
	}
}
