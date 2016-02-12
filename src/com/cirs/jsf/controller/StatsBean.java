package com.cirs.jsf.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import com.cirs.core.CIRSConstants;
import com.cirs.dao.remote.CategoryDao;
import com.cirs.dao.remote.ComplaintDao;
import com.cirs.entities.Admin;
import com.cirs.entities.Category;
import com.cirs.entities.Complaint;
import com.cirs.jsf.util.JsfUtils;

@ManagedBean(name = "statsBean")
@ViewScoped
public class StatsBean {
	@EJB
	private ComplaintDao compDao;
	@EJB
	private CategoryDao catDao;

	private BarChartModel compVsCatModel;
	private Map<String, Long> compVsCatMap = new HashMap<>();

	@PostConstruct
	public void init() {

		initMaps();
		initCompVsCatModel();
	}

	private void initCompVsCatModel() {
		compVsCatModel = new BarChartModel();
		compVsCatModel.setTitle("No. of Complaints per Category");
		compVsCatModel.setAnimate(true);
		compVsCatModel.setSeriesColors("2196f3");
		Axis x = compVsCatModel.getAxis(AxisType.X);
		Axis y = compVsCatModel.getAxis(AxisType.Y);

		x.setLabel("Complaints");

		y.setLabel("Category");
		y.setMin(0L);
		y.setMax(compVsCatMap.values().stream().mapToLong(l -> l).max().getAsLong());

		ChartSeries cs = new ChartSeries();
		for (Entry<String, Long> e : compVsCatMap.entrySet()) {
			cs.set(e.getKey(), e.getValue());
		}

		compVsCatModel.addSeries(cs);
	}

	private void initMaps() {
		Admin admin = (Admin) JsfUtils.getExternalContext().getSessionMap().get(CIRSConstants.LOGIN_ATTRIBUTE_KEY);
		List<Category> catList = catDao.findAll(admin.getId());
		List<Complaint> compList = compDao.findAll(admin.getId());
		for (Category cat : catList) {
			long count = compList.stream().filter(c -> c.getCategory().equals(cat)).count();
			compVsCatMap.put(cat.getName(), count);
		}

	}

	public BarChartModel getBcm() {
		return compVsCatModel;
	}

	public void setBcm(BarChartModel bcm) {
		this.compVsCatModel = bcm;
	}

	public Map<String, Long> getCompVsCatMap() {
		return compVsCatMap;
	}

	public void setCompVsCatMap(Map<String, Long> compVsCatMap) {
		this.compVsCatMap = compVsCatMap;
	}
}
