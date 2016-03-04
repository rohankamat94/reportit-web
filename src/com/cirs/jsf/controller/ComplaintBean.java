package com.cirs.jsf.controller;

import static com.cirs.util.Utils.getAsMap;import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

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

	@EJB
	ComplaintDao dao;

	@SuppressWarnings("serial")
	public LazyDataModel<Complaint> getComplaints() {
		if (complaints == null) {
			complaints = new LazyLoader<Complaint>(dao) {
				@Override
				public Map<String, Object> getSearchParams() {
					Map<String, Object> map = new HashMap<>();
					map.put("user", getAsMap("admin", getAsMap("id", getAdmin().getId())));
					return map;
				}
				@Override
				public List<Complaint> load(int first, int pageSize, String sortField, SortOrder sortOrder,
						Map<String, Object> filters) {
					List<Complaint> result= super.load(first, pageSize, sortField, sortOrder, filters);
					if("status".equals(sortField)){
						Comparator<Complaint> comp=Comparator.comparing(Complaint::getStatus);
						if(sortOrder==SortOrder.DESCENDING){
							comp=comp.reversed();
						}
						return result.stream().sorted(comp).collect(Collectors.toList());
					}
					return result;
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

	public String viewComplaint() {
		String viewComplaint = "View.xhtml?faces-redirect=true&complaintId=" + selected.getId();
		return viewComplaint;
	}
}
