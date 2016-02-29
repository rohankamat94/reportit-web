package com.cirs.jsf.controller;

import java.io.IOException;
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
import com.cirs.entities.Complaint.Status;
import com.cirs.exceptions.EntityNotFoundException;
import com.cirs.jsf.util.JsfUtils;
import com.cirs.util.ActionStatusMapper;

@ViewScoped
@ManagedBean(name = "viewComplaintBean")
public class ViewComplaintBean {

	@EJB(beanName = "complaintDao")
	private ComplaintDao dao;

	private Complaint complaint;
	private Long complaintId;
	private String commentData;
	public String action;

	public void init() throws IOException {
		Long adminId = ((Admin) JsfUtils.getExternalContext().getSessionMap().get(CIRSConstants.LOGIN_ATTRIBUTE_KEY))
				.getId();
		complaint = dao.findByIdWithComments(complaintId, adminId);
		System.out.println("called init");
		if (complaint == null) {
			JsfUtils.getExternalContext().responseSendError(404, "invalid complaint id" + complaintId);
		}
	}

	public void changeStatus() {
		if (action == null || action.isEmpty()) {
			return;
		}
		Status newStatus = ActionStatusMapper.getNewStatus(action, complaint.getStatus());
		System.out.println("status:" + complaint.getStatus() + " action:" + action + " new Action " + newStatus);
		complaint.setStatus(newStatus);
		action = null;
		try {
			dao.edit(complaint);
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
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

	public String getRedirect() {
		return "index.xhtml";
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
}
