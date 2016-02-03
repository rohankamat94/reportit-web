package com.cirs.jsf.controller;

import org.primefaces.event.data.PageEvent;

public abstract class BaseEntityController<T> {

	public abstract void setSelected(T t);

	public void onPageChange(PageEvent event) {
		setSelected(null);
	}

}
