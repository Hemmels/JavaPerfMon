package com.hemmels.javaperfmon.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hemmels.javaperfmon.bean.MonitorBean;

@WebServlet(name = "Monitor", urlPatterns = {"/Monitor"})
public class MonitorServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("application/html; charset=UTF-8");

		MonitorBean monitorBean = new MonitorBean();
		monitorBean.setTest("Hello world");
		request.setAttribute("bean", monitorBean);

		RequestDispatcher dispatcher = request.getRequestDispatcher("pages/monitor.jsp");
		dispatcher.forward(request, response);
	}
}
