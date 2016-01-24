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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.cirs.core.CIRSConstants;
import com.cirs.core.CIRSConstants.ImageDir;
import com.cirs.dao.remote.ComplaintDao;
import com.cirs.entities.Complaint;
import com.cirs.exceptions.EntityNotCreatedException;

@Path("/complaint")
public class ComplaintWebService {

	@EJB(beanName = "complaintDao")
	private ComplaintDao dao;
	
	@Context
	HttpServletRequest req;
	
	@PUT
	@Path("/image/{id}")
	@Consumes("image/*")
	public Response save(@PathParam("id") Long id, byte[] content) {

		System.out.println(req.getContentType());

		System.out.println("in save image");
		System.out.println(Arrays.toString(content));
		if (dao.findById(id) == null) {
			System.out.println("image for id " + id + " not found");
			return Response.status(404).build();
		} else {

			String fileType = req.getContentType().split("/")[1];
			java.nio.file.Path p = CIRSConstants.getImageDir(ImageDir.COMPLAINT).resolve(id + "." + fileType);
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
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Complaint> getComplaints(){
		System.out.println("dao null " + (dao==null));
		return dao.findAll(); 	
	} 
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addComplaint(Complaint complaint) {
		try {
			System.out.println(complaint);
			dao.create(complaint);
			System.out.println("after create complaint id =" + complaint );
			return Response.status(Status.CREATED).type(MediaType.APPLICATION_JSON)
					.entity(complaint).build();
		} catch (EntityNotCreatedException e) {
			e.printStackTrace();
			return Response.status(400).type(MediaType.APPLICATION_JSON)
					.entity(getResponseEntity(400, "Could not persist complaint")).build();
		}
	}
}