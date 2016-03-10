package com.cirs.webservice;

import static com.cirs.webservice.util.JsonUtils.getResponseEntity;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.EJBException;
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
import com.cirs.exceptions.EntityAlreadyExistsException;
import com.cirs.webservice.util.JsonUtils;

@Path("upvote")
public class UpvoteWebService {
	private static class UpvoteResponse {
		int created = 0;
		List<Upvote> failures = new ArrayList<>();
	}

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
		UpvoteResponse response = new UpvoteResponse();
		for (Upvote u : upvotes) {
			try {
				dao.createUpvote(u);
				response.created++;
			} catch (EJBException e) {
				e.printStackTrace();
				if (e.getCause() instanceof EntityAlreadyExistsException) {
					System.out.println("could not create upvote");
				}
				response.failures.add(u);
			}

		}
		return Response.status(200).entity(response).build();
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
