package app.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.dao.UserDAO;
import app.model.User;


@WebServlet("/")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO userDAO;
     
    public UserServlet() {
        super();
        this.userDAO = new UserDAO();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String path = request.getServletPath();
		switch(path) {
		case "/new":
			addUser(request, response);
			break;
		case "/insert":
			try {
				insertUser(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case "/delete":
			try {
				deleteUser(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case "/edit":
			try {
				editUser(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case "/update":
			try {
				updateUser(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		default:
			try {
				listAllUsers(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private void addUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-form.jsp");
		dispatcher.forward(request, response);
	}
	
	private void insertUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		String nameString = request.getParameter("name");
		String countString = request.getParameter("country");
		String emaiString = request.getParameter("email");
		User user = new User(nameString, emaiString, countString);
		userDAO.insertUser(user);
		response.sendRedirect("list");
	}
	
	private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		userDAO.deleteUser(id);
		response.sendRedirect("list");
		
	}
	
	private void editUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
		int id = Integer.parseInt(request.getParameter("id"));
		User user = userDAO.selectUser(id);		// retrieve user with id
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-form.jsp");
		request.setAttribute("user", user);
		dispatcher.forward(request, response);
	}
	
	private void updateUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException{
		
		int id = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name");
		String country = request.getParameter("country");
		String email = request.getParameter("email");
		User user = new User(id, name, email, country);
		userDAO.updateUser(user);
		response.sendRedirect("list");
	}
	
	private void listAllUsers(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
		List<User> list = userDAO.selectAllUsers();
		request.setAttribute("list", list);
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-list.jsp");
		dispatcher.forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

}
