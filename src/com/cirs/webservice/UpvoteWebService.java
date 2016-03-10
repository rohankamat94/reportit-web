package com.cirs.webservice;

import static com.cirs.webservice.util.JsonUtils.getResponseEntity;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.cirs.dao.remote.UpvoteDao;
import com.cirs.dao.remote.UserDao;
import com.cirs.entities.Upvote;
import com.cirs.entities.User;
import com.cirs.webservice.util.JsonUtils;

@Path("upvote")
public class UpvoteWebService {

	@EJB
	UpvoteDao dao;

	@EJB
	UserDao userDao;

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createUpvote(List<Upvote> upvotes) {
		if (upvotes.isEmpty()) {
			return Response.ok(getResponseEntity(200, "No upvotes given")).build();
		}
		for (Upvote u : upvotes) {
			dao.createUpvote(u);
		}
		return Response.status(201).entity(getResponseEntity(201, "Created")).build();
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getComplaintsUpvotedByUser(@QueryParam("userId") Long userId) {
		if (userId == null) {
			return Response.status(400).entity(JsonUtils.getResponseEntity(400, "User id cannot be null")).build();
		}
		User u = userDao.findById(userId);
		if (u == null) {
			return Response.status(404)
					.entity(JsonUtils.getResponseEntity(404, "user with id " + userId + " does not exist")).build();
		}
		return Response.ok(dao.getAllUpvotedComplaintsByUser(u)).build();

	}

}
