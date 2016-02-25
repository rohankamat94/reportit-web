package com.cirs.jsf.controller;

import org.primefaces.event.data.PageEvent;

import com.cirs.core.CIRSConstants;
import com.cirs.entities.Admin;
import com.cirs.jsf.util.JsfUtils;

public abstract class BaseEntityController<T> {

	public abstract void setSelected(T t);

	public void onPageChange(PageEvent event) {
		setSelected(null);
	}

	public Admin getAdmin() {
		return (Admin) JsfUtils.getExternalContext().getSessionMap().get(CIRSConstants.LOGIN_ATTRIBUTE_KEY);
	}

}
