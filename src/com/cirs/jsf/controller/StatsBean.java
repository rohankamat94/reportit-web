package com.cirs.jsf.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;

import com.cirs.core.CIRSConstants;
import com.cirs.dao.remote.CategoryDao;
import com.cirs.dao.remote.ComplaintDao;
import com.cirs.entities.Admin;
import com.cirs.entities.Category;
import com.cirs.entities.Complaint;
import com.cirs.entities.Complaint.Status;
import com.cirs.jsf.util.JsfUtils;

@ManagedBean(name = "statsBean")
@ViewScoped
public class StatsBean {
	@EJB
	private ComplaintDao cDao;
	@EJB
	private CategoryDao catDao;

	private List<Complaint> complaints;
	private List<Category> categories;

	private HorizontalBarChartModel compVsCatModel = new HorizontalBarChartModel();
	private BarChartModel compStatusModel = new BarChartModel();
	private BarChartModel compVsLocationModel = new BarChartModel();

	@PostConstruct
	public void init() {
		Long adminId = ((Admin) JsfUtils.getExternalContext().getSessionMap().get(CIRSConstants.LOGIN_ATTRIBUTE_KEY))
				.getId();

		complaints = cDao.findAll(adminId);
		categories = catDao.findAll(adminId);

		Map<String, Long> compVsCatMap = getCompVsCatMap();
		Map<String, Long> compStatus = getCompStatusMap();
		Map<String, Long> compVsLoc = getCompVsLocMap();

		createModel(compVsCatModel,compVsCatMap,"No. of complaints per category","Category","Complaints");
		createModel(compStatusModel, compStatus, "No. of complaints per status", "Status", "Complaints");
		createModel(compVsLocationModel, compVsLoc, "No. of complaints per location", "Location", "Complaints");
	}

	private Map<String, Long> getCompVsLocMap() {
		Map<String, Long> map = complaints.stream()
				.collect(Collectors.groupingBy(Complaint::getLocation, Collectors.counting()));
		return map;
	}

	private Map<String, Long> getCompStatusMap() {
		Map<String, Long> map = new HashMap<>();
		Status[] statuses = Status.values();
		for (Status s : statuses) {
			long count = complaints.stream().filter(c -> c.getStatus().equals(s)).count();
			map.put(s.toString(), count);

		}

		return map;
	}

	private Map<String, Long> getCompVsCatMap() {
		Map<String, Long> map = new HashMap<>();
		for (Category cat : categories) {
			long count = complaints.stream().filter(c -> c.getCategory().equals(cat)).count();
			map.put(cat.getName(), count);
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	private void createModel(BarChartModel model, Map<? extends Object, ? extends Number> map, String title,
			String xLabel, String yLabel) {
		model.setTitle(title);
		model.setAnimate(true);
		model.setSeriesColors("FF6E40");
		Axis x = model.getAxis(AxisType.X);
		x.setLabel(xLabel);
		Axis y = model.getAxis(AxisType.Y);
		y.setLabel(yLabel);

		ChartSeries cs = new ChartSeries();
		cs.setData((Map<Object, Number>) map);
		model.addSeries(cs);
	}

	public BarChartModel getCompVsCatModel() {
		return compVsCatModel;
	}

	public BarChartModel getCompStatusModel() {
		return compStatusModel;
	}

	public BarChartModel getCompVsLocationModel() {
		return compVsLocationModel;
	}

	public void setCompVsLocationModel(BarChartModel compVsLocationModel) {
		this.compVsLocationModel = compVsLocationModel;
	}

	public String getChartHeightStyle() {
		System.out.println("in chart height");
		String style = "height:" + (categories.size() * 65) + "px";
		System.out.println(style);
		return style;
	}
}