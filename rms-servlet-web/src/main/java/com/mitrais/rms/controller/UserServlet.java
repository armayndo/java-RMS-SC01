package com.mitrais.rms.controller;

import com.mitrais.rms.dao.UserDao;
import com.mitrais.rms.dao.impl.UserDaoImpl;
import com.mitrais.rms.model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet("/users/*")
public class UserServlet extends AbstractController {

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String username, userpass;
		UserDao userDao = null;
		String action = req.getPathInfo();

		try {
			switch (action) {
			case "/save":
				username = req.getParameter("username");
				userpass = req.getParameter("userpass");

				User newUser = new User(null, username, userpass);
				userDao = UserDaoImpl.getInstance();
				userDao.save(newUser);

				resp.sendRedirect("list");
				break;
			case "/update":
				Long id = Long.parseLong(req.getParameter("id"));
				username = req.getParameter("username");
				userpass = req.getParameter("userpass");

				User user = new User(id, username, userpass);
				userDao = UserDaoImpl.getInstance();
				userDao.update(user);
				resp.sendRedirect("list");
				break;
			}
		} catch (Exception ex) {
			throw new ServletException(ex);
		}

		// doGet(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		UserDao userDao = null;
		Long id = null;

		String path = getTemplatePath(req.getServletPath() + req.getPathInfo());
		String action = req.getPathInfo();

		try {
			switch (action) {
			case "/new":
				path = getTemplatePath(req.getServletPath() + "/form");
				break;
			case "/delete":
				id = Long.parseLong(req.getParameter("id"));

				User user = new User(id);
				userDao = UserDaoImpl.getInstance();
				userDao.delete(user);
				break;
			case "/edit":
				id = Long.parseLong(req.getParameter("id"));

				userDao = UserDaoImpl.getInstance();
				User existingUser = userDao.findById(id);
				
				req.setAttribute("user", existingUser);
				path = getTemplatePath(req.getServletPath() + "/formUpdate");
				break;
			case "/list":
				userDao = UserDaoImpl.getInstance();
				List<User> users = userDao.findAll();
				req.setAttribute("users", users);
				break;
			default:
				path = getTemplatePath(req.getServletPath() + "/list");
				break;
			}
		} catch (Exception ex) {
			throw new ServletException(ex);
		}

		if (action.contains("/delete")) {
			resp.sendRedirect("list");
		} else {
			RequestDispatcher requestDispatcher = req.getRequestDispatcher(path);
			requestDispatcher.forward(req, resp);
		}

	}

}
