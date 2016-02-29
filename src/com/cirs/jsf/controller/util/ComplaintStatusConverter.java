package com.cirs.jsf.controller.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.cirs.entities.Complaint.Status;

@FacesConverter("complaintStatusConverter")
public class ComplaintStatusConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		// TODO Auto-generated method stub
		return Status.valueOf(value.replaceAll("\\s+", "").toUpperCase());
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (!(value instanceof Status)) {
			throw new RuntimeException("Converter not suitable for object of class " + value.getClass());
		}
		return value.toString();
	}

}
