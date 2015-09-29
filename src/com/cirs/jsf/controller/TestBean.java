package com.cirs.jsf.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import com.cirs.dao.remote.UserDao;
import com.cirs.entities.User;
import com.cirs.entities.UserUploadResponse;
import com.cirs.jsf.util.JsfUtils;

@ManagedBean
@ViewScoped
public class TestBean implements Serializable {

	@EJB(beanName = "userDao")
	UserDao dao;

	private StreamedContent sampleFile;
	private List<User> users;
	private UserUploadResponse response;

	@PostConstruct
	public void init() {

		InputStream is = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext())
				.getResourceAsStream("/resources/sample.xlsx");
		sampleFile = new DefaultStreamedContent(is, "application/vnd.ms-excel", "sample.xlsx");

	}

	public void onUpload(FileUploadEvent event) {
		System.out.println("in listener");
		try {
			JsfUtils.beginLoader();
			UploadedFile file = event.getFile();
			try {
				File f = new File(file.getFileName());
				System.out.println(f.getName() + " " + f.getName().endsWith("xlsx"));
				if(!f.getName().endsWith("xlsx") && !f.getName().endsWith("xls")){
					JsfUtils.showSnackBar("Incorrect file type");
					JsfUtils.endLoader();
					return;
				}
				FileOutputStream fos = new FileOutputStream(f);
				InputStream fis = file.getInputstream();
				byte[] b = new byte[1024];
				while (fis.read(b) != -1) {
					fos.write(b);
				}
				response = dao.upload(f);
				System.out.println(response.getEntitiesCreated() + " " + response.getErrors());
				int newUser = response.getEntitiesCreated();
				if (newUser > 0) {
					System.out.println("showSnackBar('" + newUser + " users created');");
					JsfUtils.showSnackBar(newUser + " users created");
					JsfUtils.endLoader();
				}
				fos.flush();
				fos.close();
			} catch (Exception e) {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		JsfUtils.endLoader();
	}

	public void clear() {
		System.out.println("in listener");
		response = null;
	}

	public StreamedContent getSampleFile() {
		return sampleFile;
	}

	public String getMessage() {
		return dao.findAll().toString();
	}

	public List<User> getUsers() {
		users = dao.findAll();
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public UserUploadResponse getResponse() {
		return response;
	}

}
