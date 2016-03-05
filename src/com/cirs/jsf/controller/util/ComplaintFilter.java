package com.cirs.jsf.controller.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

@WebFilter("/faces/pages/complaints/index.xhtml")
public class ComplaintFilter implements Filter {
	@Override
	public void doFilter(ServletRequest request, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		System.out.println(" in do filter for complaint");
		HttpServletResponse response = (HttpServletResponse) resp;
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		chain.doFilter(request, resp);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}
}
