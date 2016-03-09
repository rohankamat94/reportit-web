package com.cirs.webservice;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.cirs.dao.remote.CommentDao;
import com.cirs.entities.Comment;
import com.cirs.exceptions.EntityNotCreatedException;
import static com.cirs.webservice.util.JsonUtils.*;

@Path("comment")
public class CommentWebService {
	@EJB
	CommentDao dao;

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createComment(Comment comment) {
		if (comment == null || comment.getData() == null) {
			return Response.status(400).entity(getResponseEntity(400, "Comment cannot be empty")).build();
		}
		if (comment.getUser() == null || comment.getUser().getId() == null) {
			return Response.status(400).entity(getResponseEntity(400, "User cannot be null")).build();

		}
		if (comment.getComplaint() == null || comment.getComplaint().getId() == null) {
			return Response.status(400).entity(getResponseEntity(400, "Complaint cannot be null")).build();
		}
		try {
			dao.create(comment);
			return Response.status(Status.CREATED).entity(getResponseEntity(201, "Comment has been added")).build();
		} catch (EntityNotCreatedException e) {
			e.printStackTrace();
			return Response.ok().build();
		}

	}
}
