package com.cirs.jsf.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.primefaces.event.data.PageEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.cirs.dao.remote.CategoryDao;
import com.cirs.entities.Category;
import com.cirs.exceptions.EntityNotCreatedException;
import com.cirs.exceptions.EntityNotFoundException;
import com.cirs.jsf.controller.util.LazyLoader;
import com.cirs.jsf.util.JsfUtils;
import com.cirs.util.Utils;

@ManagedBean
@ViewScoped
public class CategoryBean implements Serializable {
	private LazyDataModel<Category> categories;

	@EJB(beanName = "categoryDao")
	private CategoryDao catDao;

	private Category selected;
	private Category create;

	public LazyDataModel<Category> getCategories() {
		if (categories == null) {
			categories = new LazyLoader<Category>(catDao) {
				@Override
				public List<Category> load(int first, int pageSize, String sortField, SortOrder sortOrder,
						Map<String, Object> filters) {
					if (sortField == null) {
						sortField = "name"; // If no sort is specified, default
											// to name
					}
					return super.load(first, pageSize, sortField, sortOrder, filters);
				}
			};
		}
		return categories;
	}

	public void setCategories(LazyDataModel<Category> categories) {
		this.categories = categories;
	}

	public Category getSelected() {
		return selected;
	}

	public void setSelected(Category selected) {
		this.selected = selected;
	}

	public void onPageChange(PageEvent event) {
		setSelected(null);
	}

	public void deleteCategory() {
		Objects.requireNonNull(selected);
		catDao.delete(selected.getId());
		setSelected(null);
	}

	public void onAddClicked(ActionEvent event) {
		System.out.println("Here");
		Category cat = new Category();
		cat.setActive(false);
		setCreate(cat);
	}

	public void saveCategory() {
		System.out.println(create.getName() + " " + create.getActive());
		if (create.getName() == null || create.getName().isEmpty()) {
			JsfUtils.showSnackBar("Category Name cannot be empty");
			JsfUtils.executeJS("showAddCategory();");
			return;
		}
		try {
			catDao.create(create);
		} catch (EntityNotCreatedException e) {
			e.printStackTrace();
			JsfUtils.showSnackBar("Category with name " + create.getName() + " already exists");
		}
		setCreate(null);
	}

	public void cancelDialog() {
		// Category cat = new Category();
		// cat.setActive(false);
		setCreate(null);
		System.out.println("cancelling add");
	}

	public Category getCreate() {
		return create;
	}

	public void setCreate(Category create) {
		this.create = create;
	}

	public void editCategory() {
		try {
			catDao.edit(getSelected());
			setSelected(null);
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
	}

}
