package com.cirs.jsf.controller;

import java.sql.Timestamp;
import java.util.Date;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.cirs.core.CIRSConstants;
import com.cirs.dao.remote.ComplaintDao;
import com.cirs.entities.Admin;
import com.cirs.entities.Comment;
import com.cirs.entities.Complaint;
import com.cirs.entities.Complaint.ComplaintTO;
import com.cirs.exceptions.EntityNotFoundException;
import com.cirs.jsf.util.JsfUtils;

@ViewScoped
@ManagedBean(name = "viewComplaintBean")
public class ViewComplaintBean {

	@EJB(beanName = "complaintDao")
	private ComplaintDao dao;

	private Complaint complaint;
	private Long complaintId;
	private String commentData;

	public void init() {
		Long adminId = ((Admin) JsfUtils.getExternalContext().getSessionMap().get(CIRSConstants.LOGIN_ATTRIBUTE_KEY))
				.getId();
		complaint = dao.findByIdWithComments(complaintId, adminId);

	}

	public void addComment() {
		if (commentData == null || (commentData = commentData.trim()).equals("")) {
			return;
		}

		System.out.println("here in add comment " + commentData);
		Comment comment = new Comment();
		comment.setData(commentData);
		comment.setTime(new Timestamp(new Date().getTime()));
		comment.setUser(null); // admin comment, null user
		comment.setComplaint(complaint);
		complaint.getComments().add(comment);
		try {
			dao.edit(complaint);
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
		commentData = null;
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

	public String getComment() {
		return commentData;
	}

	public void setComment(String comment) {
		this.commentData = comment;
	}

}
