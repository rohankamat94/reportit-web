package com.cirs.jsf.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import com.cirs.dao.remote.UserDao;
import com.cirs.entities.User;
import com.cirs.entities.UserUploadResponse;
import com.cirs.jsf.controller.util.LazyLoader;
import com.cirs.jsf.util.JsfUtils;
import com.cirs.util.Utils;

@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class UserBean extends BaseEntityController<User> implements Serializable {

	@EJB
	UserDao dao;

	private StreamedContent sampleFile;
	private LazyLoader<User> users;
	private UserUploadResponse response;
	private User selected;

	@PostConstruct
	public void init() {

		InputStream is = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext())
				.getResourceAsStream("/resources/sample.xlsx");
		sampleFile = new DefaultStreamedContent(is, "application/vnd.ms-excel", "sample.xlsx");
	}

	public void onUpload(FileUploadEvent event) {
		System.out.println("in listener");
		try {
			UploadedFile file = event.getFile();
			try {
				File f = new File(file.getFileName());
				System.out.println(f.getName() + " " + f.getName().endsWith("xlsx"));
				if (!f.getName().endsWith("xlsx") && !f.getName().endsWith("xls")) {
					JsfUtils.showSnackBar("Incorrect file type");
					return;
				}
				FileOutputStream fos = new FileOutputStream(f);
				InputStream fis = file.getInputstream();
				byte[] b = new byte[1024];
				while (fis.read(b) != -1) {
					fos.write(b);
				}
				response = dao.upload(getAdmin(), f);
				System.out.println(response.getEntitiesCreated() + " " + response.getErrors());
				int newUser = response.getEntitiesCreated();
				System.out.println("showSnackBar('" + newUser + " users created');");
				JsfUtils.showSnackBar(newUser + " users created");
				fos.flush();
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clear() {
		System.out.println("in listener");
		response = null;
	}

	public StreamedContent getSampleFile() {
		return sampleFile;
	}

	/*
	 * public String getMessage() { return dao.findAll().toString(); }
	 */
	public LazyLoader<User> getUsers() {
		if (users == null) {
			System.out.println("here");
			users = new LazyLoader<User>(dao) {
				@Override
				public Map<String, Object> getSearchParams() {
					Map<String, Object> map = new HashMap<>();
					map.put("admin", Utils.getAsMap("id", getAdmin().getId()));
					return map;
				}
			};
		}
		return users;
	}

	public void setUsers(LazyLoader<User> users) {
		this.users = users;
	}

	public UserUploadResponse getResponse() {
		return response;
	}

	public User getSelected() {
		return selected;
	}

	public void setSelected(User selected) {
		this.selected = selected;
	}

	public void deleteUser() {
		if (selected != null) {
			dao.delete(selected.getId());
			setSelected(null);
		}
	}
}
