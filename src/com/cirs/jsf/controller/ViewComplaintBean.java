package com.cirs.jsf.controller;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.cirs.dao.remote.ComplaintDao;
import com.cirs.entities.Complaint;
import com.cirs.entities.Complaint.ComplaintTO;

@ViewScoped
@ManagedBean(name = "viewComplaintBean")
public class ViewComplaintBean {

	@EJB(beanName = "complaintDao")
	private ComplaintDao dao;

	private Complaint complaint;
	private Long complaintId;

	public void init() {
		complaint = dao.findByIdWithComments(complaintId);		
	}

	public Long getComplaintId() {
		return complaintId;
	}

	public void setComplaintId(Long complaintId) {
		this.complaintId = complaintId;

	}

	public Complaint getComplaint() {
		return complaint;
	}

}
