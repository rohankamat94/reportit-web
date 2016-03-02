package com.cirs.webservice;

import static com.cirs.webservice.util.JsonUtils.getResponseEntity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.cirs.core.CIRSConstants;
import com.cirs.core.CIRSConstants.ImageDir;
import com.cirs.dao.remote.UserDao;
import com.cirs.entities.User;
import com.cirs.entities.User.UserTO;
import com.cirs.exceptions.EntityNotFoundException;
import com.cirs.webservice.util.JsonUtils;

@Path("/user")
public class UserWebService {

	private static class TokenEntity {
		private String token;
		private Long id;

		public String getToken() {
			return token;
		}

		public Long getId() {
			return id;
		}

		@Override
		public String toString() {
			return "TokenEntity [token=" + token + ", id=" + id + "]";
		}

	}

	@EJB(beanName = "userDao")
	private UserDao dao;

	@Context
	HttpServletRequest req;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllUsers(@QueryParam("adminId") Long adminId) {
		if (adminId == null) {
			return Response.status(Status.BAD_REQUEST).entity("{\"message\":\"Cannot have null admin id\"}").build();
		}
		List<UserTO> list = dao.findAllUsersWithComplaints(adminId);
		return Response.status(200).entity(list).build();
	}

	/*
	 * @PUT
	 * 
	 * @Consumes(MediaType.APPLICATION_JSON)
	 * 
	 * @Produces(MediaType.APPLICATION_JSON) public Response save(User user) {
	 * if (user.getId() != null) {
	 * Response.status(Status.CONFLICT).entity(getResponseEntity(409,
	 * "The parameter must have id==null")).build(); } try {
	 * dao.createOrEdit(user); return
	 * Response.status(201).type(MediaType.APPLICATION_JSON)
	 * .entity(getResponseEntity(201, "Entity created")).build();
	 * 
	 * } catch (EntityNotCreatedException e) { e.printStackTrace(); return
	 * Response.status(Status.CONFLICT).entity(getResponseEntity(409,
	 * "Conflict")).build(); } }
	 * 
	 */
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response save(@PathParam("id") Long id, @QueryParam("adminId") Long adminId, User user) {
		if (adminId == null) {
			return Response.status(Status.BAD_REQUEST).entity("{\"message\":\"Cannot have null admin id\"}").build();
		}
		try {
			user.setId(id);
			if (!dao.findById(id).getAdmin().getId().equals(adminId)) {
				throw new EntityNotFoundException("");
			}
			System.out.println("in save " + dao.edit(user));
			return Response.status(200).type(MediaType.APPLICATION_JSON)
					.entity(getResponseEntity(200, "Entity modified")).build();

		} catch (EntityNotFoundException e) {
			return Response.status(Status.NOT_FOUND)
					.entity(getResponseEntity(404, "User with id " + id + " does not exist")).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Response getUser(@PathParam("id") Long id, @QueryParam("adminId") Long adminId) {
		if (adminId == null) {
			return Response.status(400).entity(JsonUtils.getResponseEntity(400, "adminId cannot be null")).build();
		}
		UserTO user = dao.findUserWIthComplaint(id, adminId);
		if (user == null) {
			return Response.status(404).entity(JsonUtils.getResponseEntity(404, "could not find user")).build();
		}
		return Response.status(200).entity(user).build();

	}

	@PUT
	@Path("/image/{id}")
	@Consumes("image/*")
	public Response save(@PathParam("id") Long id, byte[] content) {

		System.out.println(req.getContentType());

		System.out.println("in save image");
		System.out.println(Arrays.toString(content));
		if (dao.findById(id) == null) {
			System.out.println("image for id " + id + " not found");
			return Response.status(404).entity(JsonUtils.getResponseEntity(404, "user with id " + id + " not found"))
					.build();
		} else {

			String fileType = req.getContentType().split("/")[1];
			java.nio.file.Path p = CIRSConstants.getImageDir(ImageDir.USER).resolve(id + "." + fileType);
			System.out.println("path in save " + p);
			try {
				synchronized (p) {
					Files.deleteIfExists(p);
					Files.createFile(p);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.out.println("creating path " + p.getFileName());
			try (FileOutputStream fos = new FileOutputStream(p.toFile())) {
				fos.write(content);
				return Response.status(200).build();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response verifyCredentials(User user) {
		System.out.println("here in verify");
		if (user == null) {
			return Response.status(400).entity(
					JsonUtils.getResponseEntity(400, "The request body is null, or cannot be serialized to User"))
					.build();
		}
		UserTO u = dao.verifyCredentials(user.getUserName(), user.getPassword());
		return u != null ? Response.status(200).type(MediaType.APPLICATION_JSON).entity(u).build()
				: Response.status(404).entity(JsonUtils.getResponseEntity(404, "username or password is invalid"))
						.build();
	}

	@PUT
	@Path("/token")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response editToken(TokenEntity token) {
		try {
			System.out.println(token);
			User user = dao.findById(token.getId());
			if (user == null) {
				throw new EntityNotFoundException("");
			}
			user.setGcmToken(token.getToken());
			dao.edit(user);
			return Response.status(200).build();
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
			return Response.status(404).build();
		}
	}
}
