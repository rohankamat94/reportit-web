package com.cirs.jsf.controller;

import static com.cirs.util.Utils.getAsMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.LazyDataModel;

import com.cirs.dao.remote.ComplaintDao;
import com.cirs.entities.Complaint;
import com.cirs.entities.Complaint.Status;
import com.cirs.exceptions.EntityNotFoundException;
import com.cirs.jsf.controller.util.LazyLoader;

@ManagedBean(name = "complaintBean")
@ViewScoped
public class ComplaintBean extends BaseEntityController<Complaint> {
	private LazyDataModel<Complaint> complaints;
	private Complaint selected;
	private Status newStatus;

	@EJB(beanName = "complaintDao")
	ComplaintDao dao;

	public LazyDataModel<Complaint> getComplaints() {
		if (complaints == null) {
			complaints = new LazyLoader<Complaint>(dao) {
				@Override
				public Map<String, Object> getSearchParams() {
					Map<String, Object> map = new HashMap<>();
					map.put("user", getAsMap("admin", getAsMap("id", getAdmin().getId())));
					return map;
				}
			};
		}
		return complaints;
	}

	public void setComplaints(LazyDataModel<Complaint> complaints) {
		this.complaints = complaints;
	}

	public Complaint getSelected() {
		return selected;
	}

	@Override
	public void setSelected(Complaint selected) {
		this.selected = selected;
	}

	public Status[] getStatuses() {
		return Status.values();
	}

	public Status getNewStatus() {
		return selected != null ? selected.getStatus() : newStatus;
	}

	public void setNewStatus(Status newStatus) {
		this.newStatus = newStatus;
	}

	public void editComplaint() {
		Objects.requireNonNull(selected);
		Objects.requireNonNull(newStatus);
		if (selected.getStatus().equals(newStatus)) {
			return;
		}
		selected.setStatus(newStatus);
		try {
			dao.edit(selected);
			newStatus = null;
			setSelected(null);
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public String viewComplaint(){
		String viewComplaint="View.xhtml?faces-redirect=true&complaintId="+selected.getId();
		return viewComplaint;
	}
}
