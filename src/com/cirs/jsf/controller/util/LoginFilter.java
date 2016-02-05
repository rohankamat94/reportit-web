package com.cirs.jsf.controller.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.jasper.runtime.JspFactoryImpl;

import static com.cirs.core.CIRSConstants.*;
import com.cirs.jsf.util.JsfUtils;

@WebFilter("/faces/pages/*")
public class LoginFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		System.out.println("here in do filter for" + req.getServletPath());
		HttpSession session = req.getSession();
		Object user = session.getAttribute(LOGIN_ATTRIBUTE_KEY);	
		if(user==null){
			String cookieVal=JsfUtils.getCookieValue(req, LOGIN_ATTRIBUTE_KEY);
			if(cookieVal!=null){
				user=cookieVal;
				session.setAttribute(LOGIN_ATTRIBUTE_KEY, user);
				JsfUtils.addCookie(resp, LOGIN_ATTRIBUTE_KEY, cookieVal, 60*60*24*7*52);
			}
		}
		if (user == null) {
			resp.sendRedirect(req.getContextPath() + "/faces/login.xhtml");
		} else {
			chain.doFilter(req, resp);
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		System.out.println("in init");
	}
}
