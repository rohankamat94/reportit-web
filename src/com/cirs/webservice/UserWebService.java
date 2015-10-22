package com.cirs.webservice;

import static com.cirs.webservice.util.JsonUtils.getResponseEntity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.cirs.core.CIRSConstants;
import com.cirs.dao.remote.UserDao;
import com.cirs.entities.User;
import com.cirs.exceptions.EntityNotFoundException;

@Path("/user")
public class UserWebService {

	@EJB(beanName = "userDao")
	private UserDao dao;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getAllUsers() {
		return dao.findAll();
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
	public Response save(@PathParam("id") Long id, User user) {
		System.out.println(user == null);
		System.out.println(id);
		System.out.println(user.getId());
		System.out.println(user.getId() + " " + id);
		if (!user.getId().equals(id)) {
			return Response.status(400).type(MediaType.APPLICATION_JSON)
					.entity(getResponseEntity(400, "Resource id does not match user id")).build();

		}
		try {
			System.out.println("in save " + dao.edit(user));
			return Response.status(200).type(MediaType.APPLICATION_JSON)
					.entity(getResponseEntity(200, "Entity modified")).build();

		} catch (EntityNotFoundException e) {
			return Response.status(Status.NOT_FOUND)
					.entity(getResponseEntity(404, "User with id " + id + " does not exist")).build();
		}
	}

	@PUT
	@Path("/image/{id}")
	@Consumes(MediaType.APPLICATION_OCTET_STREAM)
	public Response save(@PathParam("id") Long id, @QueryParam("fileType") String fileType, byte[] content) {
		System.out.println("in save image");
		System.out.println(Arrays.toString(content));
		if (dao.findById(id) == null) {
			System.out.println("image for id " + id + " not found");
			return Response.status(404).build();
		} else {
			if (fileType == null || fileType.equals("")) {
				// Assume png, since it's from android
				fileType = "png";
			}
			java.nio.file.Path p = CIRSConstants.getUserImageDir().resolve(id + "." + fileType);
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

}
