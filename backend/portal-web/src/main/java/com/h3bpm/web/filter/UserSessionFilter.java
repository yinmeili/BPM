package com.h3bpm.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Component;

import com.h3bpm.web.utils.Constants;
import com.h3bpm.web.utils.UserSessionUtils;
import com.h3bpm.web.vo.UserSessionInfo;

/**
 * 自定义拦截filter,存入session信息
 * 
 * @author yangj
 */
@Component
@ServletComponentScan
@WebFilter(urlPatterns="/*")
public class UserSessionFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("init UserSessionFilter");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest session_request = (HttpServletRequest) request;
		HttpSession session = session_request.getSession();
		UserSessionInfo userSessionInfo = (UserSessionInfo) session.getAttribute(Constants.SESSION_USER);
		
		 /*
		  *	由于每一次请求都是一个独立的线程，ThreadLocal中的变量需要我们通过session做一个中专的配置，
		  * 每次请求都判断这个session中是否存在用户信息，
		  * 如果session中存在用户信息就将用户信息保存到ThreadLocal中 
		  */
		if (userSessionInfo != null) {
			//先销毁在添加否则触发不了监听器中的attributeAdded
			session.removeAttribute(Constants.SESSION_USER);
			session.setAttribute(Constants.SESSION_USER, userSessionInfo);
			UserSessionUtils.set(Constants.SESSION_USER, userSessionInfo);
		}
		
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		System.out.println("destroy UserSessionFilter");
	}

}
