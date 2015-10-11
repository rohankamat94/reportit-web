package com.cirs.jsf.controller.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.cirs.dao.remote.Dao;
import com.cirs.entities.CirsEntity;
import com.cirs.entities.User;
import com.cirs.util.Utils;

@SuppressWarnings("serial")
public class LazyLoader<T extends CirsEntity> extends LazyDataModel<T> {
	Dao<T> dao;

	public LazyLoader(Dao<T> dao) {
		this.dao = dao;
	}

	@Override
	public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
		// System.out.println((filters == null) + " " +
		// (filters.keySet().size()));

		Map<String, Object> sortMap = new HashMap<>();
		if (sortField != null) {
			sortMap = Utils.getAsMap(sortField, (Object) (sortOrder == SortOrder.ASCENDING));
		}
		System.out.println(sortMap);
		for (Map.Entry<String, Object> entry : filters.entrySet()) {
			System.out.println("entry: " + entry.getKey() + " " + entry.getValue());
		}
		setRowCount(dao.countAllLazy(filters).intValue());
		List<T> resultList = dao.findAllLazy(first, pageSize, filters, sortMap);
		for (T t : resultList) {
			System.out.println(((User) t).getUserName());
		}
		return resultList;

	}

	@Override
	public Object getRowKey(T object) {
		return object.getId();
	}

	@Override
	public T getRowData(String rowKey) {
		System.out.println("row key" + rowKey);
		return dao.findById(rowKey);
	}
}
